package com.blu.imdg;

import com.blu.imdg.jdbc.PostgresDBStore;
import com.blu.imdg.jdbc.model.Post;
import com.blu.imdg.nosql.MongoDBStore;
import com.blu.imdg.nosql.model.MongoPost;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.cache.configuration.FactoryBuilder;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.transactions.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.ignite.transactions.TransactionConcurrency.PESSIMISTIC;
import static org.apache.ignite.transactions.TransactionIsolation.REPEATABLE_READ;

/**
 * Created by isatimur on 8/9/16.
 */
public class CacheStoreSample {

    /*
      Cache name to store posts.
      */
    private static final String POST_CACHE_NAME = CacheStoreSample.class.getSimpleName() + "-post";
    private static Logger LOGGER = LoggerFactory.getLogger(CacheStoreSample.class);
    private static final String POSTGRESQL = "postgresql";
    private static final String MONGODB = "mongodb";

    /**
     * This is an entry point of CacheStoreSample, the ignite configuration lies upon resources directory as
     * example-ignite.xml.
     *
     * @param args Command line arguments, none required.
     */
    public static void main(String[] args) throws Exception
    {
        if(args.length <= 0 ){
            LOGGER.error("Usages! java -jar .\\target\\cache-store-runnable.jar postgresql|mongodb");
            System.exit(0);
        }
        if(args[0].equalsIgnoreCase(POSTGRESQL)){
            jdbcStoreExample();
        } else if (args[0].equalsIgnoreCase(MONGODB)){
            nosqlStore();
        }

    }

    private static void jdbcStoreExample() {
        //let's make a dynamic cache on the fly which is distributed across all running nodes.
        //the same configuration you would probably set in configuration xml format
        IgniteConfiguration cfg = new IgniteConfiguration();

        CacheConfiguration configuration = new CacheConfiguration();
        configuration.setName("dynamicCache");
        configuration.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

        configuration.setCacheStoreFactory(FactoryBuilder.factoryOf(PostgresDBStore.class));
        configuration.setReadThrough(true);
        configuration.setWriteThrough(true);

        configuration.setWriteBehindEnabled(true);

        log("Start. PersistenceStore example.");
        cfg.setCacheConfiguration(configuration);

        try (Ignite ignite = Ignition.start(cfg)) {
            //create cache if it doesn't exist
            int count = 10;
            try (IgniteCache<String, Post> igniteCache = ignite.getOrCreateCache(configuration)) {
                try (Transaction tx = ignite.transactions().txStart(PESSIMISTIC, REPEATABLE_READ)) {
                    //let us clear

                    for (int i = 1; i <= count; i++)
                        igniteCache.put("_" + i, new Post("_" + i, "title-" + i, "description-" + i, LocalDate.now().plus(i, ChronoUnit.DAYS), "author-" + i));

                    tx.commit();

                    for (int i = 1; i < count; i += 2) {
                        igniteCache.clear("_" + i);
                        log("Clear every odd key: " + i);
                    }

                    for (long i = 1; i <= count; i++)
                        log("Local peek at [key=_" + i + ", val=" + igniteCache.localPeek("_" + i) + ']');

                    for (long i = 1; i <= count; i++)
                        log("Got [key=_" + i + ", val=" + igniteCache.get("_" + i) + ']');

                    tx.commit();
                }
            }

            log("PersistenceStore example finished.");
            ignite.destroyCache("dynamicCache");
        }
    }

    private static void nosqlStore() {
        //let's make a dynamic cache on the fly which is distributed across all running nodes.
        //the same configuration you would probably set in configuration xml format
        IgniteConfiguration cfg = new IgniteConfiguration();

        CacheConfiguration configuration = new CacheConfiguration();
        configuration.setName("mongoDynamicCache");
        configuration.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

        configuration.setCacheStoreFactory(FactoryBuilder.factoryOf(MongoDBStore.class));
        configuration.setReadThrough(true);
        configuration.setWriteThrough(true);

        configuration.setWriteBehindEnabled(true);

        log("Start. PersistenceStore example.");
        cfg.setCacheConfiguration(configuration);

        try (Ignite ignite = Ignition.start(cfg)) {
            //create cache if it doesn't exist
            int count = 10;
            try (IgniteCache<String, MongoPost> igniteCache = ignite.getOrCreateCache(configuration)) {
                try (Transaction tx = ignite.transactions().txStart(PESSIMISTIC, REPEATABLE_READ)) {
                    //let us clear

                    for (int i = 1; i <= count; i++)
                        igniteCache.put("_" + i, new MongoPost("_" + i, "title-" + i, "description-" + i, LocalDate.now().plus(i, ChronoUnit.DAYS), "author-" + i));

                    for (int i = 1; i < count; i += 2) {
                        igniteCache.clear("_" + i);
                        log("Clear every odd key: " + i);
                    }

                    for (long i = 1; i <= count; i++)
                        log("Local peek at [key=_" + i + ", val=" + igniteCache.localPeek("_" + i) + ']');

                    for (long i = 1; i <= count; i++)
                        log("Got [key=_" + i + ", val=" + igniteCache.get("_" + i) + ']');

                    tx.commit();
                }
            }

            log("PersistenceStore example finished.");
            ignite.destroyCache("mongoDynamicCache");
        }
    }

    /**
     * Prints message to logger.
     *
     * @param msg String.
     */
    private static void log(String msg) {
        LOGGER.info("\t" + msg);
    }

}
