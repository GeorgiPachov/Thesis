package com.gpachov.masterthesis;

import com.gpachov.masterthesis.preprocessors.Preprocessor;
import com.gpachov.masterthesis.progress.ProgressReporting;

public interface IDataPreprocessor extends Preprocessor, ProgressReporting {
	public abstract float getMatchRate();

}