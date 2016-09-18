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

import zook.redis.connect.ZookConnect;
/**
 * 
 * @author Administrator
 *
 */
public class ZookReConnectWatcher implements Watcher{
	
	
	// 监控所有被触发的事件
	public void process(WatchedEvent event) { 
	
		if(event.getState().equals(KeeperState.Expired)){
			try {
				Map<String, ZooKeeper> pool = ZookConnect.getSessionPool();
				Set<Entry<String, ZooKeeper>> entrys=pool.entrySet();
				for(Entry<String, ZooKeeper> param : entrys){
					if(param.getValue().getState().equals(States.CLOSED)){
						ZooKeeper zooKeeper =new ZooKeeper(param.getKey(),2000,new ZookReConnectWatcher());
						param.setValue(zooKeeper);
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		System.out.println(" zooKeeper 已经触发了" + event.toString() + "事件！"); 

	}

}