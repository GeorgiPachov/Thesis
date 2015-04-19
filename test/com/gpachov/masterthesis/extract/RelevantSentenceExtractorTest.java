package com.gpachov.masterthesis.extract;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RelevantSentenceExtractorTest {

	private RelevantSentenceExtractor extractor;

	@Before
	public void setup() {
		this.extractor = new RelevantSentenceExtractor();
	}

	@Test
	public void testSplitNormalSentences() {
		String testInput = "I am a huge fan of Chelsea. "
				+ "I want to have a season ticked."
				+ " Unfortunately, I don't! Do you know where I can buy one from ? "
				+ "It should'be that expensive, it's Chelsea";

		List<String> result = extractor.extractRelevant(testInput, "Chelsea");
		Assert.assertEquals(2, result.size());
	}

	@Test
	public void testSplitOfMultisentences() {
		String testInput = "Chelsea are in my blood"
				+ "\n\n I love Chelsea! "
				+ "\n\n\n Chelsea rock the PL "
				+ "\n\n\n\n Chelsea just beat Manchester United!";
		List<String> result = extractor.extractRelevant(testInput, "Chelsea");
		Assert.assertEquals(4, result.size());
	}
}
