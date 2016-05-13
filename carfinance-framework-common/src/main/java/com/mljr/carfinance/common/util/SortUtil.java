package com.mljr.carfinance.common.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @Description: 排序
 * @ClassName: SortUtil
 * @author gaoxiang
 * @date 2016年4月27日 下午5:20:36
 */ 
public class SortUtil {
	
	public static Integer getRank(Long times, List<Long> list) {
		ArrayList<Long> arrayList = new ArrayList<>(new HashSet<>(list));
		Collections.sort(arrayList);
		return arrayList.indexOf(times);
	}

	public static Integer getRank(BigDecimal amount, List<BigDecimal> list) {
		ArrayList<BigDecimal> arrayList = new ArrayList<>(new HashSet<>(list));
		Collections.sort(arrayList);
		return arrayList.indexOf(amount);
	}
	
}
