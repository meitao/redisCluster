package zook.redis.watcher;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zook.redis.Selection.SelectionMaster;
import zook.redis.register.RedisNodeBean;

public class ChildWatcher implements Watcher{

	private static Logger log = LoggerFactory.getLogger(ChildWatcher.class);  
	
	private ZooKeeper zooKeeper ;

	public ChildWatcher(ZooKeeper zk){
		this.zooKeeper = zk ;
	}
	// 监控所有被触发的事件
	public void process(WatchedEvent event) { 
		try {
			boolean isSelectionMaster = true ;
			List<String>  list = zooKeeper.getChildren(event.getPath(), this);
			
			log.info(event.getPath()+"下的节点 >> "+list.toString() ); 
			
			ObjectMapper mapper = new ObjectMapper();
			if(list.size()<=1){
				return ;
			}
			//如果redis服务器的master节点down掉要重新选master
			for(String nodePath:list){
				String master = new String(zooKeeper.getData(event.getPath()+"/"+nodePath, false, null));
				RedisNodeBean node  = mapper.readValue(master, RedisNodeBean.class);
				if(node.isMaster()){
					isSelectionMaster = false ;
					break ;
				}
			}
			if(isSelectionMaster){
				SelectionMaster.selection();
			}
		} catch (KeeperException e) {
			log.error("KeeperException",e);
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("InterruptedException",e);
			e.printStackTrace();
		} catch (JsonParseException e) {
			log.error("JsonParseException",e);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.error("JsonMappingException",e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException",e);
			e.printStackTrace();
		}
		log.info("触发事件 >>>" + event.toString() ); 

	} 
}