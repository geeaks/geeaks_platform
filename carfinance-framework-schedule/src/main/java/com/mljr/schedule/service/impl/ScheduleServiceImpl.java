package com.mljr.schedule.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.UUID;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.mljr.schedule.client.SubcribeListener;
import com.mljr.schedule.client.TaskService;
import com.mljr.schedule.common.CommonCronTrigger;
import com.mljr.schedule.common.CommonJobDetail;
import com.mljr.schedule.common.CommonTimerTask;
import com.mljr.schedule.enums.ScheduleTypeEnum;
import com.mljr.schedule.enums.StrategyTypeEnum;
import com.mljr.schedule.enums.TaskRunningStatusEntum;
import com.mljr.schedule.model.ExpressModel;
import com.mljr.schedule.model.GroupModel;
import com.mljr.schedule.model.ScheduleDistribution;
import com.mljr.schedule.model.TaskModel;
import com.mljr.schedule.redis.JedisTool;
import com.mljr.schedule.service.IScheduleService;
import com.mljr.schedule.util.Constant;
import redis.clients.jedis.Jedis;

@Service("scheduleService")
public class ScheduleServiceImpl implements IScheduleService {
	
	
	private static Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);
	
	private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
	
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	@Resource
	private JedisTool jedisTool;
	
	private Scheduler scheduler = null;
	
	private Timer timer = null;
	
	public void registerSchedule(List<TaskModel> taskModelList) {
		Jedis jedis = null;
		try {
			jedis = jedisTool.createJedis();
			// 注册标识
			for (TaskModel taskModel : taskModelList) {
				// client 结点信息
				jedis.set(taskModel.getSchemaKey(), JSONObject.toJSONString(taskModel), "NX", "EX", Constant.EXPIRE_TIME);
				logger.info("客户端注册：" + JSONObject.toJSONString(taskModel));
				// 服务端配置信息
				jedis.setnx(Constant.SCHEMA_KEY_PREFIX + taskModel.getSchemaKey(), JSONObject.toJSONString(taskModel));
				// }
			}
		} catch (Throwable t) {
			logger.error("jedis exception", t);
		} finally {
			if (jedis != null)
				try {
					jedisTool.getTool().close();
				} catch (Throwable t) {
					logger.error("Jedis 释放对象池异常", t);
				}
		}
	}
	
	public ExpressModel getExpressModelByTasktag(String taskTag) {
		Jedis jedis = null;
		try {
			jedis = jedisTool.createJedis();
			String expre = jedis.hget("EXPRESSION_KEY", taskTag);
			if (!StringUtils.isEmpty(expre))
				return (ExpressModel) JSONObject.parseObject(expre, ExpressModel.class);
		} catch (Throwable t) {
			logger.error("jedis exception", t);
		} finally {
			if (jedis != null)
				try {
					jedisTool.getTool().close();
				} catch (Throwable t) {
					logger.error("Jedis 释放对象池异常", t);
				}
		}
		if (jedis != null) {
			try {
				jedisTool.getTool().close();
			} catch (Throwable t) {
				logger.error("Jedis 释放对象池异常", t);
			}
		}
		return null;
	}
	
	public void sendMsg(TaskModel taskModel) {
		logger.info(
				"machine:" + taskModel.getGroup() + taskModel.getUuid() + "  taskTag:" + taskModel.getTaskTag() + "  uuid:" + taskModel.getUuid());
		Jedis jedis = null;
		try {
			jedis = jedisTool.createJedis();
			jedis.publish(taskModel.getUuid(), JSONObject.toJSONString(taskModel));
		} catch (Throwable t) {
			logger.error("jedis exception", t);
			if (jedis != null)
				try {
					jedisTool.getTool().close();
				} catch (Throwable t2) {
					logger.error("Jedis 释放对象池异常", t2);
				}
		} finally {
			if (jedis != null)
				try {
					jedisTool.getTool().close();
				} catch (Throwable t) {
					logger.error("Jedis 释放对象池异常", t);
				}
		}
	}
	
	public List<TaskModel> queryTaskModels() {
		List<TaskModel> taskModelList = Lists.newArrayList();
		Set<String> uuidKeySet = jedisTool.keySet(Constant.SCHEMA_KEY+"*");
		if (uuidKeySet != null) {
			for (String uuid : uuidKeySet) {
				String taskJson = jedisTool.getValueByKey(uuid);
				TaskModel taskModel = (TaskModel) JSONObject.parseObject(taskJson, TaskModel.class);
				if (taskModel != null) {
					taskModelList.add(taskModel);
				}
			}
		}
		return taskModelList;
	}
	
	public List<TaskModel> queryTaskModelsWithExp() {
		Map<String, String> cornExp = jedisTool.hgetAll("EXPRESSION_KEY");
		List<TaskModel> taskModelList = Lists.newArrayList();
		Set<String> uuidKeySet = jedisTool.keySet("SCHEMA_KEY*");
		if (CollectionUtils.isNotEmpty(uuidKeySet)) {
			for (String uuid : uuidKeySet) {
				String taskJson = jedisTool.getValueByKey(uuid);
				TaskModel taskModel = (TaskModel) JSONObject.parseObject(taskJson, TaskModel.class);
				if ((taskModel != null) && (cornExp.get(taskModel.getTaskTag()) != null))
					taskModelList.add(taskModel);
			}
		}
		return taskModelList;
	}
	
	public ScheduleDistribution createDistribution(StrategyTypeEnum strategyType) {
		ScheduleDistribution model = new ScheduleDistribution(strategyType);
		List<TaskModel> allTask = queryTaskModelsWithExp();
		List<TaskModel> taskStatusModels = queryTaskStatusModels();
		formatInclude(taskStatusModels, allTask);
		for (TaskModel t : taskStatusModels) {
			if (t.getStatus().equals(TaskRunningStatusEntum.COMPLETE.toString())) {
				t.setStatus(TaskRunningStatusEntum.INIT.toString());
				jedisTool.setValue(t.getSchemaKey(), JSONObject.toJSONString(t));
			}
		}
		jedisTool.setValue("NODE_CHANGE_MARK", getHashSum(allTask));
		model.setTaskModels(taskStatusModels);
		return model;
	}
	
	private void formatInclude(List<TaskModel> taskStatusModels, List<TaskModel> taskModels) {
		for (int i = 0; i < taskStatusModels.size(); i++)
			if (!taskModels.contains(taskStatusModels.get(i))) {
				taskStatusModels.remove(i);
				i--;
			}
	}
	
	public void subscribe(String group, String UUID, Map<String, TaskService> tasks) {
		jedisTool.subscribe(UUID, new SubcribeListener(UUID, tasks, this));
	}
	
	public void startSchedule(ScheduleDistribution distribution) {
		try {
			// 得到分配好的任务列表
			List<GroupModel> taskDealStackMap = distribution.getTaskDealStack();
			if (taskDealStackMap == null) return;
			List<CommonCronTrigger> triggerList = Lists.newArrayList();
			List<CommonTimerTask> timerTaskList = Lists.newArrayList();
			// 生成待执行任务集合
			for (GroupModel groupModel : taskDealStackMap) {
				List<TaskModel> taskTriggers = groupModel.getTaskModels();
				for (TaskModel taskModel : taskTriggers) {
					String express = this.queryCronExpression(taskModel.getTaskTag());
					if (!StringUtils.isEmpty(express)) {
						ExpressModel expressModel = JSONObject.parseObject(express, ExpressModel.class);
						// 创建quarz时间触发器
						if (ScheduleTypeEnum.QUARZ.name().equals(expressModel.getType())) {
							CommonCronTrigger trigger = new CommonCronTrigger();
							trigger.setCronExpression(expressModel.getExpress());
							CommonJobDetail jobDetail = new CommonJobDetail();
							jobDetail.setGroup(taskModel.getGroup());
							jobDetail.setUuid(taskModel.getUuid());
							jobDetail.setTaskTag(taskModel.getTaskTag());
							jobDetail.setBeanName(UUID.randomUUID() + "_JOB");
							jobDetail.setScheduleService(this);
							trigger.setJobDetail(jobDetail);
							triggerList.add(trigger);
						} else {
							// 创建Timer触发器
							timerTaskList.add(new CommonTimerTask(taskModel, this));
						}
					}
				}
			}
			// 执行quarz任务
			this.stopSchedule();
			if (!triggerList.isEmpty()) {
				startTriggers(triggerList);
			}
			// 执行Timer任务
			if (!timerTaskList.isEmpty()) {
				startTimer(timerTaskList);
			}
		} catch (Throwable t) {
			logger.error("start schedule exception", t);
		}
	}
	
	private void startTimer(List<CommonTimerTask> timerTaskList) {
		if (this.timer == null) {
			this.timer = new Timer(true);
		}
		for (CommonTimerTask t : timerTaskList)
			try {
				TaskModel taskModel = t.getTaskModel();
				if (taskModel != null) {
					ExpressModel express = getExpressModelByTasktag(taskModel.getTaskTag());
					if (express != null) {
						if (ScheduleTypeEnum.TIMER_INTERVAL.name().equals(express.getType())) {
							long[] interval = express.getIntervalArray();
							for (int i = 0; i < interval.length; i++)
								this.timer.schedule(new CommonTimerTask(t.getTaskModel(), t.getScheduleService()), interval[i]);
						} else if (ScheduleTypeEnum.TIMER_DATETIME.name().equals(express.getType())) {
							Date dealTime = express.getDealDatetime();
							if (dealTime != null)
								this.timer.schedule(t, dealTime);
						}
					}
				}
			} catch (Throwable e) {
				logger.error("Timer exception", e);
			}
	}
	
	public void stopSchedule() throws SchedulerException {
		if ((this.scheduler != null) && (this.scheduler.isStarted())) {
			this.scheduler.shutdown(true);
		}
		if (this.timer != null) {
			this.timer.cancel();
			this.timer = null;
		}
	}
	
	public String getHashSum(List<TaskModel> taskModels) {
		int sum = 0;
		try {
			for (TaskModel t : taskModels)
				sum += t.hashCode();
		} catch (Throwable t) {
			logger.error("common exception", t);
		}
		return String.valueOf(sum);
	}
	
	private void startTriggers(List<CommonCronTrigger> triggerList) throws SchedulerException {
		if ((this.scheduler != null) && (this.scheduler.isStarted())) {
			this.scheduler.shutdown(true);
		}
		this.scheduler = schedulerFactory.getScheduler();
		for (CommonCronTrigger trigger : triggerList) {
			try {
				this.scheduler.scheduleJob((CommonJobDetail) trigger.getJobDataMap().get("jobDetail"), trigger);
			} catch (Throwable t) {
				logger.error("scheduler start exception", t);
			}
		}
		this.scheduler.start();
	}
	
	public String queryCronExpression(String taskName) {
		return jedisTool.hget("EXPRESSION_KEY", taskName);
	}
	
	public void updateExpireTime(List<TaskModel> taskModels) {
		Jedis jedis = null;
		try {
			jedis = jedisTool.createJedis();
			for (TaskModel t : taskModels) {
				if (jedis.exists(t.getSchemaKey()).booleanValue())
					jedis.expire(t.getSchemaKey(), 5);
				else {
					jedis.set(t.getSchemaKey(), JSONObject.toJSONString(t), "NX", "EX", 5);
				}
			}
		} catch (Throwable t) {
			logger.error("jedis exception", t);
			if (jedis != null)
				try {
					jedisTool.getTool().close();
				} catch (Throwable t2) {
					logger.error("Jedis 释放对象池异常", t2);
				}
		} finally {
			if (jedis != null)
				try {
					jedisTool.getTool().close();
				} catch (Throwable t) {
					logger.error("Jedis 释放对象池异常", t);
				}
		}
	}
	
	public void saveExpression(String taskName, String expression) {
		Jedis jedis = null;
		try {
			jedis = jedisTool.createJedis();
			if (StringUtils.isEmpty(expression))
				jedis.hdel("EXPRESSION_KEY", new String[] { taskName });
			else
				jedis.hset("EXPRESSION_KEY", taskName, expression);
		} catch (Throwable t) {
			logger.error("jedis exception", t);
			if (jedis != null)
				try {
					jedisTool.getTool().close();
				} catch (Throwable t2) {
					logger.error("Jedis 释放对象池异常", t2);
				}
		} finally {
			if (jedis != null)
				try {
					jedisTool.getTool().close();
				} catch (Throwable t) {
					logger.error("Jedis 释放对象池异常", t);
				}
		}
	}
	
	public TaskModel getTaskByTag(String tag) {
		Jedis jedis = null;
		try {
			jedis = jedisTool.createJedis();
			String jsonModel = jedis.get(tag);
			if (!StringUtils.isEmpty(jsonModel)) {
				TaskModel task = (TaskModel) JSONObject.parseObject(jsonModel, TaskModel.class);
				return task;
			}
		} catch (Throwable t) {
			logger.error("jedis exception", t);
		} finally {
			if (jedis != null)
				try {
					jedisTool.getTool().close();
				} catch (Throwable t) {
					logger.error("Jedis 释放对象池异常", t);
				}
		}
		if (jedis != null) {
			try {
				jedisTool.getTool().close();
			} catch (Throwable t) {
				logger.error("Jedis 释放对象池异常", t);
			}
		}
		return null;
	}
	
	public void updateTaskModelStatus(String createSchemaKey, TaskRunningStatusEntum status) {
		Jedis jedis = null;
		try {
			jedis = jedisTool.createJedis();
			String jsonModel = jedis.get(createSchemaKey);
			if (!StringUtils.isEmpty(jsonModel)) {
				TaskModel taskModel = (TaskModel) JSONObject.parseObject(jsonModel, TaskModel.class);
				taskModel.setStatus(status.name());
				if (status.equals(TaskRunningStatusEntum.RUNNING)) {
					taskModel.setCount(taskModel.getCount() + 1);
					taskModel.setDealTime(format.format(new Date()));
				}
				jedis.set("STATUS_" + taskModel.getSchemaKey(), JSONObject.toJSONString(taskModel));
			}
		} catch (Throwable t) {
			logger.error("jedis exception", t);
			if (jedis != null)
				try {
					jedisTool.getTool().close();
				} catch (Throwable t2) {
					logger.error("Jedis 释放对象池异常", t2);
				}
		} finally {
			if (jedis != null)
				try {
					jedisTool.getTool().close();
				} catch (Throwable t) {
					logger.error("Jedis 释放对象池异常", t);
				}
		}
	}
	
	public List<TaskModel> queryTaskStatusModels() {
		List<TaskModel> taskModelList = Lists.newArrayList();
		Set<String> uuidKeySet = jedisTool.keySet("STATUS_SCHEMA_KEY*");
		if (uuidKeySet != null) {
			for (String uuid : uuidKeySet) {
				String taskJson = jedisTool.getValueByKey(uuid);
				TaskModel taskModel = (TaskModel) JSONObject.parseObject(taskJson, TaskModel.class);
				if (taskModel != null)
					taskModelList.add(taskModel);
			}
		}
		return taskModelList;
	}
	
	public Set<String> queryKeysByPattern(String pattern) {
		Jedis jedis = null;
		try {
			jedis = jedisTool.createJedis();
			return jedis.keys(pattern);
		} catch (Throwable t) {
			logger.error("jedis exception", t);
		} finally {
			if (jedis != null)
				try {
					jedisTool.getTool().close();
				} catch (Throwable t) {
					logger.error("Jedis 释放对象池异常", t);
				}
		}
		return null;
	}
	
	public void checkChannelAlive(final String group, final String uuid, final Map<String, TaskService> taskMap) {
		Jedis jedis = null;
		try {
			if ((taskMap == null) || (taskMap.isEmpty())) {
				logger.error("订阅渠道号[" + uuid + "]不存在对应的任务，忽略注册....");
				return;
			}
			jedis = jedisTool.createJedis();
			Map<String, String> jedisChannelMap = jedis.pubsubNumSub(new String[] { uuid });
			Iterator<Map.Entry<String, String>> localIterator = jedisChannelMap.entrySet().iterator();
			if (localIterator.hasNext()) {
				Map.Entry<String, String> jedisChannelEntry = (Map.Entry<String, String>) localIterator.next();
				int jedisChannelCount = Integer.parseInt(jedisChannelEntry.getValue());
				logger.debug("订阅渠道号[" + uuid + "]目前已注册数量[" + jedisChannelCount + "]....");
				if (jedisChannelCount <= 0) {
					logger.error("订阅渠道号[" + uuid + "]不存在，重新注册中....");
					new Thread(new Runnable() {
						public void run() {
							ScheduleServiceImpl.this.subscribe(group, uuid, taskMap);
						}
					}).start();
				}
			}
			if (jedis != null)
				jedisTool.getTool().close();
		} catch (Exception e) {
			if (jedis != null) {
				jedisTool.getTool().close();
			}
			logger.error("订阅渠道号[" + uuid + "]不存在，重新注册出现异常", e);
		}
	}
}
