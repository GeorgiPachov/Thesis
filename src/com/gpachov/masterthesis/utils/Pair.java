package com.gpachov.masterthesis.utils;

public class  Pair <F,S> {
    private F first;
    private S second;

    public Pair(F first, S second){
	this.setFirst(first);
	this.setSecond(second);
    }

    public S getSecond() {
	return second;
    }

    public void setSecond(S second) {
	this.second = second;
    }

    public F getFirst() {
	return first;
    }

    public void setFirst(F first) {
	this.first = first;
    }

}