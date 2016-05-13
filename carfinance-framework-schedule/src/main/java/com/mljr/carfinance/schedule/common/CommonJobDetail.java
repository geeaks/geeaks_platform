package com.mljr.carfinance.schedule.common;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import com.mljr.carfinance.schedule.model.TaskModel;
import com.mljr.carfinance.schedule.service.IScheduleService;

public class CommonJobDetail extends JobDetailImpl implements BeanNameAware, Job {
	
	private static final long serialVersionUID = 104736465885209127L;
	
	private static Logger logger = LoggerFactory.getLogger(CommonJobDetail.class);
	
	private String beanName;
	
	public CommonJobDetail() {
		super.setJobClass(getClass());
	}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println(
		        new Date() + ":  job begin...  group:" + context.getJobDetail().getJobDataMap().get("GROUP_KEY")
		                + "   uuid:" + context.getJobDetail().getJobDataMap().get("UUID_KEY") + " taskName:"
		                + context.getJobDetail().getJobDataMap().get("TASK_TAG_KEY"));
		logger.info(new Date() + ":  job begin...  group:" + context.getJobDetail().getJobDataMap().get("GROUP_KEY")
		        + "   uuid:" + context.getJobDetail().getJobDataMap().get("UUID_KEY") + " taskName:"
		        + context.getJobDetail().getJobDataMap().get("TASK_TAG_KEY"));
		String group = String.valueOf(context.getJobDetail().getJobDataMap().get("GROUP_KEY"));
		String UUID = String.valueOf(context.getJobDetail().getJobDataMap().get("UUID_KEY"));
		String taskTag = String.valueOf(context.getJobDetail().getJobDataMap().get("TASK_TAG_KEY"));
		IScheduleService scheduleService = (IScheduleService) context.getJobDetail().getJobDataMap()
		        .get("SCHEDULE_SERVICE_KEY");
		TaskModel taskModel = new TaskModel();
		taskModel.setGroup(group);
		taskModel.setUuid(UUID);
		taskModel.setTaskTag(taskTag);
		scheduleService.sendMsg(taskModel);
	}
	
	public void setBeanName(String name) {
		this.beanName = name;
		super.setName(name);
		super.setGroup("DEFAULT");
	}
	
	public String getBeanName() {
		return this.beanName;
	}
	
	public void setGroup(String group) {
		super.getJobDataMap().put("GROUP_KEY", group);
	}
	
	public void setUuid(String uuid) {
		super.getJobDataMap().put("UUID_KEY", uuid);
	}
	
	public void setTaskTag(String taskTag) {
		super.getJobDataMap().put("TASK_TAG_KEY", taskTag);
	}
	
	public void setScheduleService(IScheduleService scheduleService) {
		super.getJobDataMap().put("SCHEDULE_SERVICE_KEY", scheduleService);
	}
}
