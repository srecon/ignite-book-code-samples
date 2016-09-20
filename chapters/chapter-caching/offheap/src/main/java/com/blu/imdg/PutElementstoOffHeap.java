package com.blu.imdg;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: bsha
 * Date: 03.07.2016
 * Time: 17:58
 */
public class PutElementstoOffHeap {
    public static void main(String[] args) throws Exception{
        if(args.length <= 0){
            System.out.println("Usages! java -jar .\\target\\chapter-three-offheap-1.0-SNAPSHOT.one-jar.jar spring-offheap-tiered.xml");
            System.exit(0);
        }
        String springCoreFile = args[0];
        // Start Ignite cluster
        Ignite ignite = Ignition.start(springCoreFile);
        // get or create cache
        IgniteCache<Integer, String> cache =  ignite.getOrCreateCache("offheap-cache");
        for(int i = 1; i <= 1000; i++){
            cache.put(i, Integer.toString(i));
        }
        for(int i =1; i<=1000;i++){
            System.out.println("Cache get:"+ cache.get(i));
        }
        System.out.println("Wait 10 seconds for statistics");
        Thread.sleep(10000);
        // statistics
        System.out.println("Cache Hits:"+ cache.metrics(ignite.cluster()).getCacheHits());

        System.out.println("Enter crtl-x to quite the application!!!");
        Thread.sleep(Integer.MAX_VALUE); // sleep for 20 seconds

        ignite.close();
    }
}
