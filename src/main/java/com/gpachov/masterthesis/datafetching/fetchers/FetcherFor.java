package com.gpachov.masterthesis.datafetching.fetchers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.gpachov.masterthesis.data.Source;

/**
 * @author georgi
 * Used by Fetchers to point out what data source they should be used for
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FetcherFor {
	Class<? extends Source> value();
}
