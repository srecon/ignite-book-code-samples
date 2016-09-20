package com.blu.imdg.example2;

import com.blu.imdg.common.CommonConstants;
import com.blu.imdg.common.JSEvaluate;
import com.blu.imdg.common.XsdValidator;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import com.blu.imdg.common.TestDataGenerator;

import java.io.IOException;

/**
 * Created by mzheludkov on 30.06.16.
 */
//mvn exec:java -Dexec.mainClass=message.ExampleWithLambda
public class AsyncComputation {



    public static void main(String[] args) throws IOException {
        String sample1 = TestDataGenerator.getSample1();
        byte[] vaidateSchema = TestDataGenerator.getValidateSchema();
        String validateScript = TestDataGenerator.getValidateScript();

        try (Ignite ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {
            IgniteCompute compute = ignite.compute().withAsync();

            compute.call(() -> {
                boolean validateXsdResult = XsdValidator.validate(sample1, vaidateSchema);
                boolean validateByJs = JSEvaluate.evaluateJs(sample1, validateScript);

                System.out.println("validateXsdResult=" + validateXsdResult);
                System.out.println("validateByJs=" + validateByJs);

                return validateXsdResult && validateByJs;
            });

            compute.future().listen((result) -> {
                boolean res = (boolean) result.get();
                System.out.println("result=" + res);
            });


            System.out.println("Presse ENTER to exit!");
            System.in.read();
        }
    }

}
