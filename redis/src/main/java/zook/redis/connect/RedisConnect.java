package zook.redis.connect;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisConnect {

	public static  Jedis getConnect(String ip,int port){
		//"localhost:" + 2181
		JedisPool pool = new JedisPool( ip , port);
		Jedis  jedis = pool.getResource();
		return  jedis;
	}
	
}
