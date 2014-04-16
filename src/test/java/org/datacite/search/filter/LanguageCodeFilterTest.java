package org.datacite.search.filter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenStream;
import org.junit.Test;

public class LanguageCodeFilterTest extends BaseTokenStreamTestCase {

    LanguageCodeFilterFactory filterFactory = new LanguageCodeFilterFactory();

    @Test
    public void test() {
        assertFilter("en", "en");
        assertFilter("GER", "de");
        assertFilter("deu", "de");
        assertFilter("chinese", "zh");
        assertFilter("foobar", "foobar");
    }

    @Test
    public void testWithNames() {
        initFilterWithNames();
        assertFilter("en", "English");
        assertFilter("deu", "German");
        assertFilter("chinese", "Chinese");
        assertFilter("ancient Greek", "Ancient Greek");
        assertFilter("foobar", "foobar");
    }

    private void initFilter(boolean languageName) {
        Map<String, String> args = new HashMap<String, String>();
        args.put(LanguageCodeFilterFactory.LANGUAGE_NAME_ATTRIBUTE, BooleanUtils.toStringTrueFalse(languageName));
        filterFactory.init(args);
    }

    private void initFilterWithNames() {
        initFilter(true);
    }

    private void assertFilter(String in, String... out) {
        Reader reader = new StringReader(in);
        TokenStream token = new KeywordTokenizer(reader);
        TokenStream filtered = filterFactory.create(token);
        try {
            assertTokenStreamContents(filtered, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
