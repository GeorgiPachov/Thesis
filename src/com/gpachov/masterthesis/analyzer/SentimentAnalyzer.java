package com.gpachov.masterthesis.analyzer;

import java.util.List;
import java.util.Map;

import com.gpachov.masterthesis.classifiers.ClassificationResult;
import com.gpachov.masterthesis.classifiers.Classifier;
import com.gpachov.masterthesis.classifiers.DataClass;
import com.gpachov.masterthesis.progress.ProgressReporting;
import com.gpachov.masterthesis.utils.Pair;

public interface SentimentAnalyzer extends ProgressReporting {
    public abstract Map<String, Pair<DataClass, ClassificationResult>> analyze(Classifier classifier, Map<DataClass, List<String>> testData);
    public abstract float getMatchRate();
}