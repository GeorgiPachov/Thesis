package com.gpachov.masterthesis.lexicon;

public class WordLexiconInfo {
    private String word;
    private float score;
    public WordLexiconInfo(String word, float score){
	this.setWord(word);
	this.score = score;
    }
    public float getSentimentScore(){
	return score;
    }
    public String getWord() {
	return word;
    }
    private void setWord(String word) {
	this.word = word;
    }
}
