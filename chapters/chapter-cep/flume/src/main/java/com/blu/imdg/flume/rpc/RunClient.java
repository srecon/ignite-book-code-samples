package com.blu.imdg.flume.rpc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by shamim on 11/08/16.
 */
public class RunClient {
    private static final String HOST_NAME="flume.agent.host";
    private static final String PORT="flume.agent.port";

    public static void main(String[] args) {
        System.out.println("RPC client to send data");
        // read properties from file
        if(getProperties() == null || getProperties().isEmpty()){
            System.out.println("Property file <generator.property> is not found or empty");
            return;
        }

        RpcEventGenerator eventGenerator = new RpcEventGenerator(getProperties().getProperty(HOST_NAME), Integer.valueOf(getProperties().getProperty(PORT)));
        // generate some sequence of transactions

        for(int i = 1; i < 100 ; i++){
            eventGenerator.sendData("transaction"+i+":"+i);
        }
        eventGenerator.cleanUp();
    }
    private static Properties getProperties(){
        Properties properties = new Properties();
        InputStream ins = RunClient.class.getClassLoader().getResourceAsStream("generator.properties");

        try {
            properties.load(ins);
        } catch (IOException e) {
            e.printStackTrace();
            properties = null;
        }
        return properties;
    }
}
