package org.datacite.search.filter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.KeywordTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenTestCase;
import org.junit.Test;

public class LanguageCodeFilterTest extends BaseTokenTestCase {

    LanguageCodeFilterFactory filterFactory = new LanguageCodeFilterFactory();

    @Test
    public void test() {
        assertFilter("en", "en");
        assertFilter("deu", "de");
        assertFilter("chinese", "zh");
        assertFilter("foobar", "foobar");
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
