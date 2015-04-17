package com.gpachov.masterthesis.data;

import java.util.Date;

public interface RawDataEntry {
	Date getDate();
	String getText();
	SourceDetails getSourceDetails();
	
	/**
	 *  [0-1]
	 * @return
	 */
	default float getPopularity(){
		return 1;
	}
}
