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
public class RunXsdValidationService {
    private static final String VALIDATING_SERVICE = "validatingService";

    public static void main(String[] args) throws IOException{
        try (Ignite ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {

            String sample1 = TestDataGenerator.getSample1();
            String sample2 = TestDataGenerator.getSample2();
            byte[] vaidateSchema = TestDataGenerator.getValidateSchema();
            String validateScript = TestDataGenerator.getValidateScript();

            System.out.println("Invoke remote service!!");

            XsdValidatingService xsdValidatingService = ignite.services().serviceProxy(VALIDATING_SERVICE, XsdValidatingService.class, /*not-sticky*/false);

            System.out.println("result=" + xsdValidatingService.isOk(new ValidateMessage("1", sample1, vaidateSchema, validateScript)));
            System.out.println("result2=" + xsdValidatingService.isOk(new ValidateMessage("2", sample2, vaidateSchema, validateScript)));

            //ignite.services().cancel(VALIDATING_SERVICE);
        }

    }

}
