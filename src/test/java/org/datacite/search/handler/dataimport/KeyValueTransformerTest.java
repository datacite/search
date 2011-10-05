package org.datacite.search.handler.dataimport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.handler.dataimport.Context;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

public class KeyValueTransformerTest {
    
    KeyValueTransformer transformer = new KeyValueTransformer();
    
    String keysField = "keysField";
    String valuesField = "valuesField";
    String targetField = "targetField";
    
    @Test
    public void test() {
        List keys = Arrays.asList("foo", "foo2");
        List values = Arrays.asList("bar", "bar2");
        List target = Arrays.asList("foo:bar", "foo2:bar2");
        assertTransformRow(target, keys, values);
    }
    
    @Test(expected = RuntimeException.class)
    public void testDifferentSize() {
        List keys = Arrays.asList("foo", "foo2", "foo3");
        List values = Arrays.asList("bar", "bar2");
        List target = Arrays.asList("foo:bar", "foo2:bar2");
        assertTransformRow(target, keys, values);
    }
    
    private void assertTransformRow(List target, List keys, List values) {
        Context context = createMockContext();
        Map<String, Object> row = new HashMap<String, Object>();
        row.put(keysField, keys);
        row.put(valuesField, values);
        row = (Map<String, Object>) transformer.transformRow(row, context);
        List targetActual = (List) row.get(targetField);
        EasyMock.verify(context);
        Assert.assertNotNull(targetActual);
        Assert.assertEquals(target.size(), targetActual.size());
        Assert.assertArrayEquals(target.toArray(), targetActual.toArray());
    }
    
    private Context createMockContext() {
        Context context = EasyMock.createNiceMock(Context.class);
        List<Map<String, String>> fields = new ArrayList<Map<String,String>>();
        Map<String, String> field = new HashMap<String, String>();
        field.put(transformer.keysAttribute, keysField);
        field.put(transformer.targetAttribute, targetField);
        field.put(transformer.valuesAttribute, valuesField);
        fields.add(field);
        EasyMock.expect(context.getAllEntityFields()).andReturn(fields);
        EasyMock.replay(context);
        return context;
    }
    

}
