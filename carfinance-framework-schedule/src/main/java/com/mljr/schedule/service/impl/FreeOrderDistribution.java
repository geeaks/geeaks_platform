package com.mljr.schedule.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mljr.schedule.model.GroupModel;
import com.mljr.schedule.model.TaskModel;
import com.mljr.schedule.service.IDistribution;

/**
 * @Description: 自由顺序分配
 * @ClassName: FreeOrderDistribution
 * @author gaoxiang
 * @date 2016年5月11日 上午11:23:03
 */ 
public class FreeOrderDistribution implements IDistribution {
	
	@Override
	public List<GroupModel> distribute(List<TaskModel> taskModels) {
		List<GroupModel> groupModels = Lists.newArrayList();
		// 分配
		if (taskModels != null) {
			// 数据构造：一个group下运行任务的uuid集合
			Map<String, List<String>> uuidMap = Maps.newHashMap();
			// 一个group下的任务名称
			Map<String, List<String>> taskMap = Maps.newHashMap();
			for (TaskModel taskModle : taskModels) {
				List<String> uuidList = uuidMap.get(taskModle.getGroup());
				List<String> taskList = taskMap.get(taskModle.getGroup());
				if (uuidList == null) {
					uuidList = Lists.newArrayList();
					uuidMap.put(taskModle.getGroup(), uuidList);
				}
				if (!uuidList.contains(taskModle.getUuid())) {
					uuidList.add(taskModle.getUuid());
				}
				if (taskList == null) {
					taskList = Lists.newArrayList();
					taskMap.put(taskModle.getGroup(), taskList);
				}
				if (!taskList.contains(taskModle.getTaskTag())) {
					taskList.add(taskModle.getTaskTag());
				}
			}
			// 按group对其 任务执行排序
			Set<String> groups = uuidMap.keySet();
			for (String group : groups) {
				List<TaskModel> newTaskModels = freeOrder(uuidMap.get(group), taskMap.get(group));
				for (TaskModel m : newTaskModels) {
					m.setGroup(group);
				}
				GroupModel groupM = new GroupModel();
				groupM.setGroupName(group);
				groupM.setTaskModels(newTaskModels);
				groupModels.add(groupM);
			}
		}
		return groupModels;
	}
	
	private List<TaskModel> freeOrder(List<String> uuidList, List<String> taskList) {
		Map<String, List<String>> orderMap = this.distribute(uuidList, taskList);
		Set<Entry<String, List<String>>> orderedList = orderMap.entrySet();
		List<TaskModel> triggerList = Lists.newArrayList();
		TaskModel mo = null;
		for (Entry<String, List<String>> entry : orderedList) {
			String uuid = entry.getKey();
			List<String> tasks = entry.getValue();
			for (String task : tasks) {
				mo = new TaskModel();
				mo.setTaskTag(task);
				mo.setUuid(uuid);
				triggerList.add(mo);
			}
		}
		return triggerList;
	}
	
	private Map<String, List<String>> distribute(List<String> uuidList, List<String> taskList) {
		int nodeSize = uuidList.size();
		int taskSize = taskList.size();
		if (uuidList == null || taskSize == 0 || taskList == null || nodeSize == 0) {
			return null;
		}
		// 按照ip的hash排序
		Collections.sort(uuidList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				long has1 = o1.hashCode();
				long has2 = o2.hashCode();
				return (int) (has1 - has2);
			}
		});
		Collections.sort(taskList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				long has1 = o1.hashCode();
				long has2 = o2.hashCode();
				return (int) (has1 - has2);
			}
		});
		// 保证key按照weight有序
		LinkedHashMap<String, List<String>> map = Maps.newLinkedHashMap();
		for (String node : uuidList) {
			map.put(node, new ArrayList<String>());
		}
		// 机器多于任务
		if (uuidList.size() >= taskList.size()) {
			for (int i = 0; i < taskList.size(); i++) {
				map.get(uuidList.get(i)).add(taskList.get(i));
			}
		} else {
			// 任务多于机器
			for (int i = 0; i < taskList.size(); i++) {
				map.get(uuidList.get(i % uuidList.size())).add(taskList.get(i));
			}
		}
		return map;
	}
}
