package hu.sztaki.ilab.longneck.process.constraint;

import hu.sztaki.ilab.longneck.Field;
import hu.sztaki.ilab.longneck.Record;
import hu.sztaki.ilab.longneck.RecordImpl;
import java.util.TreeSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Test for Lookup constraint.
 *
 * @author Lukacs Gabor <lukacsg@sztaki.mta.hu>
 */
@ContextConfiguration(locations = {"classpath:/test-beans.xml" })
public class LookupTest extends AbstractJUnit4SpringContextTests {
    
    public LookupTest() {
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
     * Test Lookup with ExternalWordset.
     */
    @Test
    public void testExternalWordset() {
        Record record = new RecordImpl();
        record.add(new Field("searchfor", "test1"));
        record.add(new Field("searchfor2", "test3"));
        
        Lookup lookup = new Lookup();
        lookup.setWords((Wordset) applicationContext.getBean("external-wordset"));
        // pozitive test
        lookup.setApplyTo("searchfor");
        assertTrue("Can't find word in external dictionary.", lookup.check(record, null).isPassed());
        
        // negativ test
        lookup.setApplyTo("searchfor searchfor2");
        assertFalse("Find word in external dictionary but it doesn't present.", lookup.check(record, null).isPassed());
    }

    /**
     * Test Lookup with InlineWordset.
     */
    @Test
    public void testInlineWordset() {
        Record record = new RecordImpl();
        record.add(new Field("searchfor", "test1"));
        record.add(new Field("searchfor2", "test3"));
        
        Lookup lookup = new Lookup();
        InlineWordset inline = new InlineWordset();
        TreeSet<String> tset = new TreeSet<String>();
        tset.add("test1");
        tset.add("test2");
        inline.setWords(tset);
        lookup.setWords(inline);
        // pozitive test
        lookup.setApplyTo("searchfor");
        assertTrue("Can't find word in external dictionary.", lookup.check(record, null).isPassed());
        
        // negativ test
        lookup.setApplyTo("searchfor searchfor2");
        assertFalse("Find word in external dictionary but it doesn't present.", lookup.check(record, null).isPassed());
    }
}