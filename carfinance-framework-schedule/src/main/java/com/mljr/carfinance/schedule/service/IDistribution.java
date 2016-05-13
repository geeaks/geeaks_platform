package com.mljr.carfinance.schedule.service;

import java.util.List;
import com.mljr.carfinance.schedule.model.GroupModel;
import com.mljr.carfinance.schedule.model.TaskModel;

public interface IDistribution {
	
	List<GroupModel> distribute(List<TaskModel> paramList);
}
