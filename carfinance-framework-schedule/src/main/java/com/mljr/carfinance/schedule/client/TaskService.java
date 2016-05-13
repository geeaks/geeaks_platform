package com.mljr.carfinance.schedule.client;

public interface TaskService {
	
	public abstract ScheduleResponse deal();
	
	public abstract String getVersion();
	
	public abstract String getTaskTag();
}
