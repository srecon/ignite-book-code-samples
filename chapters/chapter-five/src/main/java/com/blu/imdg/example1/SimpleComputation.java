package com.blu.imdg.example1;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import com.blu.imdg.common.JSEvaluate;
import com.blu.imdg.common.TestDataGenerator;
import com.blu.imdg.common.XsdValidator;

import java.io.IOException;

import static com.blu.imdg.common.CommonConstants.CLIENT_CONFIG;

/**
 * Created by mikl on 19.08.16.
 */
//mvn exec:java -Dexec.mainClass=SimpleComputation
public class SimpleComputation {




    public static void main(String[] args) throws IOException {
        String sample1 = TestDataGenerator.getSample1();
        byte[] vaidateSchema = TestDataGenerator.getValidateSchema();
        String validateScript = TestDataGenerator.getValidateScript();

        try (Ignite ignite = Ignition.start(CLIENT_CONFIG)) {
            IgniteCompute compute = ignite.compute();

            Boolean result = compute.call(() -> {
                boolean validateXsdResult = XsdValidator.validate(sample1, vaidateSchema);
                boolean validateByJs = JSEvaluate.evaluateJs(sample1, validateScript);

                System.out.println("validateXsdResult=" + validateXsdResult);
                System.out.println("validateByJs=" + validateByJs);

                return validateXsdResult && validateByJs;
            });

            System.out.println("result=" + result);
        }
    }
}
