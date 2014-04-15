package org.datacite.search.handler.dataimport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.handler.dataimport.Context;
import org.apache.solr.handler.dataimport.DataImporter;
import org.apache.solr.handler.dataimport.Transformer;

public class TrimTransformer extends Transformer {
    
    public static final String TRIM_ATTRIBUTE = "trim";

    @Override
    public Object transformRow(Map<String, Object> row, Context context) {
        List<Map<String, String>> fields = context.getAllEntityFields();
        for (Map<String, String> field : fields) {
            String trim = field.get(TRIM_ATTRIBUTE);
            if ("true".equals(trim)) {
                String columnName = field.get(DataImporter.COLUMN);
                Object value = row.get(columnName);
                if (value != null) 
                    row.put(columnName, transform(value));
            }
        }
        return row;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object transform(Object value) {
        if (value instanceof List) {
            List list = (List) value;
            List newlist = new ArrayList(list.size());
            for (Object elem : list) 
                newlist.add(transform(elem));
            return newlist;
        } else if (value instanceof String) {
            return value.toString().trim();
        } else {
            return value;
        }
    }
}
