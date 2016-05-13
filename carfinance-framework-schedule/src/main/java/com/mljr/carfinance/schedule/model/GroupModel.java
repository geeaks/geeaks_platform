package com.mljr.carfinance.schedule.model;

import java.io.Serializable;
import java.util.List;
import com.google.common.collect.Lists;

public class GroupModel implements Serializable {
	
	private static final long serialVersionUID = -4810843667308815337L;
	
	private String groupName;
	
	private List<TaskModel> taskModels = Lists.newArrayList();
	
	public GroupModel() {
	}
	
	public GroupModel(String groupName) {
		this.groupName = groupName;
	}
	
	public String getGroupName() {
		return this.groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public List<TaskModel> getTaskModels() {
		return this.taskModels;
	}
	
	public void setTaskModels(List<TaskModel> taskModels) {
		this.taskModels = taskModels;
	}
	
	public void addTaskModel(TaskModel taskModel) {
		this.taskModels.add(taskModel);
	}
	
	public int hashCode() {
		return this.groupName.hashCode();
	}
	
	public boolean equals(Object obj) {
		if ((obj instanceof GroupModel)) {
			return getGroupName().equals(((GroupModel) obj).getGroupName());
		}
		return false;
	}
}
