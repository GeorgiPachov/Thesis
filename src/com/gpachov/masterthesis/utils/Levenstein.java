package com.gpachov.masterthesis.utils;

public class Levenstein {
	private static final int MAX_LEVENSTEIN_DEPTH = 1;

	public static final int getLevensteinDistance(String a, String b) {
		return lavenstein(a, b, MAX_LEVENSTEIN_DEPTH);
	}

	private static final int lavenstein(String a, String b, int maxLevensteinDepth) {
		if (Math.abs(a.length() - b.length()) > maxLevensteinDepth){
			return 10_000;
		}
		short[][] d = new short[a.length() + 1][b.length() + 1];
		for (short i = 0; i < d.length; i++) {
			d[i][0] = i;
		}

		for (short j = 0; j < d[0].length; j++) {
			d[0][j] = j;
		}

		for (short i = 1; i < d.length; i++) {
			for (short j = 1; j < d[0].length; j++) {
				if (a.charAt(i - 1) == b.charAt(j - 1)) {
					d[i][j] = d[i - 1][j - 1];
				} else {
					d[i][j] = (short) (Math.min(d[i - 1][j], Math.min(d[i][j - 1], d[i - 1][j - 1])) + 1);
				}
			}
		}
		return d[a.length()][b.length()];
	}
}
