package hu.sztaki.ilab.longneck.process.access;

import hu.sztaki.ilab.longneck.Field;
import hu.sztaki.ilab.longneck.Record;
import hu.sztaki.ilab.longneck.RecordImpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import longneck.ilab.sztaki.hu._1.DictionaryElements;
import longneck.ilab.sztaki.hu._1.Element;
import longneck.ilab.sztaki.hu._1.KeyType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Source that reads key-value dictionary XML files
 *  
 * @author Bendig Loránd <lbendig@ilab.sztaki.hu>
 *
 */
public class DictionaryFileSource implements Source {

  private static final Logger LOG = Logger.getLogger(DictionaryFileSource.class);
  /** Hardcoded field names used by <external-dictionary> */
  private static final String KEY_FIELD_NAME = "key";            //key-column-name
  private static final String VALUE_FIELD_NAME = "value";        //value-column-name
  private static final String REGEXP_TYPE_FIELD_NAME = "regexp"; //key-type-column-name
  private static final String DEFAULT_ENCODING = "UTF8";         //default file encoding
  private String sourceFile;
  private List<Element> sourceElements;
  @Autowired
  private XmlDictionaryUtils xmlDictionaryUtils;

  @Override
  public void init() {
    try {

      Unmarshaller unmarshaller = xmlDictionaryUtils.getUnmarshaller();
      InputStream inputStream = new FileInputStream(sourceFile);
      Reader reader = new InputStreamReader(inputStream, DEFAULT_ENCODING);
      DictionaryElements de =
              (DictionaryElements) unmarshaller.unmarshal(reader);
      sourceElements = Collections.synchronizedList(de.getElement());
    } catch (UnsupportedEncodingException e) {
      LOG.error("Unsupported encoding in " + sourceFile + " XML dictionary", e);
    } catch (JAXBException e) {
      LOG.error("Can't read XML dictionary", e);
    } catch (FileNotFoundException e) {
      LOG.error("Can't find source dictionary", e);
    }
  }

  @Override
  public Record getRecord() throws NoMoreRecordsException {
    if (sourceElements == null || sourceElements.isEmpty()) {
      throw new NoMoreRecordsException();
    }

    Element e = sourceElements.remove(0);

    if (e == null) {
      throw new NoMoreRecordsException();
    }

    KeyType keyType = e.getKey();
    Record result = new RecordImpl();
    result.add(new Field(KEY_FIELD_NAME, keyType.getContent()));
    result.add(new Field(VALUE_FIELD_NAME, e.getVal()));
    result.add(new Field(REGEXP_TYPE_FIELD_NAME, String.valueOf(keyType.isRegexp())));

    return result;
  }

  public String getSourceFile() {
    return sourceFile;
  }

  public void setSourceFile(String sourceFile) {
    this.sourceFile = sourceFile;
  }

  @Override
  public void close() {
  }
}
