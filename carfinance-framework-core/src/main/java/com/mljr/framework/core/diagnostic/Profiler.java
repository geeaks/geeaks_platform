package com.mljr.framework.core.diagnostic;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 性能诊断工具 使用场景：1. 定义在通用框架中，用于分析整体系统的实时性能 2.需要对特定个业务块做性能分析时
 * @ClassName: Profiler
 * @author gaoxiang
 * @date 2015年11月17日 下午10:39:30
 */ 
public final class Profiler {
	
	private static final ThreadLocal<Entry> entryStack = new ThreadLocal<Entry>();
	
	private static ThreadLocal<Long> profilerContext = new ThreadLocal<Long>() {
		// 初始化值
		public Long initialValue() {
			return 0L;
		}
	};
	
	/**
	 * @Description: 该线程栈开始调用方法
	 * @param message
	 * @return void 返回类型
	 * @author gaoxiang
	 * @date 2016年5月5日 上午11:47:42
	 */
	public static void start(String message) {
		//如果是在方法内，则enter成分支
		if (isStart()) {
			enter(message);
		} else {
			entryStack.set(new Entry(message, null, null));
		}
		profilerContext.set(profilerContext.get() + 1L);
	}
	
	/**
	 * @Description: 该线程栈是否已经开启统计
	 * @return boolean 返回类型
	 * @author gaoxiang
	 * @date 2016年5月5日 上午11:50:00
	 */
	public static boolean isStart() {
		return null != getEntry() && getEntry().isStart();
	}
	
	/**
	 * @Description: 重置线程栈上下文
	 * @return void 返回类型
	 * @author gaoxiang
	 * @date 2016年5月5日 上午11:52:11
	 */
	public static void reset() {
		Long c = profilerContext.get();
		if (null != c && c.longValue() > 1) {
			profilerContext.set(profilerContext.get() - 1L);
		} else {
			entryStack.set(null);
			profilerContext.set(0L);
		}
	}
	
	public static void enter(String message) {
		Entry currentEntry = getCurrentEntry();
		if (currentEntry != null) {
			currentEntry.enterSubEntry(message);
		}
	}
	
	public static void release() {
		Entry currentEntry = getCurrentEntry();
		
		if (currentEntry != null) {
			currentEntry.release();
		}
	}
	
	public static long getDuration() {
		Entry entry = (Entry) entryStack.get();
		
		if (entry != null) {
			return entry.getDuration();
		} else {
			return -1;
		}
	}
	
	public static String dump() {
		return dump("", "");
	}
	
	/**
	 * 判断被父级方法调用过
	 */
	public static boolean isSuperStart() {
		return profilerContext.get() > 1L;
	}
	
	public static String dump(String prefix) {
		return dump(prefix, prefix);
	}
	
	public static String dump(String prefix1, String prefix2) {
		Entry entry = (Entry) entryStack.get();
		if (entry != null) {
			return entry.toString(prefix1, prefix2);
		} else {
			return "";
		}
	}
	
	public static Entry getEntry() {
		return (Entry) entryStack.get();
	}
	
	private static Entry getCurrentEntry() {
		Entry subEntry = (Entry) entryStack.get();
		Entry entry = null;
		
		if (subEntry != null) {
			do {
				entry = subEntry;
				subEntry = entry.getUnreleasedEntry();
			} while (subEntry != null);
		}
		
		return entry;
	}
	
	public static void main(String[] args) {
		ThreadPoolExecutor consumeExecutor = new ThreadPoolExecutor(30, 30 + 10, 5, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(30 + 10), new ThreadFactory() {
					public Thread newThread(Runnable r) {
						Thread myThread = new Thread(r);
						myThread.setName("TT");
						return myThread;
					}
				}, new ThreadPoolExecutor.CallerRunsPolicy());
		while (true) {
			consumeExecutor.execute(new Runnable() {
				public void run() {
					Profiler.start("Start method: a");
					try {
						Thread.sleep(100);
						
						Profiler.enter("Start method: a-1");
						Thread.sleep(100);
						Profiler.release();
						
						Profiler.enter("Start method: a-2");
						Thread.sleep(100);
						Profiler.release();
						
						Profiler.start("Invoking method: b");
						try {
							Thread.sleep(100);
							
							Profiler.enter("Start method: b-1");
							Thread.sleep(100);
							Profiler.release();
							
							Profiler.enter("Start method: b-2");
							Thread.sleep(100);
							Profiler.release();
							
						} finally {
							Profiler.release();
							if (!Profiler.isSuperStart())
								System.out.println(Profiler.dump());
							Profiler.reset();
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						Profiler.release();
						System.out.println(Profiler.dump());
						Profiler.reset();
					}
					
				}
			});
		}
	}
}
