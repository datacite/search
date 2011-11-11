package org.datacite.search.handler.dataimport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.apache.solr.handler.dataimport.Context;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

public class KeyValueTransformerTest {
    
    KeyValueTransformer transformer = new KeyValueTransformer();
    
    final String KEYS_FIELD = "keysField";
    final String VALUES_FIELD = "valuesField";
    final String TARGET_FIELD = "targetField";
    
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
    
    @Test
    public void testNullKeys() {
        assertTransformRow(null, null, ListUtils.EMPTY_LIST);
    }

    @Test
    public void testNullValues() {
        assertTransformRow(null, ListUtils.EMPTY_LIST, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongKeysType() {
        assertTransformRow(null, "this is not a multivalued field", ListUtils.EMPTY_LIST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongValuesType() {
        assertTransformRow(null, ListUtils.EMPTY_LIST, "this is not a multivalued field");
    }

    private void assertTransformRow(List target, Object keys, Object values) {
        HashMap<String, Object> row = new HashMap<String, Object>();
        row.put(KEYS_FIELD, keys);
        row.put(VALUES_FIELD, values);
        HashMap<String, Object> origRow = (HashMap<String, Object>) row.clone();
        
        Context context = createMockContext();
        row = (HashMap<String, Object>) transformer.transformRow(row, context);
        EasyMock.verify(context);

        List targetActual = (List) row.get(TARGET_FIELD);
        Assert.assertEquals(target, targetActual);
        
        //check if other fields have changed
        row.remove(TARGET_FIELD);
        Assert.assertEquals(origRow, row);
    }
    
    private Context createMockContext() {
        Context context = EasyMock.createNiceMock(Context.class);
        List<Map<String, String>> fields = new ArrayList<Map<String,String>>();
        Map<String, String> field = new HashMap<String, String>();
        field.put(transformer.KEYS_ATTRIBUTE, KEYS_FIELD);
        field.put(transformer.TARGET_ATTRIBUTE, TARGET_FIELD);
        field.put(transformer.VALUES_ATTRIBUTE, VALUES_FIELD);
        fields.add(field);
        EasyMock.expect(context.getAllEntityFields()).andReturn(fields);
        EasyMock.replay(context);
        return context;
    }
    
}
