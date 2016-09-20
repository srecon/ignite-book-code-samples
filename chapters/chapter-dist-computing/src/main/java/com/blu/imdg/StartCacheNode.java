package com.blu.imdg;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

import java.io.IOException;

/**
 * Created by mikl on 19.08.16.
 */
//mvn exec:java -Dexec.mainClass=StartCacheNode
public class StartCacheNode {
    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start("org/book/examples/cache-node-config.xml")) {
            System.out.println("Presse ENTER to exit!");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
