package org.datacite.search.test;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.solr.search.QParser;

public class DummyQParser extends QParser {
    public DummyQParser() {
        super(null, null, null, null);
    }

    @Override
    public Query parse() throws ParseException {
        return new DummyQuery(getString());
    }

}