package com.blu.imdg.common;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.function.Function;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by mzheludkov on 04.07.16.
 */
public class XPathExecutor implements Function<String, Object> {
    private Document document;

    public XPathExecutor(String xml) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(new InputSource(new StringReader(xml)));
        } catch (Exception err) {
            throw new RuntimeException(err);
        }
    }

    public Object apply(String xpathStr) {
        try {
            final XPath xpath = XPathFactory.newInstance().newXPath();
            return xpath.evaluate(xpathStr, document);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
