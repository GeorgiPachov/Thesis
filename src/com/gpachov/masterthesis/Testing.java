package com.gpachov.masterthesis;

import java.util.List;

import com.gpachov.masterthesis.extract.LinguisticSentenceExtractor;
import com.gpachov.masterthesis.extract.RelevantSentenceExtractor;
import com.gpachov.masterthesis.extract.SentenceExtractor;
import com.gpachov.masterthesis.linguistics.sentencemodel.SentenceModel;

public class Testing {
    public static void main(String[] args) {
	String input = "hotel is very dated. room was a little nasty. two different colors of wallpaper seamed together. nasty old carpet. bed was ok. telephone wires were hanging out of the wall. air conditioner was older than i am. regular tv with regular cable. not like the hilton where you can rent movies and watch certain things. its satellite and it was constantly cutting out. bathroom had nasty old tile and grout. toilet was old everything was just bleh. seriously i gave it a solid 1/5";
	SentenceExtractor sentenceExtractor = new RelevantSentenceExtractor();
	List<String> extractRelevant = sentenceExtractor.extractRelevant(input, null);
	for (String s: extractRelevant){
	    System.out.println(extractRelevant.size());
	    	System.out.println(s);
	}
    }
}
