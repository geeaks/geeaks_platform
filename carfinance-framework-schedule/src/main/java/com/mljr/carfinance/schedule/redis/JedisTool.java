package com.mljr.carfinance.schedule.redis;

import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

/**
 * @Description: Jedis连接池实现
 * @ClassName: JedisTool
 * @author gaoxiang
 * @date 2016年5月16日 下午7:35:02
 */ 
public class JedisTool {
	
	private Logger logger = LoggerFactory.getLogger(JedisTool.class);
	
	private JedisPool pool = null;
	
	private String ipAddr;
	
	private String port;
	
	private String password;
	
	private int timeout = 3000;
	
	private int maxIdle = 5;
	
	public JedisPool getTool() {
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(20);
			config.setMaxIdle(maxIdle);
			config.setMaxWaitMillis(100000L);
			config.setTestOnBorrow(true);
			int portExe = StringUtils.isBlank(port) ? 6379 : Integer.parseInt(port);
			if(StringUtils.isBlank(password)){
				pool = new JedisPool(config, ipAddr, portExe, timeout);
			}else{
				pool = new JedisPool(config, ipAddr, portExe, timeout, password);
			}
		}
		return pool;
	}
	
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	
	public void setPort(String port) {
		this.port = port;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Jedis createJedis() {
		return (Jedis) getTool().getResource();
	}
	
	public String getValueByKey(String key) {
		Jedis jedis = null;
		try {
			jedis = createJedis();
			return jedis.get(key);
		} catch (Throwable t) {
			logger.error("Jedis error!", t);
		} finally {
			if (jedis != null)
				try {
					getTool().returnResource(jedis);
				} catch (Throwable t) {
					logger.error("Jedis 释放对象池异常", t);
				}
		}
		return null;
	}
	
	public long ttl(String key) {
		Jedis jedis = null;
		try {
			jedis = createJedis();
			return jedis.ttl(key).longValue();
		} catch (Throwable t) {
			logger.error("Jedis error!", t);
		} finally {
			if (jedis != null)
				getTool().returnResource(jedis);
		}
		return 0L;
	}
	
	public void setValue(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = createJedis();
			jedis.set(key, value);
		} catch (Throwable t) {
			logger.error("Jedis error!", t);
			if (jedis != null)
				try {
					getTool().returnResource(jedis);
				} catch (Throwable t2) {
					logger.error("Jedis 释放对象池异常", t2);
				}
		} finally {
			if (jedis != null)
				try {
					getTool().returnResource(jedis);
				} catch (Throwable t) {
					logger.error("Jedis 释放对象池异常", t);
				}
		}
	}
	
	public void subscribe(String channel, JedisPubSub listener) {
		Jedis jedis = null;
		try {
			jedis = createJedis();
			jedis.subscribe(listener, new String[] { channel });
		} catch (Throwable t) {
			logger.error("Jedis error!", t);
			if (jedis != null)
				try {
					getTool().returnResource(jedis);
				} catch (Throwable t2) {
					logger.error("Jedis 释放对象池异常", t2);
				}
		} finally {
			if (jedis != null)
				try {
					getTool().returnResource(jedis);
				} catch (Throwable t) {
					logger.error("Jedis 释放对象池异常", t);
				}
		}
	}
	
	public String hget(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = createJedis();
			return jedis.hget(key, field);
		} catch (Throwable t) {
			logger.error("Jedis error!", t);
		} finally {
			if (jedis != null)
				try {
					getTool().returnResource(jedis);
				} catch (Throwable t) {
					logger.error("Jedis 释放对象池异常", t);
				}
		}
		return null;
	}
	
	public Map<String, String> hgetAll(String key) {
		Jedis jedis = null;
		try {
			jedis = createJedis();
			return jedis.hgetAll(key);
		} catch (Throwable t) {
			logger.error("Jedis error!", t);
		} finally {
			if (jedis != null)
				try {
					getTool().returnResource(jedis);
				} catch (Throwable t) {
					logger.error("Jedis 释放对象池异常", t);
				}
		}
		return null;
	}
	
	public Set<String> keySet(String pattern) {
		Jedis jedis = null;
		try {
			jedis = createJedis();
			return jedis.keys(pattern);
		} catch (Throwable t) {
			logger.error("Jedis error!", t);
		} finally {
			if (jedis != null)
				try {
					getTool().returnResource(jedis);
				} catch (Throwable t) {
					logger.error("Jedis 释放对象池异常", t);
				}
		}
		return null;
	}
	
	public void setExpireTime(String key, int expireTimes) {
		Jedis jedis = null;
		try {
			jedis = createJedis();
			jedis.expire(key, expireTimes);
		} catch (Throwable t) {
			logger.error("Jedis error!", t);
			if (jedis != null)
				try {
					getTool().returnResource(jedis);
				} catch (Throwable t2) {
					logger.error("Jedis 释放对象池异常", t2);
				}
		} finally {
			if (jedis != null)
				try {
					getTool().returnResource(jedis);
				} catch (Throwable t) {
					logger.error("Jedis 释放对象池异常", t);
				}
		}
	}
	
	public void del(String key) {
		Jedis jedis = null;
		try {
			jedis = createJedis();
			jedis.del(key);
		} catch (Throwable t) {
			logger.error("Jedis error!", t);
			if (jedis != null)
				try {
					getTool().returnResource(jedis);
				} catch (Throwable t2) {
					logger.error("Jedis 释放对象池异常", t2);
				}
		} finally {
			if (jedis != null)
				try {
					getTool().returnResource(jedis);
				} catch (Throwable t) {
					logger.error("Jedis 释放对象池异常", t);
				}
		}
	}
}
