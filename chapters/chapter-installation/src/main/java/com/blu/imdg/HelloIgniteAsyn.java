package com.blu.imdg;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteFuture;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import java.util.Arrays;

/**
 * Created by shamim on 23/10/16.
 */
public class HelloIgniteAsyn {
    public static void main(String[] args) {
        System.out.println("Hello Ignite Asynchronous!!");
        // create a new instance of TCP Discovery SPI
        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        // create a new instance of tcp discovery multicast ip finder
        TcpDiscoveryMulticastIpFinder tcMp = new TcpDiscoveryMulticastIpFinder();
        tcMp.setAddresses(Arrays.asList("localhost")); // change your IP address here
        // set the multi cast ip finder for spi
        spi.setIpFinder(tcMp);
        // create new ignite configuration
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(false);
        // set the discovery spi to ignite configuration
        cfg.setDiscoverySpi(spi);
        // Start ignite
        Ignite ignite = Ignition.start(cfg);

        // get or create cache
        IgniteCache<Integer, String> cache = ignite.getOrCreateCache("testCache");
        // get an asynchronous cache
        IgniteCache<Integer, String> asynCache = cache.withAsync();

        // put some cache elements
        for(int i = 1; i <= 100; i++){
            cache.put(i, Integer.toString(i));
        }

        String val = asynCache.withAsync().get(1);
        System.out.println("Non future call:" + val);
        IgniteFuture<String> igniteFuture = asynCache.future();



        igniteFuture.listen(f-> System.out.println("Cache Value:" + f.get()));
        ignite.close();

    }
}
