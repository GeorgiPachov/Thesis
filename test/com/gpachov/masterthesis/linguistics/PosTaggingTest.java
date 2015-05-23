package com.gpachov.masterthesis.linguistics;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gpachov.masterthesis.linguistics.sentencemodel.PosToken;
import com.gpachov.masterthesis.linguistics.sentencemodel.PosTokenizer;
import com.gpachov.masterthesis.linguistics.sentencemodel.SentenceModel;

public class PosTaggingTest {

    @Test
    public void test() {
	String input = "I am majestically stupid";
	SentenceModel sentence = new SentenceModel(input);
	for (PosToken posToken : sentence){
	    assertNotNull(posToken);
	}
	
    }

}
