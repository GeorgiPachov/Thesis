package com.gpachov.masterthesis.linguistics;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class PhraseExtractorTest {

    @Test
    public void test() {
	String input = "This was amazing great superb night";
	List<Phrase> phrases = PhraseExtractor.extractPhrases(input);
	assertTrue(phrases.size() > 0);
	for (Phrase phrase : phrases){
	    System.out.println(phrase);
	}
    }

}
