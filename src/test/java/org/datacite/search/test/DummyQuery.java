package org.datacite.search.test;

import org.apache.lucene.search.Query;

public class DummyQuery extends Query {

    String qstr;

    public DummyQuery(String qstr) {
        this.qstr = qstr;
    }

    @Override
    public String toString(String field) {
        return qstr;
    }

}