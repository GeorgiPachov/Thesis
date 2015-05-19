package com.gpachov.masterthesis.linguistics;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gpachov.masterthesis.linguistics.sentencemodel.PosToken;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosTokenizer;
import com.gpachov.masterthesis.linguistics.sentencemodel.Sentence;

public class PosTaggingTest {

    @Test
    public void test() {
	String input = "I am majestically stupid";
	Sentence sentence = new Sentence(input);
	for (PosToken posToken : sentence){
	    assertNotNull(posToken);
	}
	
    }

}
