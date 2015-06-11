package com.gpachov.masterthesis;

public class Constants {
    private static final int TEST_DATA_SET_MULTIPLIER = 1;
    public static final long OPINION_LIMIT_UNCLASSIFIED = TEST_DATA_SET_MULTIPLIER * 500;
    public static final int OPINION_LIMIT_PER_CLASS = TEST_DATA_SET_MULTIPLIER * 250;
    public static final float STRONG_MULTIPLIER = 3.0f;
    public static final boolean SYNONIM_CORRECTION = false;
    public static final boolean DERIVATIVE_CORRECTION = false;
    public static final boolean MULTIPLY_CORRECTION = false;
    public static final int NEGATION_RANGE = 3;
    public static final boolean NEGATION_FIX = true;
    public static boolean USE_STRONG_DATA_ONLY = false;
    
    public static boolean DEBUG = true;
    public static boolean DEBUG_CLASSIFIER = true;
    public static boolean DEBUG_ANALYZER = false;
}
