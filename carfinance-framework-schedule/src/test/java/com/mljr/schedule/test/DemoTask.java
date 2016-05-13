package com.mljr.schedule.test;

import com.mljr.schedule.client.AbstractTaskService;
import com.mljr.schedule.client.ScheduleResponse;
import com.mljr.schedule.client.ScheduleStatusEnum;

public class DemoTask extends AbstractTaskService {
	
	@Override
	public ScheduleResponse execute() {
		System.out.println("测试任务 开始");
		ScheduleResponse scheduleResponse = new ScheduleResponse();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("OK，任务搞定");
		scheduleResponse.setStatus(ScheduleStatusEnum.SUCCESS);
		return scheduleResponse;
	}
	
}
