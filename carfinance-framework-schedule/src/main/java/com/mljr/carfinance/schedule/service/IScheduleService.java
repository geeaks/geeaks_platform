package com.mljr.carfinance.schedule.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.quartz.SchedulerException;
import com.mljr.carfinance.schedule.client.TaskService;
import com.mljr.carfinance.schedule.enums.StrategyTypeEnum;
import com.mljr.carfinance.schedule.enums.TaskRunningStatusEntum;
import com.mljr.carfinance.schedule.model.ExpressModel;
import com.mljr.carfinance.schedule.model.ScheduleDistribution;
import com.mljr.carfinance.schedule.model.TaskModel;

public interface IScheduleService {
	
	void registerSchedule(List<TaskModel> paramList);
	
	ExpressModel getExpressModelByTasktag(String paramString);
	
	void sendMsg(TaskModel paramTaskModel);
	
	ScheduleDistribution createDistribution(StrategyTypeEnum paramStrategyTypeEnum);
	
	void subscribe(String paramString1, String paramString2, Map<String, TaskService> paramMap);
	
	void startSchedule(ScheduleDistribution paramScheduleDistribution);
	
	void stopSchedule() throws SchedulerException;
	
	String queryCronExpression(String paramString);
	
	void updateExpireTime(List<TaskModel> paramList);
	
	List<TaskModel> queryTaskModels();
	
	List<TaskModel> queryTaskModelsWithExp();
	
	String getHashSum(List<TaskModel> paramList);
	
	void saveExpression(String paramString1, String paramString2);
	
	TaskModel getTaskByTag(String paramString);
	
	void updateTaskModelStatus(String paramString, TaskRunningStatusEntum paramTaskRunningStatusEntum);
	
	List<TaskModel> queryTaskStatusModels();
	
	Set<String> queryKeysByPattern(String paramString);
	
	void checkChannelAlive(String paramString1, String paramString2, Map<String, TaskService> paramMap);
}
