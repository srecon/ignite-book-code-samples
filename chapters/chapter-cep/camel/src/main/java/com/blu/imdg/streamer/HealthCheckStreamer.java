package com.blu.imdg.streamer;

import com.blu.imdg.ExamplesUtils;
import com.blu.imdg.dto.ServiceStatus;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.configuration.FactoryBuilder;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.Date;
import java.util.Random;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by shamim on 04/08/16.
 */
public class HealthCheckStreamer {
    private static final Random RAND = new Random();
    private static final String[] SERVICE_NAME = new String[] {"GETINN", "GETWEATHER", "GETEXCHANGERATE", "CONVERTCURRENCY", "GETPUBLICSERVICELIST", "SEARCHBRANCHES", "GETNEWSBYTOPICS"};
    private static final int[] STATUS_CODE = new int[] {200, 404, 504};

    public static void main(String[] args) throws Exception{
        System.out.println("Streamer for service health check!!");
        // Mark this cluster member as client.
        Ignition.setClientMode(true);
        try(Ignite ignite = Ignition.start("example-ignite.xml")){
            if(!ExamplesUtils.hasServerNodes(ignite))
                return;
            // healthcheck cache configuration
            CacheConfiguration<String, ServiceStatus> healthcheck_cfg = new CacheConfiguration<>("healthchecks");
            // set window to 5 second
            //  Note: data will evicts after 5 seconds
            //healthcheck_cfg.setExpiryPolicyFactory(FactoryBuilder.factoryOf(new CreatedExpiryPolicy(new Duration(SECONDS, 15))));
            // set index
            healthcheck_cfg.setIndexedTypes(String.class, ServiceStatus.class);

            IgniteCache<String, ServiceStatus> healthCheckCache = ignite.getOrCreateCache(healthcheck_cfg);

            try(IgniteDataStreamer<String, ServiceStatus> healthCheckStreamer = ignite.dataStreamer(healthCheckCache.getName())){
                healthCheckStreamer.allowOverwrite(true);

                while(true ){
                    int idx_service_name = RAND.nextInt(SERVICE_NAME.length);
                    int idx_code = RAND.nextInt(STATUS_CODE.length);
                    ServiceStatus serviceStatus = new ServiceStatus(SERVICE_NAME[idx_service_name], STATUS_CODE[idx_code]);

                    healthCheckStreamer.addData(SERVICE_NAME[idx_service_name], serviceStatus);
                }

            }

        }
    }
}
