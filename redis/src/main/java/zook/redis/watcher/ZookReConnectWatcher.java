package zook.redis.watcher;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zook.redis.connect.ZookConnect;
/**
 * 
 * @author Administrator
 *
 */
public class ZookReConnectWatcher implements Watcher{
	
	private static Logger log = LoggerFactory.getLogger(ZookReConnectWatcher.class);  
	
	// 监控所有被触发的事件
	public void process(WatchedEvent event) { 
		
		if(event.getState().equals(KeeperState.Expired)){
			try {
				Map<String, ZooKeeper> pool = ZookConnect.getSessionPool();
				Set<Entry<String, ZooKeeper>> entrys=pool.entrySet();
				for(Entry<String, ZooKeeper> param : entrys){
					if(param.getValue().getState().equals(States.CLOSED)){
						log.info("zookeeper 服务器 "+param.getKey()+" 过期 "); 
						ZooKeeper zooKeeper =new ZooKeeper(param.getKey(),2000,new ZookReConnectWatcher());
						param.setValue(zooKeeper);
						log.info("zookeeper 服务器 "+param.getKey()+" 重新连接 "); 
					}
				}
				
			} catch (IOException e) {
				log.error(" IOException : ",e);
				e.printStackTrace();
			}
		} 
		log.info("触发事件 >>>" + event.toString() ); 
	}

}