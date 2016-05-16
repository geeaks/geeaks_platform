package com.mljr.carfinance.schedule.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mljr.carfinance.log.util.LocalIPUtils;
import com.mljr.carfinance.schedule.client.TaskService;
import com.mljr.carfinance.schedule.enums.TaskRunningStatusEntum;
import com.mljr.carfinance.schedule.model.TaskModel;

public class ScheduleClient {
	
	private static Logger logger = LoggerFactory.getLogger(ScheduleClient.class);
	
	@Autowired
	private IScheduleService scheduleService;
	
	private String group;
	
	private List<TaskService> tasks;
	
	private UUID uuid;
	
	private Executor executor = Executors.newSingleThreadExecutor();
	
	private List<TaskModel> taskModelList = Lists.newArrayList();
	
	private Map<String, TaskService> taskMap = Maps.newHashMap();
	
	public void init() {
		this.uuid = UUID.randomUUID();
		if ((tasks != null) && (!tasks.isEmpty())) {
			new TaskExpireTimerThread().start();
			for (TaskService task : tasks) {
				TaskModel t = new TaskModel();
				t.setGroup(group);
				t.setVersion(task.getVersion());
				t.setName(task.getClass().getName());
				t.setUuid(uuid.toString());
				t.setIp(LocalIPUtils.getIp4Single());
				t.setStatus(TaskRunningStatusEntum.INIT.toString());
				taskModelList.add(t);
				taskMap.put(task.getTaskTag(), task);
			}
			scheduleService.registerSchedule(taskModelList);
			executor.execute(new Runnable() {
				public void run() {
					scheduleService.subscribe(group, uuid.toString(), taskMap);
				}
			});
		}
	}
	
	public void updateExpireTime(List<TaskModel> taskModels) {
		if ((tasks != null) && (!tasks.isEmpty()))
			scheduleService.updateExpireTime(taskModels);
	}
	
	public void setTasks(List<TaskService> tasks) {
		this.tasks = tasks;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	class TaskExpireTimerThread extends Thread {
		
		TaskExpireTimerThread() {
			setDaemon(true);
		}
		
		public void run() {
			if ((tasks != null) && (!tasks.isEmpty()))
				while (true)
					try {
						Thread.sleep(3000L);
						scheduleService.checkChannelAlive(group, uuid.toString(), taskMap);
						ScheduleClient.logger.debug("update expire time ...." + new Date());
						scheduleService.updateExpireTime(taskModelList);
					} catch (Throwable t) {
						ScheduleClient.logger.error("TaskExpireTimerThread exception", t);
					}
		}
	}
}
