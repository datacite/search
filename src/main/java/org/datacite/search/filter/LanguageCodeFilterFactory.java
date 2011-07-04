package org.datacite.search.filter;

import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenFilterFactory;

public class LanguageCodeFilterFactory extends BaseTokenFilterFactory {

    public TokenStream create(TokenStream input) {
        return new LanguageCodeFilter(input);
    }

}
