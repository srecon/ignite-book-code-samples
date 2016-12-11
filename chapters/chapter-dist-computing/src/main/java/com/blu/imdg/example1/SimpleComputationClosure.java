package com.blu.imdg.example1;

import com.blu.imdg.common.JSEvaluate;
import com.blu.imdg.common.TestDataGenerator;
import com.blu.imdg.common.XsdValidator;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteClosure;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static com.blu.imdg.common.CommonConstants.CLIENT_CONFIG;

/**
 * Created by shamim on 26/11/16.
 * mvn exec:java -Dexec.mainClass=com.blu.imdg.example1.SimpleComputationClosure
 */
public class SimpleComputationClosure {
    public static void main(String[] args) throws IOException {
        try (Ignite ignite = Ignition.start(CLIENT_CONFIG)) {
            IgniteCompute compute = ignite.compute();

            // Execute closure on all cluster nodes.
            Collection<Integer> res = compute.apply(
                    new IgniteClosure<String, Integer>() {
                        @Override
                        public Integer apply(String word) {
                            // Return number of letters in the word.
                            return word.length();
                        }
                    },
                    Arrays.asList("Count characters using closure".split(" "))
            );

            int sum = 0;

            // Add up individual word lengths received from remote nodes
            for (int len : res)
                sum += len;
            System.out.println("Length of the sentence: "+ sum);
        }
    }
}
