package com.blu.imdg.camel;

import com.blu.imdg.ExamplesUtils;
import com.blu.imdg.dto.MnpRouting;
import com.blu.imdg.processor.RouteProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;
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
import java.util.Map;
import java.util.Properties;

/**
 * User: bsha
 * Date: 07.08.2016
 * Time: 19:21
 */
public class CamelStreamerMediationIngestion {
    private static final String FILE_PROPERTIES= "camel.properties";

    public static void main(String[] args) throws Exception {
        System.out.println("Camel Streamer Mediation ingestion!");
        Ignition.setClientMode(true);
        Ignite ignite = Ignition.start("example-ignite.xml");
        if (!ExamplesUtils.hasServerNodes(ignite))
            return;
        if (getFileLocation() == null || getFileLocation().isEmpty()){
            System.out.println("properties file is empty or null!");
            return;
        }
        // camel_cache cache configuration
        CacheConfiguration<String, String> camel_cache_cfg = new CacheConfiguration<>("camel-direct");

        camel_cache_cfg.setIndexedTypes(String.class, String.class);

        IgniteCache<String, String> camel_cache = ignite.getOrCreateCache(camel_cache_cfg);
        // Create an streamer pipe which ingests into the 'camel_cache' cache.
        IgniteDataStreamer<String, String> pipe = ignite.dataStreamer(camel_cache.getName());
        // does the tricks
        pipe.autoFlushFrequency(1l);
        pipe.allowOverwrite(true);
        // Create a Camel streamer and connect it.
        CamelStreamer<String, String> streamer = new CamelStreamer<>();
        streamer.setIgnite(ignite);
        streamer.setStreamer(pipe);
        streamer.setEndpointUri("direct:ignite.ingest");
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file://"+getFileLocation())
                        .unmarshal().json(JsonLibrary.Jackson, MnpRouting.class)
                        .bean(new RouteProcessor(), "process")
                        .to("direct:ignite.ingest");
            }
        });

        streamer.setCamelContext(context);
        streamer.setSingleTupleExtractor(new StreamSingleTupleExtractor<Exchange, String, String>() {
            @Override
            public Map.Entry<String, String> extract(Exchange exchange) {
                String key = exchange.getIn().getHeader("key", String.class);
                String routeMsg = exchange.getIn().getBody(String.class);
                return new GridMapEntry<>(key, routeMsg);

            }
        });
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
