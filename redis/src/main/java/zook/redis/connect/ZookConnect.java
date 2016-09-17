package zook.redis.connect;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;

import zook.redis.RedisContext;

public class ZookConnect {

//	final static String[] zookIps ={"localhost:2181","localhost:2182","localhost:2183"};
	
//	private static List<ZooKeeper> sessionList = new ArrayList<ZooKeeper>();
	
	private final static Map<String,ZooKeeper> sessionPool = new ConcurrentHashMap<String,ZooKeeper>();
	
	static {
		String[] zookIps = RedisContext.getPropertie(RedisContext.zookeeperNode).split(",");
		for(String ip : zookIps){
			try {
				sessionPool.put(ip,new ZooKeeper(ip,2000, new Watcher() {
					// 监控所有被触发的事件
					public void process(WatchedEvent event) {
						System.out.println("已经触发了" + event.toString() + "事件！"); 
					} 
				}));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * 获取有效的连接
	 * @return
	 */
	public static  ZooKeeper getConnect(){
		
		Collection<ZooKeeper> set =(Collection<ZooKeeper>) sessionPool.values();
		for( ZooKeeper session :set){
			if(session.getState().equals(States.CONNECTED)){
				return session ;
			}
		}
		return null;
	}


	public static Map<String,ZooKeeper> getSessionPool(){
		return sessionPool ;
	}
}
