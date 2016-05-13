package com.mljr.schedule.service;

import java.util.List;
import com.mljr.schedule.model.GroupModel;
import com.mljr.schedule.model.TaskModel;

public interface IDistribution {
	
	List<GroupModel> distribute(List<TaskModel> paramList);
}
