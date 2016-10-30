package com.blu.imdg.storm.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.blu.imdg.storm.bolts.SplitSentence;
import com.blu.imdg.storm.bolts.WordCount;
import com.blu.imdg.storm.spouts.RandomSentenceSpout;
import org.apache.ignite.stream.storm.StormStreamer;

/**
 * Created by shamim on 24/08/16.
 */
public class WordCountTopology {
    /** Parallelization in Storm. */
    private static final int STORM_EXECUTORS = 2;
    //Entry point for the topology
    public static void main(String[] args) throws Exception {
        // Ignite Stream Ibolt
        final StormStreamer<String, String> stormStreamer = new StormStreamer<>();

        stormStreamer.setAutoFlushFrequency(10L);
        stormStreamer.setAllowOverwrite(true);
        stormStreamer.setCacheName("testCache");
        stormStreamer.setIgniteTupleField("ignite");
        stormStreamer.setIgniteConfigFile("/Users/shamim/Development/workshop/assembla/ignite-book/chapters/chapter-cep-storm/src/main/resources/example-ignite.xml");

        //Used to build the topology
        TopologyBuilder builder = new TopologyBuilder();
        //Add the spout, with a name of 'spout'
        //and parallelism hint of 5 executors
        builder.setSpout("spout", new RandomSentenceSpout(), 5);
        //Add the SplitSentence bolt, with a name of 'split'
        //and parallelism hint of 8 executors
        //shufflegrouping subscribes to the spout, and equally distributes
        //tuples (sentences) across instances of the SplitSentence bolt
        builder.setBolt("split", new SplitSentence(), 8).shuffleGrouping("spout");
        //Add the counter, with a name of 'count'
        //and parallelism hint of 12 executors
        //fieldsgrouping subscribes to the split bolt, and
        //ensures that the same word is sent to the same instance (group by field 'word')
        builder.setBolt("count", new WordCount(), 12).fieldsGrouping("split", new Fields("word"));
        // set ignite bolt
        builder.setBolt("ignite-bolt", stormStreamer,STORM_EXECUTORS).shuffleGrouping("count");

        //new configuration
        Config conf = new Config();
        //Set to false to disable debug information
        // when running in production mode.
        conf.setDebug(false);

        //If there are arguments, we are running on a cluster
        if (args != null && args.length > 0) {
            //parallelism hint to set the number of workers
            conf.setNumWorkers(3);
            //submit the topology
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        }
        //Otherwise, we are running locally
        else {
            //Cap the maximum number of executors that can be spawned
            //for a component to 3
            conf.setMaxTaskParallelism(3);
            //LocalCluster is used to run locally
            LocalCluster cluster = new LocalCluster();
            //submit the topology
            cluster.submitTopology("word-count", conf, builder.createTopology());
            //sleep
            Thread.sleep(10000);
            //shut down the cluster
            cluster.shutdown();
        }
    }
}
