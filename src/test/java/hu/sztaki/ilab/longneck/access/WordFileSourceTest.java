package hu.sztaki.ilab.longneck.access;

import hu.sztaki.ilab.longneck.process.access.NoMoreRecordsException;
import hu.sztaki.ilab.longneck.process.access.WordsetFileSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Lukacs Gabor <lukacsg@sztaki.mta.hu>
 */
public class WordFileSourceTest {
    
    public WordFileSourceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /** Test getRecord method of the source. */
    @Test
    public void testgetRecord() throws NoMoreRecordsException {
        WordsetFileSource source = new WordsetFileSource();
        source.setSourceFile("src/test/resources/testwordfilesource.txt");
        source.init();
        assertEquals("Error during reading from the source.", source.getRecord().getFields().get("word").toString(), "word: test1");
        assertEquals("Error during reading from the source.", source.getRecord().getFields().get("word").toString(), "word: test2");
        try {
            source.getRecord();
            fail("It shoudn't return more record.");
        } catch (NoMoreRecordsException e) {
            // it is the expected behavior.
        }   
    }
}