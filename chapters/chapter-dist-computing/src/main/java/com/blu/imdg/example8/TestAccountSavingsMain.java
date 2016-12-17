package com.blu.imdg.example8;

import com.blu.imdg.common.CommonConstants;
import com.blu.imdg.common.bankData.BankDataGenerator;
import com.blu.imdg.common.bankData.model.*;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by mikl on 21.10.16.
 */

//mvn exec:java -Dexec.mainClass=com.blu.imdg.example8.TestAccountSavingsMain
public class TestAccountSavingsMain {

    public static void main(String[] args) {

        try (Ignite ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {
            IgniteCompute compute = ignite.compute();

            IgniteCache<AccountCacheKey, AccountCacheData> cache = BankDataGenerator.createBankCache(ignite);
            IgniteCache<String, CashBackDictionaryData> savingsCache = BankDataGenerator.initSavigsCache(ignite);

            SqlFieldsQuery sql = new SqlFieldsQuery("select * from TransactionData where account = ?");

            BigDecimal result = compute.affinityCall(BankDataGenerator.ACCOUNT_CACHE, new AccountKey(BankDataGenerator.TEST_ACCOUNT), () -> {
                //download all transactions for this account
                List<List<?>> data = cache.query(sql.setArgs(BankDataGenerator.TEST_ACCOUNT)).getAll();
                BigDecimal cashBack = new BigDecimal(0);
                for (List row : data) {
                    TransactionKey key = (TransactionKey) row.get(0);
                    TransactionData tr = (TransactionData) row.get(1);

                    CashBackDictionaryData cashBackDictionaryData = savingsCache.get(tr.getTransactionType());
                    cashBack = cashBack.add(tr.getSum().multiply(cashBackDictionaryData.getCashBackPercent()));
                }
                //System.out.println("savings="+cashBack);
                return cashBack;
            });

            System.out.println("result="+result);

        }
    }

}
