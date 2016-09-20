package com.blu.imdg.flume.Transformer;

import org.apache.flume.Event;
import org.apache.ignite.stream.flume.EventTransformer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: bsha
 * Date: 10.08.2016
 * Time: 23:24
 */
public class FlumeEventTransformer implements EventTransformer <Event, String, Integer> {
    @Nullable
    @Override
    public Map<String, Integer> transform(List<Event> list) {
        final Map<String, Integer> map = new HashMap<>(list.size());
        for(Event event : list){
            map.putAll(transform(event));
        }
        return map;
    }
    /**
     * Format event - String:String
     * example - transactionId:amount [56102:232], where transactionId is the key and amount is the value
     * */
    private Map<String, Integer> transform(Event event){
        final Map<String, Integer> map = new HashMap<>();
        String eventBody = new String(event.getBody());
        if(!eventBody.isEmpty()){
            // parse the string by :
            String[] tokens = eventBody.split(":");
            map.put(tokens[0].trim(), Integer.valueOf(tokens[1].trim()));
        }

        return map;
    }
}
