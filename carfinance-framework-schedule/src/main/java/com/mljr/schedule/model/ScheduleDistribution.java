package com.mljr.schedule.model;

import java.io.Serializable;
import java.util.List;
import com.mljr.schedule.enums.StrategyTypeEnum;

public class ScheduleDistribution implements Serializable {
	
	private static final long serialVersionUID = 7270108078148751037L;
	
	private StrategyTypeEnum strategyType;
	
	private List<TaskModel> taskModels;
	
	private List<GroupModel> groupModelList = null;
	
	public ScheduleDistribution() {
	}
	
	public ScheduleDistribution(StrategyTypeEnum strategyType) {
		this.strategyType = strategyType;
	}
	
	public StrategyTypeEnum getStrategyType() {
		return this.strategyType;
	}
	
	public void setStrategyType(StrategyTypeEnum strategyType) {
		this.strategyType = strategyType;
	}
	
	public List<GroupModel> getTaskDealStack() {
		return this.groupModelList == null ? this.strategyType.getDistribution().distribute(this.taskModels)
		        : this.groupModelList;
	}
	
	public List<TaskModel> getTaskModels() {
		return this.taskModels;
	}
	
	public void setTaskModels(List<TaskModel> taskModels) {
		this.taskModels = taskModels;
	}
}
