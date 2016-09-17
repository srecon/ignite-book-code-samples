package com.blu.imdg.storm.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.text.BreakIterator;

/**
 * Created by shamim on 24/08/16.
 */
public class SplitSentence extends BaseBasicBolt {
    //Execute is called to process tuples
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        //Get the sentence content from the tuple
        String sentence = tuple.getString(0);
        //An iterator to get each word
        BreakIterator boundary=BreakIterator.getWordInstance();
        //Give the iterator the sentence
        boundary.setText(sentence);
        //Find the beginning first word
        int start=boundary.first();
        //Iterate over each word and emit it to the output stream
        for (int end = boundary.next(); end != BreakIterator.DONE; start=end, end=boundary.next()) {
            //get the word
            String word=sentence.substring(start,end);
            //If a word is whitespace characters, replace it with empty
            word=word.replaceAll("\\s+","");
            //if it's an actual word, emit it
            if (!word.equals("")) {
                collector.emit(new Values(word));
            }
        }
    }

    //Declare that emitted tuples will contain a word field
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }
}
