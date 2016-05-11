package com.gpachov.masterthesis.preprocessors;

import java.util.ArrayList;
import java.util.List;

public class DefaultPreprocessor implements Preprocessor {
	@SuppressWarnings("serial")
	private static final List<Preprocessor> preprocessors = new ArrayList<Preprocessor>() {
		{
			add(new TrimmingPreprocessor());
			add(new TagStrippingPreprocessor());
			add(new CompressSpacesPreprocessor());
//			add(new RemovingNonWordsPreprocessor());
//			add(new SkipWordsPreprocessor());
//			add(new WordCompactingPreprocessor());
		}
	};

	@Override
	public String applyPreprocessing(String opinion) {
		for (Preprocessor preprocessor : preprocessors){
			opinion = preprocessor.applyPreprocessing(opinion);
		}
		return opinion;
	}
}
