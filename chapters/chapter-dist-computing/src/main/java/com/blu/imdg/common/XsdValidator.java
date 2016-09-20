package com.blu.imdg.common;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;

/**
 * Created by mzheludkov on 04.07.16.
 */
public class XsdValidator {
    public static boolean validate(String xml, byte[] xsd) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Source schemaFile = new StreamSource(new ByteArrayInputStream(xsd));
            Schema schema = factory.newSchema(schemaFile);

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new ByteArrayInputStream(xml.getBytes())));
            return true;
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
    }

}
