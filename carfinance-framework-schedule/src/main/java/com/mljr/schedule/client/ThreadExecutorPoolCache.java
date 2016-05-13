package com.mljr.schedule.client;

import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExecutorPoolCache extends Hashtable<String, ExecutorService> {
	
	private static final long serialVersionUID = 2671592923690585265L;
	
	public synchronized ExecutorService getSingleExecutor(String key) {
		if (get(key) == null) {
			put(key, Executors.newSingleThreadExecutor());
		}
		return (ExecutorService) get(key);
	}
}
