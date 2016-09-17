package com.blu.imdg.flume.rpc;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;

/**
 * Created by shamim on 11/08/16.
 */
public class RpcEventGenerator {
    private org.apache.flume.api.RpcClient rpcClient;
    private String hostName;
    private int port;

    public RpcEventGenerator(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
        this.rpcClient = RpcClientFactory.getDefaultInstance(hostName, port);
    }

    public void sendData(String data){
        Event event = EventBuilder.withBody(data.getBytes());

        try {
            rpcClient.append(event);
        } catch (EventDeliveryException e) {
            e.printStackTrace();
            rpcClient.close();
            rpcClient = RpcClientFactory.getDefaultInstance(this.hostName, this.port);
        }

    }
    public void cleanUp(){
        if(rpcClient.isActive()){
            rpcClient.close();
        }

    }
}
