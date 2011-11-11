package org.datacite.search.filter;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.datacite.search.util.LanguageUtils;

public final class LanguageCodeFilter extends TokenFilter {

    CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    protected LanguageCodeFilter(TokenStream input) {
        super(input);
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (input.incrementToken()) {
            String term = termAtt.toString();
            termAtt.setEmpty();
            termAtt.append(LanguageUtils.toIsoCode(term));
            return true;
        } else {
            return false;
        }
    }
}
