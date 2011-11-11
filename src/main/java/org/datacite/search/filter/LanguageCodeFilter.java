package org.datacite.search.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.ibm.icu.util.ULocale;

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
            termAtt.append(toIsoCode(term));
            return true;
        } else {
            return false;
        }
    }
    
    private String toIsoCode(String lang) {
        lang = ULocale.getLanguage(lang);
        lang = lang.toLowerCase();
        if (lang.length() > 3 && languageCodeByName.containsKey(lang))
            lang = languageCodeByName.get(lang);
        return lang;
    }
    
    private static Map<String, String> languageCodeByName;
    
    private static void fillLanguageNameMap() {
        languageCodeByName = new HashMap<String, String>();
        for (String code : ULocale.getISOLanguages()) {
            String nativeLanguageName = ULocale.getDisplayLanguage(code, code);
            String englishLanguageName = ULocale.getDisplayLanguage(code, "en");
            languageCodeByName.put(nativeLanguageName.toLowerCase(), code);
            languageCodeByName.put(englishLanguageName.toLowerCase(), code);
        }
    }

    static {
        fillLanguageNameMap();
    }
}
