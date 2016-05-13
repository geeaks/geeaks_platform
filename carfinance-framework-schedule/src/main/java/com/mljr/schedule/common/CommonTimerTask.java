package com.mljr.schedule.common;

import java.util.TimerTask;
import com.mljr.schedule.model.TaskModel;
import com.mljr.schedule.service.IScheduleService;

public class CommonTimerTask extends TimerTask {
	
	private IScheduleService scheduleService;
	
	private TaskModel taskModel;
	
	public CommonTimerTask(TaskModel taskModel, IScheduleService scheduleService) {
		this.taskModel = taskModel;
		this.scheduleService = scheduleService;
	}
	
	public void run() {
		this.scheduleService.sendMsg(this.taskModel);
	}
	
	public IScheduleService getScheduleService() {
		return this.scheduleService;
	}
	
	public TaskModel getTaskModel() {
		return this.taskModel;
	}
}
