package se.cronsioe.johan.test.xml;

import org.xml.sax.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class XsdValidator {

    public static boolean validate(InputStream xml) throws RuntimeException {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);

        try
        {
            SAXParser parser = factory.newSAXParser();
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");

            XMLReader reader = parser.getXMLReader();
            reader.setErrorHandler(getErrorHandler());
            reader.setEntityResolver(getClasspathResolver());

            reader.parse(new InputSource(xml));
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        return true;
    }

    private static ErrorHandler getErrorHandler() {
        return new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) throws SAXException {
                throw exception;
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                throw exception;
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                throw exception;
            }
        };
    }

    private static EntityResolver getClasspathResolver() {
        return new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, final String systemId) throws SAXException, IOException {
                return new InputSource(new URL(null, systemId, new URLStreamHandler() {
                    @Override
                    protected URLConnection openConnection(URL url) throws IOException {
                        ClassLoader current = Thread.currentThread().getContextClassLoader();

                        try
                        {
                            return current.getResource(url.getPath()).openConnection();
                        }
                        catch (NullPointerException ex)
                        {
                            throw new RuntimeException("Could not load xsd at: " + systemId + "\n" + ex, ex);
                        }
                    }
                }).openStream());
            }
        };
    }
}
