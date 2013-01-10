package org.datacite.search.filter;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

public class LanguageCodeFilterFactory extends TokenFilterFactory {
    
    public final static String LANGUAGE_NAME_ATTRIBUTE = "languageName"; 
    
    boolean languageName;

    @Override
    public void init(Map<String, String> args) {
        super.init(args);
        languageName = getBoolean(LANGUAGE_NAME_ATTRIBUTE, false);
    }

    public TokenStream create(TokenStream input) {
        return new LanguageCodeFilter(input, languageName);
    }

}
