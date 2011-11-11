package org.datacite.search.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LanguageUtilsTest {
    
    //make cobertura happy
    @Test
    public void test() {
        new LanguageUtils();
    }

    @Test
    public void testToIsoCode_TwoLetter() {
        assertEquals("en", LanguageUtils.toIsoCode("en"));
    }

    @Test
    public void testToIsoCode_ThreeLetterToTwoLetter() {
        assertEquals("de", LanguageUtils.toIsoCode("deu"));
        assertEquals("en", LanguageUtils.toIsoCode("eng"));
        assertEquals("zh", LanguageUtils.toIsoCode("zho"));
    }

    @Test
    public void testToIsoCode_ThreeLetterOnly() {
        assertEquals("tlh", LanguageUtils.toIsoCode("tlh")); // Klingon
    }

    @Test
    public void testToIsoCode_CountryCode() {
        assertEquals("en", LanguageUtils.toIsoCode("en_GB"));
    }

    @Test
    public void testToIsoCode_ThreeLetterAlternatives() {
        assertEquals("de", LanguageUtils.toIsoCode("ger"));
        assertEquals("zh", LanguageUtils.toIsoCode("chi"));
    }

    @Test
    public void testToIsoCode_Fullname() {
        assertEquals("de", LanguageUtils.toIsoCode("deutsch"));
        assertEquals("de", LanguageUtils.toIsoCode("German"));
        assertEquals("en", LanguageUtils.toIsoCode("english"));
    }

    @Test
    public void testToIsoCode_UnknownFullname() {
        assertEquals("foobar", LanguageUtils.toIsoCode("foobar"));
    }
    
    @Test
    public void testToName() {
        assertEquals("German", LanguageUtils.toName("de"));
        assertEquals("foobar", LanguageUtils.toName("foobar"));
    }
}
