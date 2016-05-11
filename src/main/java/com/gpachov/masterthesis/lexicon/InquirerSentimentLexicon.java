package com.gpachov.masterthesis.lexicon;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InquirerSentimentLexicon extends AbstractSentimentLexicon {
    private static final URL inquirerLexicon = BasicSentimentLexicon.class.getClassLoader().getResource("main/resources/inquirerbasic.csv");
    public InquirerSentimentLexicon() {
	try {
	    Files.readAllLines(Paths.get(inquirerLexicon.toURI())).forEach(l -> {
		if (!l.startsWith("#")) {
		    String[] tokens = l.split(",", -1);
		    String word = tokens[0];
		    boolean isPositive = !"".equals(tokens[1]);
		    boolean isNegative = !"".equals(tokens[2]);
		    boolean isStrong = !"".equals(tokens[3]);
		    boolean isWeak = !"".equals(tokens[4]);

		    float powerMultiplier = isStrong ? 2 : 1;
		    float positivityMultiplier = isPositive ? 1 : (isNegative ? -1 : 0);
		    float score = powerMultiplier * positivityMultiplier;
		    if (score != 0.0f) {
			lexicon.put(word.toLowerCase(), score);
		    }
		}
	    });
	} catch (IOException | URISyntaxException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	System.out.println(new InquirerSentimentLexicon().lexicon.toString());
    }
}
