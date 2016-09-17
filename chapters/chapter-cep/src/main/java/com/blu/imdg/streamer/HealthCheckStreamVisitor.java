package com.blu.imdg.streamer;

import com.blu.imdg.ExamplesUtils;
import com.blu.imdg.dto.Alert;
import com.blu.imdg.dto.Level;
import com.blu.imdg.dto.ServiceStatus;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.stream.StreamVisitor;

import java.util.Random;

/**
 * Created by shamim on 05/08/16.
 */
public class HealthCheckStreamVisitor {
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
            CacheConfiguration<String, ServiceStatus> healthcheck_cfg = new CacheConfiguration<String, ServiceStatus>("healthchecks");
            CacheConfiguration<String, Alert> alert_cfg = new CacheConfiguration<String, Alert>("alerts");

            alert_cfg.setIndexedTypes(String.class, Alert.class);
            // create cache
            IgniteCache<String, ServiceStatus> healthCheckCache = ignite.getOrCreateCache(healthcheck_cfg);
            IgniteCache<String, Alert> alertCache = ignite.getOrCreateCache(alert_cfg);

            // stream receiver (Visitor)

            try(IgniteDataStreamer<String, ServiceStatus> healthCheckStreamer = ignite.dataStreamer(healthCheckCache.getName())){
                healthCheckStreamer.allowOverwrite(true);
                healthCheckStreamer.receiver(StreamVisitor.from((cache, e) ->{
                    String serviceName = e.getKey();
                    ServiceStatus serviceStatus = e.getValue();
                    // get Alert by key
                    Alert alert =  alertCache.get(serviceName);
                    if (alert == null)
                        alert = new Alert(serviceName, Level.GREEN);

                    if (serviceStatus != null && serviceStatus.getStatusCode() == 404){
                        alert.setAlertLevel(Level.RED);
                    } else if (serviceStatus != null && serviceStatus.getStatusCode() == 200){
                        alert.setAlertLevel(Level.GREEN);
                    } else {
                        alert.setAlertLevel(Level.YELLOW);
                    }

                    alertCache.put(serviceName, alert);

                }));

                int cnt = 0;

                while(true){
                    cnt++;
                    int idx_service_name = RAND.nextInt(SERVICE_NAME.length);
                    int idx_code = RAND.nextInt(STATUS_CODE.length);
                    ServiceStatus serviceStatus = new ServiceStatus(SERVICE_NAME[idx_service_name], STATUS_CODE[idx_code]);

                    healthCheckStreamer.addData(SERVICE_NAME[idx_service_name], serviceStatus);
                    System.out.println("Count:" + cnt);
                }

            }

        }
    }
}
