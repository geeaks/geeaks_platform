package com.mljr.carfinance.schedule.client;

import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.mljr.carfinance.schedule.enums.TaskRunningStatusEntum;
import com.mljr.carfinance.schedule.model.TaskModel;
import com.mljr.carfinance.schedule.service.IScheduleService;
import com.mljr.carfinance.schedule.util.Constant;
import redis.clients.jedis.JedisPubSub;

/**
 * @Description: 订阅消息消费
 * @ClassName: SubcribeListener
 * @author gaoxiang
 * @date 2016年4月28日 下午11:50:05
 */
public class SubcribeListener extends JedisPubSub {
	
	private static Logger logger = LoggerFactory.getLogger(SubcribeListener.class);
	
	// 取消订阅命令
	private final static String UNSUBCRIBE = "UNSUBCRIBE";
	
	private ThreadExecutorPoolCache threadPool = new ThreadExecutorPoolCache();
	
	private Map<String, TaskService> tasks;
	
	private IScheduleService scheduleService;
	
	private String UUID;
	
	public SubcribeListener(String UUID, Map<String, TaskService> tasks, IScheduleService scheduleService) {
		this.UUID = UUID;
		this.tasks = tasks;
		this.scheduleService = scheduleService;
	}
	
	// 取得订阅的消息后的处理
	@Override
	public void onMessage(String channel, String message) {
		try {
			if (StringUtils.isNotBlank(message)) {
				if (UNSUBCRIBE.equals(message)) {
					this.unsubscribe();
				}
				TaskModel task = JSONObject.parseObject(message, TaskModel.class);
				TaskService taskService = tasks.get(task.getTaskTag());
				if (taskService == null) {
					logger.error("执行任务不存在，taskTag:" + task.getTaskTag());
				} else {
					threadPool.getSingleExecutor(task.getTaskTag()).execute(new TaskDealThread(taskService));
				}
			}
		} catch (Throwable t) {
			logger.error("redis listener exception", t);
		}
	}
	
	// 取得按表达式的方式订阅的消息后的处理
	@Override
	public void onPMessage(String pattern, String channel, String message) {
		// TODO Auto-generated method stub
	}
	
	// 初始化订阅时候的处理
	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		// TODO Auto-generated method stub
	}
	
	/// 取消订阅时候的处理
	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		
	}
	
	// 取消按表达式的方式订阅时候的处理
	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		
	}
	
	// 初始化按表达式的方式订阅时候的处理
	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		
	}
	
	class TaskDealThread implements Runnable {
		
		private TaskService taskService;
		
		TaskDealThread(TaskService taskService) {
			this.taskService = taskService;
		}
		
		private String createSchemaKey() {
			return Constant.SCHEMA_KEY + UUID + this.taskService.getTaskTag();
		}
		
		@Override
		public void run() {
			try {
				scheduleService.updateTaskModelStatus(Constant.SCHEMA_KEY_PREFIX + createSchemaKey(), TaskRunningStatusEntum.RUNNING);
				ScheduleResponse response = this.taskService.deal();
				logger.info("taskService deal success! deal time is:{0} response:", response);
				scheduleService.updateTaskModelStatus(Constant.SCHEMA_KEY_PREFIX + createSchemaKey(), TaskRunningStatusEntum.COMPLETE);
			} catch (Throwable e) {
				scheduleService.updateTaskModelStatus(Constant.SCHEMA_KEY_PREFIX + createSchemaKey(), TaskRunningStatusEntum.ERROR);
				logger.error("调度任务执行发生异常 taskService:" + this.taskService.getClass(), e);
			}
		}
	}
}
