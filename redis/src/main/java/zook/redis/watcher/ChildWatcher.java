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

import zook.redis.RedisContext;
import zook.redis.Selection.SelectionMaster;
import zook.redis.register.RedisNodeBean;

public class ChildWatcher implements Watcher{

//	public static final  String path = RedisContext.getPropertie(RedisContext.redisPath) ;
	
	private ZooKeeper zooKeeper ;

	public ChildWatcher(ZooKeeper zk){
		this.zooKeeper = zk ;
	}
	// 监控所有被触发的事件
	public void process(WatchedEvent event) { 
		try {
			boolean isSelectionMaster = true ;
			List<String>  list = zooKeeper.getChildren(event.getPath(), this);
			
			System.out.println(" ChildWatcher 下的节点数"+list.toString() ); 
			
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
		}catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(" ChildWatcher 已经触发了" + event.toString() + "事件！"); 

	} 
}