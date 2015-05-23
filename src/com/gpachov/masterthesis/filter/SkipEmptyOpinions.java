package com.gpachov.masterthesis.filter;

import java.util.function.Predicate;

public class SkipEmptyOpinions implements Predicate<String> {

    @Override
    public boolean test(String s) {
	return !s.trim().equals("");
    }

}
