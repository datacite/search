package org.datacite.search.handler.component;

import java.io.IOException;

import org.apache.lucene.queryParser.ParseException;
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
        if (qstr.equals("*")) {
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSource() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSourceId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getVersion() {
        // TODO Auto-generated method stub
        return null;
    }
}
