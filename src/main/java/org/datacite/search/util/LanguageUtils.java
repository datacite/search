package org.datacite.search.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.ibm.icu.util.ULocale;

public class LanguageUtils {

    private static Map<String, String> languageCodeByName;
    private static Map<String, String> languageCodeByISO6392B;

    private static void fillLanguageNameMap() {
        languageCodeByName = new HashMap<String, String>();
        for (String code : ULocale.getISOLanguages()) {
            String nativeLanguageName = ULocale.getDisplayLanguage(code, code);
            String englishLanguageName = ULocale.getDisplayLanguage(code, "en");
            languageCodeByName.put(nativeLanguageName.toLowerCase(), code);
            languageCodeByName.put(englishLanguageName.toLowerCase(), code);
        }
    }

    private static void fillLanguageCodeByISO639TMap() {
        // http://www.loc.gov/standards/iso639-2/ascii_8bits.html
        Map<String, String> map = new HashMap<String, String>();
        map.put("alb", "sq");
        map.put("arm", "hy");
        map.put("baq", "eu");
        map.put("bur", "my");
        map.put("chi", "zh");
        map.put("cze", "cs");
        map.put("dut", "nl");
        map.put("fre", "fr");
        map.put("geo", "ka");
        map.put("ger", "de");
        map.put("gre", "el");
        map.put("ice", "is");
        map.put("mac", "mk");
        map.put("mao", "mi");
        map.put("may", "ms");
        map.put("per", "fa");
        map.put("rum", "ro");
        map.put("slo", "sk");
        map.put("tib", "bo");
        map.put("wel", "cy");
        languageCodeByISO6392B = map;
    }

    static {
        fillLanguageNameMap();
        fillLanguageCodeByISO639TMap();
    }

    public static String toIsoCode(String lang) {
        lang = ULocale.getLanguage(lang);
        lang = lang.toLowerCase();
        if (lang.length() > 3 && languageCodeByName.containsKey(lang))
            lang = languageCodeByName.get(lang);
        if (lang.length() == 3 && languageCodeByISO6392B.containsKey(lang))
            lang = languageCodeByISO6392B.get(lang);
        return lang;
    }
    
    public static String toName(String code) {
        return ULocale.getDisplayLanguage(code, "en");
    }
}
