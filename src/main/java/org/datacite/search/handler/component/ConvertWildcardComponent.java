package org.datacite.search.handler.component;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.handler.component.SearchComponent;
import org.apache.solr.search.QParser;

public class ConvertWildcardComponent extends SearchComponent {

    @Override
    public void prepare(ResponseBuilder rb) throws IOException {
    }

    @Override
    public void process(ResponseBuilder rb) throws IOException {
        String qstr = rb.getQueryString();
        if (qstr != null && qstr.equals("*")) {
            setQuery(rb, "*:*");
        }
    }

    private void setQuery(ResponseBuilder rb, String qstr) {
        try {
            QParser qParser = rb.getQparser();
            qParser.setString(qstr);
            Query query = qParser.parse();
            rb.setQueryString(query.toString());
            rb.setQuery(query);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getDescription() {
        return "Component to convert query '*' to '*:*'";
    }

    @Override
    public String getSource() {
        return "https://github.com/datacite/search/blob/master/src/main/java/org/datacite/search/handler/component/ConvertWildcardComponent.java";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
