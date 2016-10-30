package com.blu.imdg.common.bankData;

import com.blu.imdg.common.bankData.model.*;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.CacheConfiguration;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.apache.ignite.cache.CacheAtomicityMode.TRANSACTIONAL;

/**
 * Created by mikl on 21.10.16.
 */
public class BankDataGenerator {

    public static final String ACCOUNT_CACHE = "accountCache";
    public static final String SAVINGS_CACHE = "savingsDictionaryCache";
    public static final String TEST_ACCOUNT = "0000*1111";

    public static IgniteCache<AccountCacheKey, AccountCacheData> createBankCache(Ignite ignite) {
        CacheConfiguration accountCacheCfg = new CacheConfiguration()
                .setName(ACCOUNT_CACHE)
                .setAtomicityMode(TRANSACTIONAL)
                .setIndexedTypes(
                        AccountKey.class, AccountData.class,
                        TransactionKey.class, TransactionData.class,
                        String.class, SavingsDictionaryData.class
                );

        IgniteCache<AccountCacheKey, AccountCacheData> result = ignite.getOrCreateCache(accountCacheCfg);
        result.removeAll();
        return initData(result);
    }

    public static IgniteCache<String, SavingsDictionaryData> initSavigsCache(Ignite ignite) {
        CacheConfiguration savingsCacheCfg = new CacheConfiguration().setName(SAVINGS_CACHE);
        IgniteCache<String, SavingsDictionaryData> result = ignite.getOrCreateCache(savingsCacheCfg);
        result.removeAll();
        result.put("meal", new SavingsDictionaryData(new BigDecimal(0.01),"meal shopping"));;
        result.put("entertainment", new SavingsDictionaryData(new BigDecimal(0.02),"entertainment"));;
        return result;
    }

    private static IgniteCache<AccountCacheKey, AccountCacheData> initData(IgniteCache<AccountCacheKey, AccountCacheData> result) {
        //init data
        result.put(new AccountKey(TEST_ACCOUNT), new AccountData(TEST_ACCOUNT, "John Doe", "standard", new Date(), new BigDecimal(100)));
        result.put(new AccountKey(TEST_ACCOUNT), new AccountData(TEST_ACCOUNT, "Mr. Smith", "premium", new Date(), new BigDecimal(200)));

        result.put(
                new TransactionKey(TEST_ACCOUNT, new Date(), UUID.randomUUID().toString()),
                new TransactionData(TEST_ACCOUNT,"1111*2222", new BigDecimal(100), "meal")
        );

        result.put(
                new TransactionKey(TEST_ACCOUNT, new Date(), UUID.randomUUID().toString()),
                new TransactionData(TEST_ACCOUNT,"3333*4444", new BigDecimal(100), "entertainment")
        );

        return result;
    }
}
