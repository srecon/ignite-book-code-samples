package com.blu.imdg;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteTransactions;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.transactions.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.ignite.transactions.TransactionConcurrency.PESSIMISTIC;
import static org.apache.ignite.transactions.TransactionIsolation.REPEATABLE_READ;

/**
 * Created by isatimur on 8/19/16.
 *
 * Here we are going to see a <code>transactional</code> mode which allows us using fully <a
 * href="https://en.wikipedia.org/wiki/ACID">ACID</a> operations on the cache data.
 */
public class SimpleTransactions {

    private static Logger logger = LoggerFactory.getLogger(SimpleTransactions.class.getSimpleName());

    /** Cache name. */
    private static final String CACHE_NAME = SimpleTransactions.class.getSimpleName();

    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start("example-ignite.xml");) {
            IgniteTransactions transactions = ignite.transactions();

            logger.info("|Transaction example started.|");

            CacheConfiguration<Integer, BankAccount> cfg = new CacheConfiguration<>(CACHE_NAME);

            cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

            BankAccount ba1 = new BankAccount(100);
            BankAccount ba2 = new BankAccount(900);
            BankAccount ba3 = new BankAccount(400);

            // Auto-close cache at the end of the example.
            try (IgniteCache<Integer, BankAccount> cache = ignite.getOrCreateCache(cfg)) {
                // Initialize.

                cache.put(1, ba1);
                cache.put(2, ba2);
                cache.put(3, ba3);

                logger.info("|--Bank accounts initialized: --|");
                logger.info("|--Account #1: " + cache.get(1) + "--|");
                logger.info("|--Account #2: " + cache.get(2) + "--|");
                logger.info("|--Account #3: " + cache.get(3) + "--|");

                // Make transactional operatons with bank accounts.
                deposit(cache, 1, 100);
                deposit(cache, 2, 200);

                logger.info("|--Bank accounts after deposit: --|");
                logger.info("|--Account #1: " + cache.get(1) + "--|");
                logger.info("|--Account #2: " + cache.get(2) + "--|");

                withdraw(cache, 3, 10);
                withdraw(cache, 2, 500);
                withdraw(cache, 1, 100);

                logger.info("|--Bank accounts after withdraw: --|");
                logger.info("|--Account #3: " + cache.get(3) + "--|");
                logger.info("|--Account #2: " + cache.get(2) + "--|");
                logger.info("|--Account #1: " + cache.get(1) + "--|");

                logger.info("|--Transaction example finished.--|");
            }
            finally {
                // Distributed cache could be removed from cluster only by #destroyCache() call.
                ignite.destroyCache(CACHE_NAME);
            }
        }
    }

    /**
     * Make deposit into specified account number.
     *
     * @param accountNumber Bank account number.
     * @param amount Amount to deposit.+
     * @throws IgniteException If failed.
     */
    private static void deposit(IgniteCache<Integer, BankAccount> cache, int accountNumber,
        double amount) throws IgniteException {
        try (Transaction tx = Ignition.ignite().transactions().txStart(PESSIMISTIC, REPEATABLE_READ)) {
            BankAccount bankAccount = cache.get(accountNumber);

            // Deposit into account.
            bankAccount.deposit(amount);

            // Store updated account in cache.
            cache.put(accountNumber, bankAccount);

            tx.commit();
        }

        logger.info("");
        logger.info("|--Deposit amount: $" + amount + "--|");
    }

    /**
     * Make withdraw from specified account number.
     *
     * @param accountNumber Bank Account number.
     * @param amount Amount to withdraw.-
     * @throws IgniteException If failed.
     */
    private static void withdraw(IgniteCache<Integer, BankAccount> cache, int accountNumber,
        double amount) throws IgniteException {
        try (Transaction tx = Ignition.ignite().transactions().txStart(PESSIMISTIC, REPEATABLE_READ)) {
            BankAccount bankAccount = cache.get(accountNumber);

            // Deposit into account.
            bankAccount.withdraw(amount);

            // Store updated account in cache.
            cache.put(accountNumber, bankAccount);

            tx.commit();
        }

        logger.info("");
        logger.info("|--Withdraw amount: $" + amount + "--|");
    }

    /**
     * Bank Account. is a serialized entity of our bank account within operations like deposit and withdraw.
     */
    private static class BankAccount implements Serializable {
        private static final AtomicInteger id_serial = new AtomicInteger(0);

        //Bank Account number.
        private int accountNumber;

        //Bank Account balance.
        private double accountBalance;

        public BankAccount() {
            this.accountNumber = id_serial.incrementAndGet();
            this.accountBalance = 0;
        }

        public BankAccount(double accountBalance) {
            this.accountNumber = id_serial.incrementAndGet();
            this.accountBalance = accountBalance;
        }

        public BankAccount(int accountNumber, double accountBalance) {
            this.accountNumber = accountNumber;
            this.accountBalance = accountBalance;
        }

        public boolean withdraw(double amount) {
            double newAccountBalance;
            if (amount > accountBalance) {
                return false;
            }
            else {
                newAccountBalance = accountBalance - amount;
                accountBalance = newAccountBalance;
                return true;
            }
        }

        public boolean deposit(double amount) {
            double newAccountBalance;
            if (amount < 0.0) {
                return false;
            }
            else {
                newAccountBalance = accountBalance + amount;
                accountBalance = newAccountBalance;
                return true;
            }
        }

        @Override public String toString() {
            return "BankAccount{" +
                "accountNumber=" + accountNumber +
                ", accountBalance=" + accountBalance +
                '}';
        }
    }
}