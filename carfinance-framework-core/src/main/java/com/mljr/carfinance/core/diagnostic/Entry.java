package com.mljr.carfinance.core.diagnostic;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import com.google.common.collect.Lists;

public final class Entry {
	
	private final List<Entry> subEntries = Lists.newArrayListWithCapacity(4);
	
	private final String message;
	
	private final Entry parentEntry;
	
	private final Entry firstEntry;
	
	private final long baseTime;
	
	private final long startTime;
	
	private long endTime;
	
	private boolean isStart = false;
	
	public Entry(String message, Entry parentEntry, Entry firstEntry) {
		this.message = message;
		this.startTime = System.currentTimeMillis();
		this.parentEntry = parentEntry;
		this.firstEntry = (Entry) ObjectUtils.defaultIfNull(firstEntry, this);
		this.baseTime = (firstEntry == null) ? 0 : firstEntry.startTime;
		this.isStart = true;
	}
	
	public String getMessage() {
		return StringUtils.defaultIfEmpty(message, null);
	}
	
	public long getStartTime() {
		return (baseTime > 0) ? (startTime - baseTime) : 0;
	}
	
	public long getEndTime() {
		if (endTime < baseTime) {
			return -1;
		} else {
			return endTime - baseTime;
		}
	}
	
	public long getDuration() {
		if (endTime < startTime) {
			return -1;
		} else {
			return endTime - startTime;
		}
	}
	
	public long getDurationOfSelf() {
		long duration = getDuration();
		if (duration < 0) {
			return -1;
		} else if (subEntries.isEmpty()) {
			return duration;
		} else {
			for (int i = 0; i < subEntries.size(); i++) {
				Entry subEntry = (Entry) subEntries.get(i);
				duration -= subEntry.getDuration();
			}
			if (duration < 0) {
				return -1;
			} else {
				return duration;
			}
		}
	}
	
	public double getPecentage() {
		double parentDuration = 0;
		double duration = getDuration();
		if ((parentEntry != null) && parentEntry.isReleased()) {
			parentDuration = parentEntry.getDuration();
		}
		if ((duration > 0) && (parentDuration > 0)) {
			return duration / parentDuration;
		} else {
			return 0;
		}
	}
	
	public double getPecentageOfAll() {
		double firstDuration = 0;
		double duration = getDuration();
		if ((firstEntry != null) && firstEntry.isReleased()) {
			firstDuration = firstEntry.getDuration();
		}
		if ((duration > 0) && (firstDuration > 0)) {
			return duration / firstDuration;
		} else {
			return 0;
		}
	}
	
	public List<Entry> getSubEntries() {
		return Collections.unmodifiableList(subEntries);
	}
	
	public void release() {
		endTime = System.currentTimeMillis();
	}
	
	/**
	 * 判断当前entry是否结束。
	 */
	public boolean isReleased() {
		return endTime > 0;
	}
	
	public void enterSubEntry(String message) {
		Entry subEntry = new Entry(message, this, firstEntry);
		subEntries.add(subEntry);
	}
	
	public Entry getUnreleasedEntry() {
		Entry subEntry = null;
		if (!subEntries.isEmpty()) {
			subEntry = (Entry) subEntries.get(subEntries.size() - 1);
			if (subEntry.isReleased()) {
				subEntry = null;
			}
		}
		return subEntry;
	}
	
	public String toString(String prefix1, String prefix2) {
		StringBuffer buffer = new StringBuffer();
		toString(buffer, prefix1, prefix2);
		return buffer.toString();
	}
	
	private void toString(StringBuffer buffer, String prefix1, String prefix2) {
		buffer.append(prefix1);
		String message = getMessage();
		long startTime = getStartTime();
		long duration = getDuration();
		long durationOfSelf = getDurationOfSelf();
		double percentOfAll = getPecentageOfAll();
		Object[] params = new Object[] { message, // {0} - entry信息
				new Long(startTime), // {1} - 起始时间
				new Long(duration), // {2} - 持续总时间
				new Long(durationOfSelf), // {3} -
											// 自身消耗的时间
				new Double(percentOfAll) // {4} -
											// 在总时间中所旧的时间比例
		};
		StringBuffer pattern = new StringBuffer("{1,number} ");
		if (isReleased()) {
			pattern.append("[{2,number}ms");
			if ((durationOfSelf > 0) && (durationOfSelf != duration)) {
				pattern.append(" ({3,number}ms)");
			}
			if (percentOfAll > 0) {
				pattern.append(", {4,number,##%}");
			}
			pattern.append("]");
		} else {
			pattern.append("[UNRELEASED]");
		}
		if (message != null) {
			pattern.append(" - {0}");
		}
		buffer.append(MessageFormat.format(pattern.toString(), params));
		for (int i = 0; i < subEntries.size(); i++) {
			Entry subEntry = (Entry) subEntries.get(i);
			buffer.append('\n');
			if (i == (subEntries.size() - 1)) {
				subEntry.toString(buffer, prefix2 + "`---", prefix2 + "    "); // 最后一项
			} else if (i == 0) {
				subEntry.toString(buffer, prefix2 + "+---", prefix2 + "|   "); // 第一项
			} else {
				subEntry.toString(buffer, prefix2 + "+---", prefix2 + "|   "); // 中间项
			}
		}
	}
	
	public boolean isStart() {
		return isStart;
	}
}