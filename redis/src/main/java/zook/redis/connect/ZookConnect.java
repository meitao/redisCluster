package zook.redis.connect;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zook.redis.RedisContext;
import zook.redis.loadBalance.LoadBalance;
import zook.redis.loadBalance.RandomLoadBalance;
import zook.redis.watcher.ZookReConnectWatcher;

public class ZookConnect {


	private static Logger log = LoggerFactory.getLogger(ZookConnect.class);   

	private final static Map<String,ZooKeeper> sessionPool = new ConcurrentHashMap<String,ZooKeeper>();

	private  static LoadBalance loadBalance ;

	static {
		
		String[] zookIps = RedisContext.getPropertie(RedisContext.zookeeperNode).split(",");
		String  zookLoadWeight = RedisContext.getPropertie(RedisContext.zookLoadWeight);
		
		String[] loadWeights = null  ;
		Map<String,Integer> param = new HashMap<String,Integer>();

		if(zookLoadWeight!=null&&"".equals(zookLoadWeight)){
			loadWeights = zookLoadWeight.split(",");
		}
		for(int j=0;j<zookIps.length;j++){
			String ip = null ;
			try {
				ip = zookIps[j];
				ZooKeeper zooKeeper = new ZooKeeper(ip,2000,new ZookReConnectWatcher());
				//等待连接zookeeper 成功
				int i = 0 ;
				while(!zooKeeper.getState().equals(States.CONNECTED)&&i<100){
					i++;
					Thread.sleep(10);
					log.info(" -------"+zooKeeper.getState());
				}
				sessionPool.put(ip,zooKeeper);
			} catch (IOException e) {
				e.printStackTrace();
				log.error(" IOException : ",e);
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.error(" InterruptedException : ",e);
			}
			for(int k=0;k<loadWeights.length;k++){
				param.put(ip, Integer.valueOf(loadWeights[k]));
			}
			loadBalance = new  RandomLoadBalance(param);
			
			log.info("创建zook-------");
		}
	}

	/**
	 * 获取有效的连接
	 * @return
	 */
	public static  ZooKeeper getConnect(){
		
		int i = 0 ;
		ZooKeeper zk = null;

		while(i<sessionPool.size()){
			i++;
			zk = sessionPool.get(loadBalance.loadBalance());
			if(zk.getState().equals(States.CONNECTED)){
				break ;
			}
		}
		 
		return zk;
	}


	public static Map<String,ZooKeeper> getSessionPool(){
		return sessionPool ;
	}
}
