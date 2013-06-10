package hu.sztaki.ilab.longneck.process.access;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import longneck.ilab.sztaki.hu._1.DictionaryElements;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

/**
 * Helper class to initialize XML binding resources
 * 
 * @author Bendig Loránd <lbendig@ilab.sztaki.hu>
 * 
 */
@Service
public class XmlDictionaryUtils {

    private static final Logger LOG = Logger.getLogger(XmlDictionaryUtils.class);

    private Unmarshaller unmarshaller;

    public XmlDictionaryUtils() {

        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] { DictionaryElements.class });
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            unmarshaller = jaxbContext.createUnmarshaller();

            Schema schema = schemaFactory.newSchema(
                this.getClass().getClassLoader().getResource("META-INF/longneck/schema/dictionaryFileSource.xsd"));

            unmarshaller.setSchema(schema);

        } catch (JAXBException e) {
            LOG.error("Can't unmarshall XML dictionary", e);
        } catch (SAXException e) {
            LOG.error("XML dictionary schema error", e);
        }

    }

    public Unmarshaller getUnmarshaller() {
        return unmarshaller;
    }
}
