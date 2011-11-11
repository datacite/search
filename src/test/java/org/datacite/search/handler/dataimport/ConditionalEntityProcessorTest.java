package org.datacite.search.handler.dataimport;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.handler.dataimport.Context;
import org.apache.solr.handler.dataimport.ContextImpl;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

public class ConditionalEntityProcessorTest {
    
    ConditionalEntityProcessor processor = new ConditionalEntityProcessor();
    
    @Test
    public void test() {
        assertProcessor(false, null);
        assertProcessor(false, "false");
        assertProcessor(false, "0");
        assertProcessor(false, 42);
        assertProcessor(true, 1);
        assertProcessor(true, "truE");
        assertProcessor(true, "ON");
    }
    
    private void assertProcessor(boolean expectProcessed, Object fieldValue) {
        initProcessor(fieldValue);
        Map<String, Object> nextRow = processor.nextRow();
        if (expectProcessed) {
            assertNotNull(nextRow);
            assertEquals(0, nextRow.size());
        } else {
            assertNull(nextRow);
        }
        assertNull(processor.nextRow());
    }
    
    private void initProcessor(Object fieldValue) {
        Context context = EasyMock.createNiceMock(Context.class);
        String conditionalField = "field";
        EasyMock.expect(context.getEntityAttribute(ConditionalEntityProcessor.CONDITIONAL_ATTRIBUTE)).andReturn(conditionalField);
        EasyMock.expect(context.resolve(conditionalField)).andReturn(fieldValue);
        EasyMock.replay(context);
        processor.init(context);
    }
    

}
