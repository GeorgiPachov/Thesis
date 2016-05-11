package com.gpachov.masterthesis.threading;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadUtil {
	private static final ExecutorService threadPool = Executors.newCachedThreadPool();
	public static <T> Future<T> runAsync(Callable<T> callable){
		return threadPool.submit(callable);
	}
}
