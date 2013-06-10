package hu.sztaki.ilab.longneck.access;

import hu.sztaki.ilab.longneck.Record;
import hu.sztaki.ilab.longneck.process.access.DictionaryFileSource;
import hu.sztaki.ilab.longneck.process.access.NoMoreRecordsException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 *
 * @author Lukacs Gabor <lukacsg@sztaki.mta.hu>
 */
@ContextConfiguration(locations = {"classpath:/test-beans.xml" })
public class DictionaryFileSourceTest extends AbstractJUnit4SpringContextTests {
    
    public DictionaryFileSourceTest() {
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
        DictionaryFileSource source = (DictionaryFileSource) applicationContext.getBean("dictionary-file-source");
        // 1. record
        Record r = source.getRecord();
        assertEquals("Error during reading from the source.", r.get("key").toString(), "key: .*(testkey[134]).*");
        assertEquals("Error during reading from the source.", r.get("value").toString(), "value: $1");
        assertEquals("Error during reading from the source.", r.get("regexp").toString(), "regexp: true");
        // 2. record
        r = source.getRecord();
        assertEquals("Error during reading from the source.", r.get("key").toString(), "key: testkey2");
        assertEquals("Error during reading from the source.", r.get("value").toString(), "value: value2");
        assertEquals("Error during reading from the source.", r.get("regexp").toString(), "regexp: false");
        try {
            source.getRecord();
            fail("It shoudn't return more record.");
        } catch (NoMoreRecordsException e) {
            // it is the expected behavior.
        }   
    }
}