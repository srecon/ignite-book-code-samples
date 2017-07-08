package com.blu.imdg.example10;

import com.blu.imdg.common.bankData.BankDataGenerator;
import com.blu.imdg.common.bankData.model.AccountCacheData;
import com.blu.imdg.common.bankData.model.AccountCacheKey;
import com.blu.imdg.common.bankData.model.AccountData;
import com.blu.imdg.common.bankData.model.AccountKey;
import com.blu.imdg.example10.dto.ValidateRequest;
import com.blu.imdg.example10.dto.ValidateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.locks.Lock;

/**
 * Created by mikl on 26.10.16.
 */
public class AsyncBankServiceImpl implements AsyncBankService, Service {

    @IgniteInstanceResource
    private Ignite ignite;

    IgniteCache<AccountCacheKey, AccountCacheData> accountCache;

    private String zeroMqBrokerAddress;


    public AsyncBankServiceImpl(String zeroMqBrokerAddress) {
        this.zeroMqBrokerAddress = zeroMqBrokerAddress;
    }


    @Override
    public void cancel(ServiceContext serviceContext) {

    }

    @Override
    public void init(ServiceContext serviceContext) throws Exception {
        accountCache = ignite.getOrCreateCache(BankDataGenerator.ACCOUNT_CACHE);
    }

    @Override
    public void execute(ServiceContext serviceContext) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Context context = ZMQ.context(1);

        //  Socket to talk to server
        Socket responder = context.socket(ZMQ.REP);
        responder.connect(zeroMqBrokerAddress);
        ZMQ.PollItem items[] = {new ZMQ.PollItem(responder, ZMQ.Poller.POLLIN)};
        while (!Thread.currentThread().isInterrupted() && !serviceContext.isCancelled()) {
            //  Wait for next request from client
            int rc = ZMQ.poll(items, 1000);
            if (rc == -1) {
                continue;
            }

            if (items[0].isReadable()) {
                String reqStr = responder.recvStr(0);
                System.out.printf("Received request: [%s]\n", reqStr);

                ValidateRequest req = objectMapper.readValue(reqStr, ValidateRequest.class);

                ValidateResponse result = validateOperation(req.getAccount(), req.getSum());
                System.out.printf("send response request: [%s]\n", result);

                responder.send(objectMapper.writeValueAsString(result));
            }
        }
        System.out.println("Stop async read!");

        //  We never get here but clean up anyhow
        responder.close();
        context.term();

    }

    private ValidateResponse validateOperation(String account, BigDecimal sum) {
        AccountKey key = new AccountKey(account);
        Lock lock = accountCache.lock(key);
        try {
            lock.lock();
            AccountData accountData = (AccountData) accountCache.get(key);
            if (accountData == null) {
                return ValidateResponse.error("Account not found!");
            }

            //clean today operations
            if (!accountData.getToday().equals(LocalDate.now())) {
                accountData.setTodayOperationSum(new BigDecimal(0));
                accountData.setToday(LocalDate.now());
                accountCache.put(key, accountData);
            }

            BigDecimal newOperationSum = accountData.getTodayOperationSum().add(sum);
            if (newOperationSum.compareTo(accountData.getDailyLimit()) > 0) {
                return ValidateResponse.DENIED;
            } else {
                accountData.setTodayOperationSum(newOperationSum);
                accountCache.put(key, accountData);
                return ValidateResponse.OK;
            }

        } finally {
            lock.unlock();
        }
    }
}
