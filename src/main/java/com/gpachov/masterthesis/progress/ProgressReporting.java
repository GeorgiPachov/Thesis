package com.gpachov.masterthesis.progress;

public interface ProgressReporting {
	ProgressReport DEFAULT_PROGRESS_REPORT = createProgressReport();

	default ProgressReport getProgressReport(){
		return DEFAULT_PROGRESS_REPORT;
	}

	static ProgressReport createProgressReport(){
		final CompositeProgressReport progressReport = new CompositeProgressReport();
		progressReport.addSubscriber(new LoggingProgressReport());
		return progressReport;
	}
}
