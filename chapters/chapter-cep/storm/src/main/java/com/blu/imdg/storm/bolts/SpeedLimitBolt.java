package com.blu.imdg.storm.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import clojure.lang.IFn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TreeMap;

/**
 * Created by shamim on 28/08/16.
 */
public class SpeedLimitBolt extends BaseBasicBolt {
    // Static field for ignite
    private static final String IGNITE_FIELD = "ignite";
    private static final int SPEED_THRESHOLD = 120;
    //Create logger for this class
    private static final Logger LOGGER = LogManager.getLogger(SpeedLimitBolt.class);
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        String line = (String)tuple.getValue(0);
        if(!line.isEmpty()){
            String[] elements = line.split(",");
            // we are interested in speed and the car registration number
            int speed = Integer.valueOf((elements[1]).trim());
            String car = elements[0];
            if(speed > SPEED_THRESHOLD){
                TreeMap<String, Integer> carValue = new TreeMap<String, Integer>();
                carValue.put(car, speed);
                basicOutputCollector.emit(new Values(carValue));
                LOGGER.info("Speed violation found:"+ car + " speed:" + speed);
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields(IGNITE_FIELD));
    }
}
