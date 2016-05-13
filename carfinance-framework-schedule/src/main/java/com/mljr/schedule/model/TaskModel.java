package com.mljr.schedule.model;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;
import com.mljr.schedule.util.Constant;

public class TaskModel implements Serializable {
	
	private static final long serialVersionUID = 6949613533527162443L;
	
	private String group;
	
	private String name;
	
	private String uuid;
	
	private String ip;
	
	private String version;
	
	private int count = 0;
	
	private String status;
	
	private String dealTime;
	
	public String taskTag;
	
	public String getTaskTag() {
		return taskTag != null ? taskTag : getName()+getVersion() ;
	}
	
	public void setTaskTag(String taskTag) {
		this.taskTag = taskTag;
	}
	
	public String getGroup() {
		return this.group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUuid() {
		return this.uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int hashCode() {
		return this.name.hashCode() + this.uuid.hashCode() + this.group.hashCode();
	}
	
	public String toString() {
		return "taskName:" + this.name + "  uuid:" + this.uuid + "  group:" + this.group;
	}
	
	public boolean equals(Object target) {
		if ((target instanceof TaskModel)) {
			TaskModel newT = (TaskModel) target;
			return (getName().equals(newT.getName())) && (getUuid().equals(newT.getUuid()))
			        && (getGroup().equals(newT.getGroup()));
		}
		return false;
	}
	
	public String getSchemaKey() {
		return Constant.SCHEMA_KEY_PREFIX + getUuid() + getName() + getVersion();
	}
	
	public String getIp() {
		return this.ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getDealTime() {
		return this.dealTime;
	}
	
	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}
	
	public String getVersion() {
		if (StringUtils.isEmpty(this.version))
			return "";
		return this.version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
}
