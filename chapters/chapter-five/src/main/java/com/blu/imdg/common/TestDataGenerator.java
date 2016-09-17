package com.blu.imdg.common;

import org.apache.commons.io.IOUtils;
import com.blu.imdg.example3.ValidateMessage;

import java.io.IOException;

/**
 * Created by mikl on 22.08.16.
 */
public class TestDataGenerator {
    private static final String SAMPLE1 = "/META-INF/org/book/examples/data/sample1.xml";
    private static final String SAMPLE2 = "/META-INF/org/book/examples/data/sample2.xml";
    private static final String SAMPLE3 = "/META-INF/org/book/examples/data/sample3.xml";
    private static final String VALIDATE_SCHEMA = "/META-INF/org/book/examples/data/validate-schema.xsd";
    private static final String VALIDATE_SCRIPT = "/META-INF/org/book/examples/data/validate-script.js";

    private static String getStringResource(String resource) throws IOException {
        return IOUtils.toString(TestDataGenerator.class.getResourceAsStream(resource));
    }

    private static byte[] getByteResource(String resource) throws IOException {
        return IOUtils.toByteArray(TestDataGenerator.class.getResourceAsStream(resource));
    }


    public static String getValidateScript() throws IOException {
        return getStringResource(VALIDATE_SCRIPT);
    }

    public static byte[] getValidateSchema() throws IOException {
        return getByteResource(VALIDATE_SCHEMA);
    }

    public static String getSample1() throws IOException {
        return getStringResource(SAMPLE1);
    }

    public static String getSample2() throws IOException {
        return getStringResource(SAMPLE2);
    }

    public static ValidateMessage[] getValidateMessages() throws IOException {
        byte[] vaidateSchema = getValidateSchema();
        String validateScript = getValidateScript();

        return new ValidateMessage[]{
                new ValidateMessage("1", getStringResource(SAMPLE1), vaidateSchema, validateScript),
                new ValidateMessage("2", getStringResource(SAMPLE2), vaidateSchema, validateScript),
                new ValidateMessage("3", getStringResource(SAMPLE3), vaidateSchema, validateScript)
        };
    }


}
