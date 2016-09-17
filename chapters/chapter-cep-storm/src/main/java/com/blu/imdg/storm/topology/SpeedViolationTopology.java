package com.blu.imdg.storm.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.blu.imdg.storm.bolts.SpeedLimitBolt;
import com.blu.imdg.storm.bolts.SplitSentence;
import com.blu.imdg.storm.bolts.WordCount;
import com.blu.imdg.storm.spouts.FileSourceSpout;
import com.blu.imdg.storm.spouts.RandomSentenceSpout;
import org.apache.ignite.stream.storm.StormStreamer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by shamim on 28/08/16.
 */
public class SpeedViolationTopology {

    private static final int STORM_EXECUTORS = 2;

    public static void main(String[] args) throws Exception {
        if (getProperties() == null || getProperties().isEmpty()) {
            System.out.println("Property file <ignite-storm.property> is not found or empty");
            return;
        }
        // Ignite Stream Ibolt
        final StormStreamer<String, String> stormStreamer = new StormStreamer<>();

        stormStreamer.setAutoFlushFrequency(10L);
        stormStreamer.setAllowOverwrite(true);
        stormStreamer.setCacheName(getProperties().getProperty("cache.name"));

        stormStreamer.setIgniteTupleField(getProperties().getProperty("tuple.name"));
        stormStreamer.setIgniteConfigFile(getProperties().getProperty("ignite.spring.xml"));


        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout", new FileSourceSpout(), 1);
        builder.setBolt("limit", new SpeedLimitBolt(), 1).fieldsGrouping("spout", new Fields("trafficLog"));
        // set ignite bolt
        builder.setBolt("ignite-bolt", stormStreamer, STORM_EXECUTORS).shuffleGrouping("limit");

        Config conf = new Config();
        conf.setDebug(false);

        conf.setMaxTaskParallelism(1);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("speed-violation", conf, builder.createTopology());
        Thread.sleep(10000);
        cluster.shutdown();

    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        InputStream ins = SpeedViolationTopology.class.getClassLoader().getResourceAsStream("ignite-storm.properties");

        try {
            properties.load(ins);
        } catch (IOException e) {
            e.printStackTrace();
            properties = null;
        }
        return properties;
    }
}
