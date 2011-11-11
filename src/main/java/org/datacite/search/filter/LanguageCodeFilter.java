package org.datacite.search.filter;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.datacite.search.util.LanguageUtils;

public final class LanguageCodeFilter extends TokenFilter {

    CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    
    boolean languageName;

    protected LanguageCodeFilter(TokenStream input, boolean languageName) {
        super(input);
        this.languageName = languageName;
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (input.incrementToken()) {
            String term = termAtt.toString();
            termAtt.setEmpty();
            String code = LanguageUtils.toIsoCode(term);
            if (languageName) {
                String name = LanguageUtils.toName(code);
                termAtt.append(name);
            } else {
                termAtt.append(code);
            }
            return true;
        } else {
            return false;
        }
    }
}
