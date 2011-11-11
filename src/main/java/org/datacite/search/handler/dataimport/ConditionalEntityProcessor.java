package org.datacite.search.handler.dataimport;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.solr.handler.dataimport.Context;
import org.apache.solr.handler.dataimport.EntityProcessorBase;

/**
 * DataImportHandler entity, which is only processed if a given field exists and
 * have a true value ("1" or interpreted by {@link BooleanUtils#toBoolean(String)}
 * 
 * <pre>
 * &lt;entity name="myname" hasField="field_to_check" 
 *         processor="org.datacite.search.handler.dataimport.ConditionalEntityProcessor">
 *   [...]
 * &lt;/entity>
 * </pre>
 */
public class ConditionalEntityProcessor extends EntityProcessorBase {

    public static final String CONDITIONAL_ATTRIBUTE = "hasField";
    
    boolean skip;

    @Override
    public void init(Context context) {
        super.init(context);
        String conditionalField = context.getEntityAttribute(CONDITIONAL_ATTRIBUTE);
        Object condition = context.resolve(conditionalField);
        skip = ! toBoolean(condition);
    }
    
    private boolean toBoolean(Object obj) {
        if (obj == null)
            return false;
        else {
            String str = obj.toString();
            if (str.equals("1"))
                return true;
            else if (str.equals("0"))
                return false;
            else
                return BooleanUtils.toBoolean(str);
        }
    }

    /** 
     *  @return
     *   <ul>
     *   <li>empty row, if executed for the first time and CONDITIONAL_ATTRIBUTE was true
     *   <li>null, otherwise
     *   </ul>
     */
    @Override
    public Map<String, Object> nextRow() {
        if (skip)
            return null;
        else {
            skip = true;
            return new HashMap<String, Object>();
        }

    }
}
