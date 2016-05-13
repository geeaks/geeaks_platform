package com.mljr.schedule.client;

public interface TaskService {
	
	public abstract ScheduleResponse deal();
	
	public abstract String getVersion();
	
	public abstract String getTaskTag();
}
