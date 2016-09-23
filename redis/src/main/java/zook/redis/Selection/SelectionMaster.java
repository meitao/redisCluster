package zook.redis.Selection;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import zook.redis.RedisContext;
import zook.redis.connect.RedisConnect;
import zook.redis.connect.ZookConnect;
import zook.redis.register.RedisNodeBean;
import zook.redis.watcher.ChildWatcher;

public class SelectionMaster {

	public static  String path = RedisContext.getPropertie(RedisContext.redisPath) ;

	private static Logger log = LoggerFactory.getLogger(SelectionMaster.class);  
	
	/**
	 * 当redis master节点down了的时候，触发重新选举;选举之前要先去除之前的子节点归属的master，重新选择master;
	 * @param args
	 */
	public static void selection(){
		
		log.info("-----开始redis  master的选举 ----");

		ZooKeeper zk = 	ZookConnect.getConnect() ;
		// 创建另外一个子目录节点
		try {

			zk.getChildren(path, new ChildWatcher(zk));
			ObjectMapper mapper = new ObjectMapper();
			// 获取自增临时节点最早创建的znode
			List<String> list = zk.getChildren(path, null);
			Collections.sort(list);
			String masterPath = list.get(0);
			String master = new String(zk.getData(path+"/"+masterPath, false, null));
			RedisNodeBean masterNode  = mapper.readValue(master, RedisNodeBean.class);
			Jedis jedis = null;
		    jedis =RedisConnect.getConnect(masterNode.getIp(),masterNode.getPort());
		    jedis.slaveofNoOne();
		    
		    masterNode.setMaster(true);
		    zk.setData(path+"/"+masterPath, mapper.writeValueAsBytes(masterNode), -1);
		    log.info("master 节点 为"+masterNode.getIp()+":"+masterNode.getPort());
			//将节点设置为master
			//发送slaveof no one 命令给 redis的 slaves
			for(int i=1;i<list.size();i++){
				String slave = new String(zk.getData(path+"/"+list.get(i), false, null));
				RedisNodeBean slaveNode  = mapper.readValue(slave , RedisNodeBean.class);
			    jedis =RedisConnect.getConnect(slaveNode.getIp(),slaveNode.getPort());
				jedis.slaveofNoOne();
				//发送slaveof master 
				jedis.slaveof(masterNode.getIp(), masterNode.getPort());
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
	}

}
