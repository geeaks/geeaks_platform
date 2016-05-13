package com.mljr.carfinance.schedule.common;

import java.text.ParseException;
import org.quartz.impl.triggers.CronTriggerImpl;

public class CommonCronTrigger extends CronTriggerImpl {
	
	private static final long serialVersionUID = -7752529426616863477L;
	
	private CommonJobDetail jobDetail;
	
	public void setCronExpression(String cronExpression) {
		try {
			super.setCronExpression(cronExpression);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setJobDetail(CommonJobDetail jobDetail) {
		this.jobDetail = jobDetail;
		super.setName(jobDetail.getName() + "_TRIGGER");
		super.getJobDataMap().put("jobDetail", jobDetail);
	}
	
	public CommonJobDetail getJobDetail() {
		return this.jobDetail;
	}
}
