package zook.redis.heartPing;

import java.net.InetAddress;

import redis.clients.jedis.Jedis;
import zook.redis.RedisContext;
import zook.redis.connect.RedisConnect;

public class PingRedis {

	public static String heartPing(){
		String ping = null ;
		try{
			InetAddress addr  = InetAddress.getLocalHost();
			Jedis jedis = RedisConnect.getConnect( "127.0.0.1",
					Integer.parseInt(RedisContext.getPropertie(RedisContext.redisPort)));
		    ping = jedis.ping();
			System.out.println(" heartPing ----"+ping);
			return ping ;
		} catch(Exception e){
			e.printStackTrace();
		}
		return  ping; 
	}

}
