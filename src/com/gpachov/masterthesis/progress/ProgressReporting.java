package com.gpachov.masterthesis.progress;

public interface ProgressReporting {
	ProgressReport PROGRESS_REPORT = createProgressReport();

	default ProgressReport getProgressReport(){
		return PROGRESS_REPORT;
	}

	static ProgressReport createProgressReport(){
		final CompositeProgressReport progressReport = new CompositeProgressReport();
		progressReport.addSubscriber(new LoggingProgressReport());
		return progressReport;
	}
}
