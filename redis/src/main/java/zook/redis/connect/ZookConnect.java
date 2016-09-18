package zook.redis.connect;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;

import zook.redis.RedisContext;
import zook.redis.watcher.ZookReConnectWatcher;

public class ZookConnect {

	//	final static String[] zookIps ={"localhost:2181","localhost:2182","localhost:2183"};

	//	private static List<ZooKeeper> sessionList = new ArrayList<ZooKeeper>();

	private final static Map<String,ZooKeeper> sessionPool = new ConcurrentHashMap<String,ZooKeeper>();

	static {
		String[] zookIps = RedisContext.getPropertie(RedisContext.zookeeperNode).split(",");
		for(String ip : zookIps){
			try {
				ZooKeeper zooKeeper = new ZooKeeper(ip,2000,new ZookReConnectWatcher());
				//等待连接zookeeper 成功
				int i = 0 ;
				while(!zooKeeper.getState().equals(States.CONNECTED)&&i<100){
					i++;
					Thread.sleep(10);
					System.out.println(" -------"+zooKeeper.getState());
				}
				sessionPool.put(ip,zooKeeper);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("创建zook-------");
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
