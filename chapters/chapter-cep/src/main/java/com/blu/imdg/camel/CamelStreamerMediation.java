package com.blu.imdg.camel;

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

import java.util.Map;

/**
 * User: bsha
 * Date: 07.08.2016
 * Time: 13:42
 */
public class CamelStreamerMediation {
    public static void main(String[] args) throws Exception{
        System.out.println("Camel Streamer Mediation ingestion!");
        Ignition.setClientMode(true);
        Ignite ignite = Ignition.start("example-ignite.xml");
        // Create a CamelContext with a custom route that will:
        //  (1) consume from our Jetty endpoint.
        //  (2) transform incoming JSON into a Java object with Jackson.
        //  (3) uses JSR 303 Bean Validation to validate the object.
        //  (4) dispatches to the direct:ignite.ingest endpoint, where the streamer is consuming from.
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
                from("jetty:http://127.0.0.1:8081/ignite?httpMethodRestrict=POST")
                        .unmarshal().json(JsonLibrary.Jackson)
                        .to("bean-validator:validate")
                        .to("direct:ignite.ingest");
            }
        });

        // Remember our Streamer is now consuming from the Direct endpoint above.
        streamer.setCamelContext(context);
        streamer.setSingleTupleExtractor(new StreamSingleTupleExtractor<Exchange, String, String>() {
            @Override
            public Map.Entry<String, String> extract(Exchange exchange) {
                String stationId = exchange.getIn().getHeader("X-StationId", String.class);
                String temperature = exchange.getIn().getBody(String.class);
                System.out.println("StationId:" + stationId + " temperature:" + temperature);
                return new GridMapEntry<>(stationId, temperature);
            }
        });
        streamer.start();
        //streamer.start();
    }
}
