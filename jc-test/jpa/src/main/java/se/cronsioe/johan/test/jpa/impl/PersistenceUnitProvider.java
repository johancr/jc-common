package se.cronsioe.johan.test.jpa.impl;

import com.google.inject.Provider;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;


public class PersistenceUnitProvider implements Provider<String> {

    @Override
    public String get() {

        InputStream persistence = PersistenceUnitProvider.class.getResourceAsStream("/META-INF/persistence.xml");

        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(persistence);
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            XPathExpression xPathExpression = xPath.compile("persistence/persistence-unit/@name");

            return (String) xPathExpression.evaluate(document, XPathConstants.STRING);
        }
        catch (Exception ex)
        {
            throw new IllegalStateException("Could not find persistence unit: " + ex, ex);
        }
    }
}
