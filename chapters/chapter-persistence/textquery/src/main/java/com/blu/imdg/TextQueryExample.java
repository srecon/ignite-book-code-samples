package com.blu.imdg;

import com.blu.imdg.model.Company;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.TextQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.internal.processors.cache.CacheEntryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Created by isatimur on 8/9/16.
 */
public class TextQueryExample {

    private static Logger logger = LoggerFactory.getLogger(TextQueryExample.class);

    /*
      Partitioned cache name to store companies.
      */
    private static final String COMPANY_CACHE_NAME = TextQueryExample.class.getSimpleName() + "-company";

    /**
     * This is an entry point of TextQueryExample, the ignite configuration lies upon resources directory as
     * example-ignite.xml.
     *
     * @param args Command line arguments, none required.
     */
    public static void main(String[] args) throws Exception {

        //The ignite configuration lies below resources directory as example-ignite.xml.
        try (Ignite ignite = Ignition.start("example-ignite.xml")) {

            logger.info("Text. Sql query example.");

            CacheConfiguration<Long, Company> employeeCacheCfg = new CacheConfiguration<>(COMPANY_CACHE_NAME);

            employeeCacheCfg.setCacheMode(CacheMode.PARTITIONED);
            employeeCacheCfg.setIndexedTypes(Long.class, Company.class);

            try (
                    IgniteCache<Long, Company> employeeCache = ignite.createCache(employeeCacheCfg)
            ) {
                // Populate cache.
                initialize();

                // Full text query example.
                //textQuery();
                scanQuery();

                log("Text query example finished.");
            }
        }
    }

    /**
     * Let's fill ignite cache with test data. Data are taken from oracle's study test scheme EMP. The structure of
     * those scheme you can see below resources folder of this module.
     *
     * @throws InterruptedException In case of error.
     */
    private static void initialize() throws InterruptedException, IOException {
        IgniteCache<Long, Company> companyCache = Ignition.ignite().cache(COMPANY_CACHE_NAME);

        // Clear caches before start.
        companyCache.clear();

        // Companies
        try (
                Stream<String> lines = Files.lines(Paths.get(TextQueryExample.class.getClassLoader().getResource("USA_NY_email_addresses.csv").toURI()));

        ) {
            lines
                    .skip(1)
                    .map(s1 -> s1.split("\",\""))
                    .map(s2 -> new Company(Long.valueOf(s2[0].replaceAll("\"", "")), s2[1], s2[2], s2[3], s2[4], s2[5], s2[6], s2[7], s2[8], s2[9], s2[10], s2[11], s2[12].replaceAll("\"", "")))
                    .forEach(r -> companyCache.put(r.getId(), r));

        } catch (URISyntaxException | IOException e) {
            log(e.getMessage());

        }

        // Wait 1 second to be sure that all nodes processed put requests.
        Thread.sleep(1000);
    }


    /**
     * Example for TEXT queries using LUCENE-based indexing of people's job name.
     */
    private static void textQuery() {
        IgniteCache<Integer, Company> cache = Ignition.ignite().cache(COMPANY_CACHE_NAME);

        //  Query for all companies which has a text "John".
        TextQuery<Integer, Company> john = new TextQuery<>(Company.class, "John");

        //  Query for all companies which has a text "primavera".
        TextQuery<Integer, Company> primavera = new TextQuery<>(Company.class, "Primavera");

        log("==So many companies with information about 'John'==", cache.query(john).getAll());
        log("==A company which name is starting as 'Primavera'==", cache.query(primavera).getAll());
    }
    private static void scanQuery() {
        IgniteCache<Long, Company> companyCache = Ignition.ignite().cache(COMPANY_CACHE_NAME);

        //  Query for all companies which the city 'NEW YORK' - NewYork.
        QueryCursor cursor = companyCache.query(new ScanQuery<Long, Company>((k, p) -> p.getCity().equalsIgnoreCase("NEW YORK")));

        for(Iterator ite = cursor.iterator(); ite.hasNext();)
        {
            CacheEntryImpl company = (CacheEntryImpl) ite.next();

            log(((Company)company.getValue()).getCompanyName());
        }

        cursor.close();

    }

    /**
     * Prints message to logger.
     *
     * @param msg String.
     */
    private static void log(String msg) {
        logger.info("\t" + msg);
    }

    /**
     * Prints message to logger.
     *
     * @param msg String.
     */
    private static void log(String msg, Iterable<?> col) {
        logger.info("\t" + msg);
        col.forEach(c -> logger.info("\t\t" + c));
    }

    /**
     * Prints message and resultset to logger.
     *
     * @param msg String.
     * @param col Iterable
     */
    private static void logDecorated(String msg, Iterable<?> col) {
        logger.info("\t" + msg);
        col.forEach(c -> logger.info("\t\t" + c));
    }

}
