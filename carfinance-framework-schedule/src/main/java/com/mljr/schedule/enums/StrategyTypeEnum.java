package com.mljr.schedule.enums;

import com.mljr.schedule.service.IDistribution;
import com.mljr.schedule.service.impl.FreeOrderDistribution;

/**
 * @Description: 分配策略生成类型
 * @ClassName: StrategyTypeEnum
 * @author gaoxiang
 * @date 2016年5月11日 下午1:22:33
 */ 
public enum StrategyTypeEnum {
	/**
	 * 自由顺序
	 */
	FREE_ORDER {
		@Override
		public IDistribution getDistribution() {
			return new FreeOrderDistribution();
		}
	},
	/**
	 * 取模
	 */
	MOD {
		@Override
		public IDistribution getDistribution() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	/**
	 * 手动配置
	 */
	HANDLE {
		@Override
		public IDistribution getDistribution() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	public abstract IDistribution getDistribution();
}
