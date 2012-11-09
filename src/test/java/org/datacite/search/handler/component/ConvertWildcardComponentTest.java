package org.datacite.search.handler.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.solr.handler.component.ResponseBuilder;
import org.datacite.search.test.DummyQParser;
import org.datacite.search.test.DummyQuery;
import org.junit.Before;
import org.junit.Test;

public class ConvertWildcardComponentTest {

    ConvertWildcardComponent component;
    ResponseBuilder rb;

    @Before
    public void init() throws Exception {
        component = new ConvertWildcardComponent();
        rb = new ResponseBuilder(null, null, null);
        rb.setQparser(new DummyQParser());
    }
    
    @Test 
    public void testSolrInfoMBean() {
        assertNotNull(component.getDescription());
        assertNotNull(component.getSource());
        assertNotNull(component.getVersion());
    }

    @Test
    public void test() throws Exception {
        testQuery(null, null);
        testQuery("", "");
        testQuery("term", "term");
        testQuery("*:*", "*:*");
        testQuery("*", "*:*");
        testQuery("*ab", "*ab");
        testQuery("* foo", "* foo");
    }

    private void testQuery(String qstrIn, String qstrOut) throws Exception {
        rb.setQuery(new DummyQuery(qstrIn));
        rb.setQueryString(qstrIn);
        component.prepare(rb);
        component.process(rb);
        assertEquals(qstrOut, rb.getQueryString());
        assertEquals(qstrOut, rb.getQuery().toString());
    }
    
    
    @Test(expected = RuntimeException.class)
    public void testException() throws Exception {
        rb.setQparser(new DummyQParserWithException());
        testQuery("*", "*");
    }
    
    private class DummyQParserWithException extends DummyQParser {
        @Override
        public Query parse() throws ParseException {
            throw new ParseException();
        }
        
    }

}
