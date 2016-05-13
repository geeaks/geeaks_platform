package com.mljr.schedule.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/applicationContext.xml" })
public class BaseTest {
	
	@Test
	public void test() {
		while (true) {
			try {
				Thread.sleep(20000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("睡了20秒...");
		}
	}
	
	@Test
	public void jedisPool() {
		JedisPool jedisPool = null;
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(20);
			config.setMaxIdle(5);
			config.setMaxWaitMillis(1000);
			config.setTestOnBorrow(true);
			jedisPool = new JedisPool(config, "192.168.50.245", 6379, 3000, "password");
			Jedis resource = jedisPool.getResource();
			resource.set("fookey", "foovalue");
			System.out.println(resource.get("fookey"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(jedisPool!=null)
			jedisPool.close();
		}
	}
}
