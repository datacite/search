package org.datacite.search.handler.dataimport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.solr.handler.dataimport.Context;
import org.apache.solr.handler.dataimport.DataImporter;
import org.apache.solr.handler.dataimport.Transformer;

// import com.fasterxml.jackson.core.JsonParser;
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.DeserializationContext;
// import com.fasterxml.jackson.databind.JsonDeserializer;
// import com.fasterxml.jackson.databind.JsonNode;

public class LicenseTransformer extends Transformer {

    public static final String LICENSE_ATTRIBUTE = "license";

    // ObjectMapper objectMapper = new ObjectMapper();
    // String jsonInput = "{\"http://creativecommons.org/licenses/by/3.0/legalcode\": \"CC-BY-3.0 - Creative Commons Attribution 3.0\", \"http://creativecommons.org/licenses/by/4.0/legalcode\": \"CC-BY-4.0 - Creative Commons Attribution 4.0\"}";
    // JsonNode jsonNode = objectMapper.readTree(jsonInput);

    @Override
    public Object transformRow(Map<String, Object> row, Context context) {
        List<Map<String, String>> fields = context.getAllEntityFields();
        for (Map<String, String> field : fields) {
            String license = field.get(LICENSE_ATTRIBUTE);
            if ("true".equals(license)) {
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
            String str = value.toString().trim();

            // if Creative Commons license
            if (str.contains("creativecommons.org")) {
              String str1 = str.replace("https://", "http://");
              String str2 = str1.replace("/us/", "/");
              String str3 = (str2.endsWith("/") ? str2 : str2 + "/");
              String str4 = (str3.endsWith("legalcode") ? str3 : str3 + "legalcode");
              return str4;
            } else {
              return str;
            }
        } else {
            return null;
        }
    }
}
