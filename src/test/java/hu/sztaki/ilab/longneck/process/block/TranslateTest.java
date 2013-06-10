package hu.sztaki.ilab.longneck.process.block;

import hu.sztaki.ilab.longneck.Field;
import hu.sztaki.ilab.longneck.Record;
import hu.sztaki.ilab.longneck.RecordImpl;
import hu.sztaki.ilab.longneck.process.CheckError;
import hu.sztaki.ilab.longneck.process.VariableSpace;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 *  Test for Translate block.
 * @author Lukacs Gabor <lukacsg@sztaki.mta.hu>
 */
@ContextConfiguration(locations = {"classpath:/test-beans.xml" })
public class TranslateTest extends AbstractJUnit4SpringContextTests  {
    
    public TranslateTest() {
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

     /**
     * Test Translate with ExternalDictionary.
     */
    @Test
    public void testExternalDictionary() throws CheckError {
        Record record = new RecordImpl();
        record.add(new Field("to", ""));
        record.add(new Field("from", "testkey2"));
        record.add(new Field("from2", "testkey3"));
        
        Translate translate = new Translate();
        translate.setDictionary((Dictionary) applicationContext.getBean("external-dictionary"));
        // pozitive test
        translate.setApplyTo("to");
        translate.setFrom("from");
        translate.apply(record, null);
        assertEquals("Dictionary error, didn't translate field", "value2", record.get("to").getValue());
        
        // negativ test
        translate.setFrom("from2");
        try {
            translate.apply(record, null);
        fail("It should throw an CheckError.");
        } catch (CheckError e) {
            // expected behavior.
        }
    }

     /**
     * Test Translate with InlineDictionary.
     */
    @Test
    public void testInlineDictionary() throws CheckError {
        Record record = new RecordImpl();
        record.add(new Field("to", ""));
        record.add(new Field("from", "testkey2"));
        record.add(new Field("from2", "testkey3"));
        
        Translate translate = new Translate();
        InlineDictionary inline = new InlineDictionary();
        inline.addItem(new DictItem(new KeyType(".*(testkey[134]).*", true), "$1"));
        inline.addItem(new DictItem(new KeyType("testkey2", false), "value2"));
        translate.setDictionary(inline);
        // pozitive test
        translate.setApplyTo("to");
        translate.setFrom("from");
        translate.apply(record, null);
        assertEquals("Dictionary error, didn't translate field", "value2", record.get("to").getValue());
        
        // negativ test
        translate.setFrom("from2");
        try {
            translate.apply(record, null);
        fail("It should throw an CheckError.");
        } catch (CheckError e) {
            // expected behavior.
        }
    }

     /**
     * Test Translate with RegexpDictionary and ExternalDictionary.
     */
    @Test
    public void testExternalRegexpDictionary() throws CheckError {
        Record record = new RecordImpl();
        record.add(new Field("to", ""));
        record.add(new Field("from", "testkey2"));
        record.add(new Field("from2", "testkey3"));
        
        Translate translate = new Translate();
        translate.setDictionary((Dictionary) applicationContext.getBean("regexp-dictionary"));
        // pozitive test
        translate.setApplyTo("to");
        translate.setFrom("from");
        translate.apply(record, null);
        assertEquals("Dictionary error, didn't translate field", "value2", record.get("to").getValue());
        
        // regexp test
        translate.setFrom("from2");
        translate.apply(record, null);
        assertEquals("Dictionary error, didn't translate field", "testkey3", record.get("to").getValue());
    }
    
     /**
     * Test Translate with RegexpDictionary and InlineDictionary.
     */
    @Test
    public void testInlineRegexpDictionary() throws CheckError {
        Record record = new RecordImpl();
        record.add(new Field("to", ""));
        record.add(new Field("from", "testkey2"));
        record.add(new Field("from2", "testkey3"));
        
        Translate translate = new Translate();
        InlineDictionary inline = new InlineDictionary();
        inline.addItem(new DictItem(new KeyType(".*(testkey[134]).*", true), "$1"));
        inline.addItem(new DictItem(new KeyType("testkey2", false), "value2"));
        RegexpDictionary regexp = new RegexpDictionary();
        regexp.setDictionary(inline);
        translate.setDictionary(regexp);
        // pozitive test
        translate.setApplyTo("to");
        translate.setFrom("from");
        translate.apply(record, null);
        assertEquals("Dictionary error, didn't translate field", "value2", record.get("to").getValue());
        
        // regexp test
        translate.setFrom("from2");
        translate.apply(record, null);
        assertEquals("Dictionary error, didn't translate field", "testkey3", record.get("to").getValue());
    }
}