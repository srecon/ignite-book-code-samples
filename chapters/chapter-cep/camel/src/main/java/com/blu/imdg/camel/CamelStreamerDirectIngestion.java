package com.blu.imdg.camel;

import com.blu.imdg.ExamplesUtils;
import com.blu.imdg.dto.MnpRouting;
import com.blu.imdg.dto.ServiceStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.internal.util.lang.GridMapEntry;
import org.apache.ignite.stream.StreamSingleTupleExtractor;
import org.apache.ignite.stream.camel.CamelStreamer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * User: bsha
 * Date: 07.08.2016
 * Time: 12:28
 */
public class CamelStreamerDirectIngestion {
    private static final String FILE_PROPERTIES= "camel.properties";
    public static void main(String[] args) {
        System.out.println("Camel Streamer Direct ingestion!");
        Ignition.setClientMode(true);
        Ignite ignite = Ignition.start("example-ignite.xml");
        if (!ExamplesUtils.hasServerNodes(ignite))
            return;
        if(getFileLocation() == null || getFileLocation().isEmpty()){
            System.out.println("Properties file is empty or null!");
            return;
        }
        // camel_cache cache configuration
        CacheConfiguration<String, MnpRouting> camel_cache_cfg = new CacheConfiguration<>("camel-direct");

        camel_cache_cfg.setIndexedTypes(String.class, MnpRouting.class);

        IgniteCache<String, MnpRouting> camel_cache = ignite.getOrCreateCache(camel_cache_cfg);
        // Create an streamer pipe which ingests into the 'camel_cache' cache.
        IgniteDataStreamer<String, MnpRouting> pipe = ignite.dataStreamer(camel_cache.getName());
        // does the tricks
        pipe.autoFlushFrequency(1l);
        pipe.allowOverwrite(true);
        // Create a Camel streamer and connect it.
        CamelStreamer<String, MnpRouting> streamer = new CamelStreamer<>();
        streamer.setIgnite(ignite);
        streamer.setStreamer(pipe);

        streamer.setEndpointUri("file://"+getFileLocation());
        streamer.setSingleTupleExtractor(new StreamSingleTupleExtractor<Exchange, String, MnpRouting>() {
            @Override
            public Map.Entry<String, MnpRouting> extract(Exchange exchange) {
                // (1) convert the JSON to java object
                // (2) add the key value as telephone
                // (3) store the java object into Cache
                ObjectMapper mapper = new ObjectMapper();
                String msgBody = exchange.getIn().getBody(String.class);
                System.out.println("MsgBody:" + msgBody);
                MnpRouting obj = null;
                try {
                    obj = mapper.readValue(msgBody, MnpRouting.class);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (obj != null){
                    return new GridMapEntry<String, MnpRouting>(obj.getTelephone(), obj);

                }
                return new GridMapEntry<String, MnpRouting>(null, null);
            }
        });

        // Start the streamer.
        streamer.start();
    }
    private static String getFileLocation(){
        Properties properties = new Properties();
        InputStream in;
        try{
            in = CamelStreamerDirectIngestion.class.getClassLoader().getResourceAsStream(FILE_PROPERTIES);
            if(in == null){
               return null;
            }
            properties.load(in);
            return properties.getProperty("input.file.location");

        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
