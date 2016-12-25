package com.blu.imdg.example7;

import com.blu.imdg.common.CommonConstants;
import com.blu.imdg.common.TestDataGenerator;
import com.blu.imdg.example3.ValidateMessage;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

import java.io.IOException;

/**
 * Created by shamim on 25/12/16.
 */
public class DeployNodeSingleton {
    private static final String VALIDATING_SERVICE = "validatingService";

    public static void main(String[] args) throws IOException{
        try (Ignite ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {

            System.out.println("Deploy Node-singleton service!!");
            ignite.services().deployNodeSingleton(VALIDATING_SERVICE, new XsdValidatingServiceImpl());

        }

    }
}
