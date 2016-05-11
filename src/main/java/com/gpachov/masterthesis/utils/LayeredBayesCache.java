package com.gpachov.masterthesis.utils;

public class LayeredBayesCache extends BidimensionalHashMap<Integer, String, Integer>{
	public int getCacheSize(Integer level){
		return super.getRow(level).size();
		
	}
}
