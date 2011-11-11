package org.datacite.search.filter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.KeywordTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.analysis.BaseTokenTestCase;
import org.junit.Ignore;
import org.junit.Test;

public class LanguageCodeFilterTest extends BaseTokenTestCase {

    LanguageCodeFilterFactory filterFactory = new LanguageCodeFilterFactory();

    @Test
    public void testTwoLetter() {
        assertFilter("en", "en");
    }

    @Test
    public void testThreeLetterToTwoLetter() {
        assertFilter("deu", "de");
        assertFilter("eng", "en");
        assertFilter("zho", "zh");
    }

    @Test
    public void testThreeLetterOnly() {
        assertFilter("tlh", "tlh"); // Klingon
    }
    
    @Test
    public void testCountryCode() {
        assertFilter("en_GB", "en");
    }
    
    @Test
    public void testThreeLetterAlternatives() {
        assertFilter("ger", "de");
        assertFilter("chi", "zh");
    }
    
    @Test
    public void testFullname() {
        assertFilter("deutsch", "de");
        assertFilter("german", "de");
        assertFilter("english", "en");
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
