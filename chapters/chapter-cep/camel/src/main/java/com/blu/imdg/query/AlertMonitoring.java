package com.blu.imdg.query;

import com.blu.imdg.ExamplesUtils;
import com.blu.imdg.dto.Alert;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import java.util.List;

/**
 * Created by shamim on 05/08/16.
 */
public class AlertMonitoring {

    private static final String QUERY_RED = "select alertLevel, count(alertLevel) from Alert group by alertLevel";
    public static void main(String[] args) throws Exception {
        // Mark this cluster member as client.
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start("example-ignite.xml")) {
            if (!ExamplesUtils.hasServerNodes(ignite))
                return;

            CacheConfiguration<String, Alert> alert_Cfg = new CacheConfiguration<>("alerts");
            IgniteCache<String, Alert> instCache = ignite.getOrCreateCache(alert_Cfg);

            SqlFieldsQuery top3qry = new SqlFieldsQuery(QUERY_RED);
            while(true){
                // Execute queries.
                List<List<?>> top3 = instCache.query(top3qry).getAll();

                System.out.println("Service Health Monitoring");

                ExamplesUtils.printQueryResults(top3);

                Thread.sleep(1000);
            }

        }
    }
}
