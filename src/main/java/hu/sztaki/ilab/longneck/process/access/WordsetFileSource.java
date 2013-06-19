package hu.sztaki.ilab.longneck.process.access;

import hu.sztaki.ilab.longneck.Field;
import hu.sztaki.ilab.longneck.Record;
import hu.sztaki.ilab.longneck.RecordImpl;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Source of words set is file.
 * One line is one value.
 *
 */
public class WordsetFileSource implements Source {

  private static final Logger LOG = Logger.getLogger(WordsetFileSource.class);
  private static final String DEFAULT_ENCODING = "UTF8"; //default file encoding
  private String sourceFile;
  private List<String> sourceList;
  private static final String COLUMN_FIELD_NAME = "word"; //coulmn-name

  @Override
  public void init() {
    readFile();
  }

  private void readFile() {

    sourceList = Collections.synchronizedList(new ArrayList<String>());
    try {

      BufferedReader br;
      br = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), DEFAULT_ENCODING));
      String line;
      while ((line = br.readLine()) != null) {
        sourceList.add(line);
      }
    } catch (UnsupportedEncodingException e) {
      LOG.error("Unsupported encoding in " + sourceFile + " wordset file", e);
    } catch (FileNotFoundException e) {
      LOG.error("Can't read wordset file: " + sourceFile, e);
    } catch (IOException e) {
      LOG.error("Error when reading wordset file: " + sourceFile, e);
    }

  }

  public String getSourceFile() {
    return sourceFile;
  }

  public void setSourceFile(String sourceFile) {
    this.sourceFile = sourceFile;
  }

  @Override
  public Record getRecord() throws NoMoreRecordsException {
    if (sourceList == null || sourceList.isEmpty()) {
      throw new NoMoreRecordsException();
    }

    String e = sourceList.remove(0);

    if (e == null) {
      throw new NoMoreRecordsException();
    }

    Record result = new RecordImpl();
    result.add(new Field(COLUMN_FIELD_NAME, e));
    return result;

  }

  @Override
  public void close() {
  }
}
