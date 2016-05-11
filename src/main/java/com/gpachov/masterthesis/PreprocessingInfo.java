package com.gpachov.masterthesis;

public class PreprocessingInfo {

	private long allWords;

	public PreprocessingInfo(long allWords) {
		this.setAllWords(allWords);
	}

	public long getAllWords() {
		return allWords;
	}

	private void setAllWords(long allWords) {
		this.allWords = allWords;
	}

	@Override
	public String toString() {
		return "PreprocessingInfo [allWords=" + allWords + "]";
	}
}
