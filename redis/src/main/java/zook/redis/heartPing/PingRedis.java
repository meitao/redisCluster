package zook.redis.heartPing;

import java.net.InetAddress;

import redis.clients.jedis.Jedis;
import zook.redis.RedisContext;
import zook.redis.connect.RedisConnect;

public class PingRedis {
	private static Jedis jedis ;
	static {
		try{
			InetAddress addr  = InetAddress.getLocalHost();
		    jedis = RedisConnect.getConnect( addr.getHostAddress(),
					Integer.parseInt(RedisContext.getPropertie(RedisContext.redisPort)));
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static String heartPing(){
		return jedis.ping();
	}
}