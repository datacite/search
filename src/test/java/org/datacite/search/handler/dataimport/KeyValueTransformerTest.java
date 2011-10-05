package org.datacite.search.handler.dataimport;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class KeyValueTransformerTest {
    
    KeyValueTransformer transformer = new KeyValueTransformer();
    
    Map<String, Object> row;
    
    @Before
    public void init() {
        row = new HashMap<String, Object>();
    }
    
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
        row.put(transformer.keysField, keys);
        row.put(transformer.valuesField, values);
        row = (Map<String, Object>) transformer.transformRow(row, null);
        List targetActual = (List) row.get(transformer.targetField);
        Assert.assertNotNull(targetActual);
        Assert.assertEquals(target.size(), targetActual.size());
        Assert.assertArrayEquals(target.toArray(), targetActual.toArray());
    }
}
