package com.mljr.carfinance.schedule.model;

import java.io.Serializable;
import java.util.List;
import com.google.common.collect.Lists;

public class GroupViewModel implements Serializable {
	
	private static final long serialVersionUID = -7405622250115019576L;

	private String group;
	
	List<TaskViewModel> tasks = Lists.newArrayList();
	
	public GroupViewModel(String group) {
		this.group = group;
	}
	
	public String getGroup() {
		return this.group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	public List<TaskViewModel> getTasks() {
		return this.tasks;
	}
	
	public void setTasks(List<TaskViewModel> tasks) {
		this.tasks = tasks;
	}
	
	public void addTask(TaskViewModel task) {
		this.tasks.add(task);
	}
	
	public int hashCode() {
		return getGroup().hashCode();
	}
	
	public boolean equals(Object target) {
		if ((target instanceof GroupViewModel)) {
			return getGroup().equals(((GroupViewModel) target).getGroup());
		}
		return false;
	}
}
