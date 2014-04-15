package org.datacite.search.handler.dataimport;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.handler.dataimport.Context;
import org.apache.solr.handler.dataimport.DataImporter;
import org.easymock.EasyMock;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class TrimTransformerTest {
    
    TrimTransformer transformer = new TrimTransformer();
    
    String FIELD="field";
    
    @Test
    public void test() {
        assertEquals("bar", transform("   bar   "));
        assertEquals("foo   bar", transform("foo   bar"));
    }
    
    @Test
    public void testMultiValue() {
        List<String> original = Arrays.asList(" foo ", "bar   ", "zz yy");
        List<String> expected = Arrays.asList("foo", "bar", "zz yy");
        List<String> actual = (List<String>) transform(original);
        assertArrayEquals(expected.toArray(), actual.toArray());
}
    
    @Test
    public void testBooleanParameter() {
        assertTrue(isTransforming("true"));
        assertFalse(isTransforming("false"));
        assertFalse(isTransforming(""));
        assertFalse(isTransforming(null));
        assertFalse(isTransforming("foobar"));
    }
    
    private boolean isTransforming(String trim) {
        String value = " foo ";
        return !transform(value, trim).equals(value);
    }
    
    private Object transform(Object value) {
        String trim = "true";
        return transform(value, trim);
    }

    private Object transform(Object value, String trim) {
        String column = "column";
        
        List<Map<String, String>> fields = new ArrayList<Map<String, String>>();
        Map<String, String> field = new HashMap<String, String>();
        field.put(DataImporter.COLUMN, column);
        field.put(TrimTransformer.TRIM_ATTRIBUTE, trim);
        fields.add(field);
        
        Map<String, Object> row = new HashMap<String, Object>();
        row.put(column, value);
        
        Map<String, Object> newrow = (Map<String, Object>) transformer.transformRow(row, mockContext(fields));
        return newrow.get(column);
    }
    
    private Context mockContext(List<Map<String, String>> fields) {
        Context context = EasyMock.createNiceMock(Context.class);
        EasyMock.expect(context.getAllEntityFields()).andReturn(fields);
        EasyMock.replay(context);
        return context;
    }

    

}
