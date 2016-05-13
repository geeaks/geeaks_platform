package com.mljr.carfinance.schedule.client;

/**
 * @Description: 任务调度结果实体
 * @ClassName: ScheduleResponse
 * @author gaoxiang
 * @date 2016年5月12日 下午4:41:33
 */ 
public class ScheduleResponse {
	
	/**
	 * @Fields status : 调度结果
	 */ 
	private ScheduleStatusEnum status;
	
	/**
	 * @Fields errorCode : 错误码
	 */ 
	private String errorCode;
	
	/**
	 * @Fields errorMsg : 错误信息
	 */ 
	private String errorMsg;
	
	/**
	 * @Fields taskService : 任务
	 */ 
	private String taskService;
	
	public ScheduleStatusEnum getStatus() {
		return this.status;
	}
	
	public void setStatus(ScheduleStatusEnum status) {
		this.status = status;
	}
	
	public String getErrorCode() {
		return this.errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorMsg() {
		return this.errorMsg;
	}
	
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public String getTaskService() {
		return this.taskService;
	}
	
	public void setTaskService(String taskService) {
		this.taskService = taskService;
	}
}
