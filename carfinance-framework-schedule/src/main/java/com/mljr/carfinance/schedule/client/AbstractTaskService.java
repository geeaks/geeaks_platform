package com.mljr.carfinance.schedule.client;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractTaskService implements TaskService {
	
	private String version;
	
	public ScheduleResponse deal() {
		ScheduleResponse response = execute();
		response.setTaskService(getClass().getName());
		return response;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getTaskTag() {
		return getClass().getName() + StringUtils.defaultIfEmpty(getVersion(), "");
	}
	
	public abstract ScheduleResponse execute();
}
