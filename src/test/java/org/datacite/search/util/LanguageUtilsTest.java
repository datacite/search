package org.datacite.search.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LanguageUtilsTest {

    @Test
    public void testTwoLetter() {
        assertEquals("en", LanguageUtils.toIsoCode("en"));
    }

    @Test
    public void testThreeLetterToTwoLetter() {
        assertEquals("de", LanguageUtils.toIsoCode("deu"));
        assertEquals("en", LanguageUtils.toIsoCode("eng"));
        assertEquals("zh", LanguageUtils.toIsoCode("zho"));
    }

    @Test
    public void testThreeLetterOnly() {
        assertEquals("tlh", LanguageUtils.toIsoCode("tlh")); // Klingon
    }

    @Test
    public void testCountryCode() {
        assertEquals("en", LanguageUtils.toIsoCode("en_GB"));
    }

    @Test
    public void testThreeLetterAlternatives() {
        assertEquals("de", LanguageUtils.toIsoCode("ger"));
        assertEquals("zh", LanguageUtils.toIsoCode("chi"));
    }

    @Test
    public void testFullname() {
        assertEquals("de", LanguageUtils.toIsoCode("deutsch"));
        assertEquals("de", LanguageUtils.toIsoCode("German"));
        assertEquals("en", LanguageUtils.toIsoCode("english"));
    }

    @Test
    public void testUnknownFullname() {
        assertEquals("foobar", LanguageUtils.toIsoCode("foobar"));
    }
}
