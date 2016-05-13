package com.mljr.carfinance.schedule.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import com.mljr.carfinance.schedule.enums.ScheduleTypeEnum;

public class ExpressModel {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private String type;
	
	private String express;
	
	public ExpressModel() {
	}
	
	public ExpressModel(String type, String express) {
		this.type = type;
		this.express = express;
	}
	
	public long[] getIntervalArray() {
		if ((StringUtils.isNotBlank(this.express)) && (ScheduleTypeEnum.TIMER_INTERVAL.name().equals(this.type))) {
			String[] tArray = this.express.replaceAll("s", "").split(",");
			long[] arr = new long[tArray.length];
			for (int i = 0; i < arr.length; i++) {
				if (i == 0)
					arr[i] = (Long.valueOf(tArray[i]).longValue() * 1000L);
				else {
					arr[i] = (arr[(i - 1)] + Long.valueOf(tArray[i]).longValue() * 1000L);
				}
			}
			return arr;
		}
		return new long[0];
	}
	
	public Date getDealDatetime() {
		if ((StringUtils.isNotBlank(this.express)) && (ScheduleTypeEnum.TIMER_DATETIME.name().equals(this.type))) {
			try {
				return dateFormat.parse(this.express);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getExpress() {
		return this.express;
	}
	
	public void setExpress(String express) {
		this.express = express;
	}
}
