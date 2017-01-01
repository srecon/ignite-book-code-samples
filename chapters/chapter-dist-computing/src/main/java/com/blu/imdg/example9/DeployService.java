package com.blu.imdg.example9;

/**
 * Created by shamim on 01/01/17.
 */
import com.blu.imdg.common.CommonConstants;
import com.blu.imdg.common.bankData.BankDataGenerator;
import com.blu.imdg.common.bankData.model.AccountCacheData;
import com.blu.imdg.common.bankData.model.AccountCacheKey;
import com.blu.imdg.example9.exception.AccountNotFoundException;
import com.blu.imdg.example9.exception.LogServiceException;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;

public class DeployService {
    public static void main(String[] args) throws AccountNotFoundException, LogServiceException {

        try (Ignite ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {

            IgniteCache<AccountCacheKey, AccountCacheData> cache = BankDataGenerator.createBankCache(ignite);

            IgniteServices services = ignite.services().withAsync();

            services.deployNodeSingleton(LogService.NAME, new LogServiceImpl());
            services.future().get();

            services.deployNodeSingleton(BankService.NAME, new BankServiceImpl());
            services.future().get();
        }
    }
}
