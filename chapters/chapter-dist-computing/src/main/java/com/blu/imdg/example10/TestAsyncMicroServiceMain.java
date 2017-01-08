package com.blu.imdg.example10;

import com.blu.imdg.common.CommonConstants;
import com.blu.imdg.common.bankData.BankDataGenerator;
import com.blu.imdg.common.bankData.model.AccountCacheData;
import com.blu.imdg.common.bankData.model.AccountCacheKey;
import com.blu.imdg.example10.dto.ValidateRequest;
import com.blu.imdg.example10.dto.ValidateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by mikl on 29.10.16.
 */
//mvn clean package exec:java -Dexec.mainClass=com.blu.imdg.example10.TestAsyncMicroServiceMain
public class TestAsyncMicroServiceMain {

    public static void main(String[] args) throws  JsonProcessingException, InterruptedException {
        try (Ignite ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {

            IgniteCache<AccountCacheKey, AccountCacheData> cache = BankDataGenerator.createBankCache(ignite);

            IgniteServices services = ignite.services().withAsync();

            services.deployNodeSingleton(AsyncBankService.NAME, new AsyncBankServiceImpl("tcp://localhost:5560"));
            services.future().get();

            AsyncBankService bankService = services.serviceProxy(AsyncBankService.NAME, AsyncBankService.class, /*not-sticky*/false);

            sendAsync(50, BankDataGenerator.TEST_ACCOUNT);
            sendAsync(40, BankDataGenerator.TEST_ACCOUNT);
            sendAsync(180, BankDataGenerator.TEST_ACCOUNT);
            sendAsync(180, BankDataGenerator.TEST_UNKNOWN_ACCOUNT);

            services.cancel(AsyncBankService.NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendAsync(int val, String account) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Context context = ZMQ.context(1);

        //  Socket to talk to server
        Socket requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://localhost:5559");
        System.out.println("launch and connect client.");
        ValidateRequest req = new ValidateRequest(account, new BigDecimal(val));

        //send request
        requester.send(objectMapper.writeValueAsString(req), 0);
        //receive response
        String responseStr = requester.recvStr(0);

        //parse and print reply
        ValidateResponse reply = objectMapper.readValue(responseStr, ValidateResponse.class);
        System.out.println("Received reply for request= " + req + " reply= " + reply + "");

        //  We never get here but clean up anyhow
        requester.close();
        context.term();
    }
}
