package com.gpachov.masterthesis;

import java.util.List;

public interface IDataProvider extends ClassifierData {

	public abstract List<String> getPositive();

	public abstract List<String> getNegative();

}