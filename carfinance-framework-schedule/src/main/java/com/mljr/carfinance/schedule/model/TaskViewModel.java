package com.mljr.carfinance.schedule.model;

import java.io.Serializable;
import java.util.List;
import com.google.common.collect.Lists;

public class TaskViewModel implements Serializable {
	
	private static final long serialVersionUID = -5428799498913956420L;

	private String taskName;
	
	private String group;
	
	private String cornExpress;
	
	private String type;
	
	List<TaskModel> nodes = Lists.newArrayList();
	
	public TaskViewModel() {
	}
	
	public TaskViewModel(String group, String taskName) {
		this.group = group;
		this.taskName = taskName;
	}
	
	public String getTaskName() {
		return this.taskName;
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public String getCornExpress() {
		return this.cornExpress;
	}
	
	public void setCornExpress(String cornExpress) {
		this.cornExpress = cornExpress;
	}
	
	public List<TaskModel> getNodes() {
		return this.nodes;
	}
	
	public void setNodes(List<TaskModel> nodes) {
		this.nodes = nodes;
	}
	
	public void addNode(TaskModel node) {
		this.nodes.add(node);
	}
	
	public int hashCode() {
		return getTaskName().hashCode();
	}
	
	public boolean equals(Object target) {
		if ((target instanceof TaskViewModel)) {
			TaskViewModel tv = (TaskViewModel) target;
			return (getTaskName().equals(tv.getTaskName())) && (getGroup().equals(tv.getGroup()));
		}
		return false;
	}
	
	public String getGroup() {
		return this.group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
