package com.blu.imdg.example7;

import com.blu.imdg.common.CommonConstants;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.services.ServiceDescriptor;

import java.io.IOException;

/**
 * Created by shamim on 25/12/16.
 */
public class ServiceManagements {
    public static void main(String[] args) throws IOException{
        try (Ignite ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {

            for(ServiceDescriptor serviceDescriptor : ignite.services().serviceDescriptors()){
                System.out.println("Service Name: " + serviceDescriptor.name());
                System.out.println("MaxPerNode count: " + serviceDescriptor.maxPerNodeCount());
                System.out.println("Total count: " + serviceDescriptor.totalCount());
                System.out.println("Service class Name: " + serviceDescriptor.serviceClass());
                System.out.println("Origin Node ID: " + serviceDescriptor.originNodeId());

            }

        }
    }
}
