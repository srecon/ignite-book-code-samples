package com.blu.imdg.example7;

import com.blu.imdg.common.CommonConstants;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

import java.io.IOException;

/**
 * Created by shamim on 25/12/16.
 */
public class DeployClusterSingleton {
    private static final String VALIDATING_SERVICE = "validatingService-cluster-singleton";

    public static void main(String[] args) throws IOException {
        try (Ignite ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {

            System.out.println("Deploy Node-singleton service!!");
            ignite.services().deployClusterSingleton(VALIDATING_SERVICE, new XsdValidatingServiceImpl());

        }

    }
}
