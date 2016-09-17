package com.blu.imdg.query;

import com.blu.imdg.ExamplesUtils;
import com.blu.imdg.dto.ServiceStatus;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import java.util.List;

/**
 * Created by shamim on 04/08/16.
 */
public class QueryStatus {
    private static final String QUERY_404="SELECT serviceName, statusCode from ServiceStatus where statusCode = 404";
    private static final String QUERY_CNT= "select count(*) from ServiceStatus where statusCode = 404";
    public static void main(String[] args) throws Exception {
        // Mark this cluster member as client.
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start("example-ignite.xml")) {
            if (!ExamplesUtils.hasServerNodes(ignite))
                return;
            // query code goes here.
            CacheConfiguration<String, ServiceStatus> healthchecksCfg = new CacheConfiguration<>("healthchecks");
            IgniteCache<String, ServiceStatus> instCache = ignite.getOrCreateCache(healthchecksCfg);
            SqlFieldsQuery query = new SqlFieldsQuery(QUERY_404);

            while(true){
                // Execute queries.
                List<List<?>> res = instCache.query(query).getAll();

                System.out.println("Service Health check status");

                ExamplesUtils.printQueryResults(res);

                Thread.sleep(1000);
            }

        }
    }
}
