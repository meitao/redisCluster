package zook.redis;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import zook.redis.Selection.SelectionMaster;
import zook.redis.connect.ZookConnect;
import zook.redis.register.RedisNodeBean;
import zook.redis.register.RegisterNode;
import zook.redis.watcher.ZookReConnectWatcher;

public class RedisClusterTest {

	public static final  String path = RedisContext.getPropertie(RedisContext.redisPath) ;

	public static final String sub = RedisContext.getPropertie(RedisContext.redisNodePath);

	 
	@Test
	public void test() {
		ZooKeeper zooKeeper = null;
		try {
			zooKeeper =new ZooKeeper("127.0.0.1:2183",2000,new ZookReConnectWatcher()  );
		} catch (IOException e) {
			e.printStackTrace();
		} 
		//将redis服务器信息注册至zookeeper，并监听集群的状态
		RegisterNode.register();

		ObjectMapper mapper = new ObjectMapper();

		InetAddress addr = null;
		try {
			 ZooKeeper zk = 	ZookConnect.getConnect() ;
			addr = InetAddress.getLocalHost();
			RedisNodeBean redisNodeBean =  new RedisNodeBean();
			redisNodeBean.setIp("127.0.0.1");;
			redisNodeBean.setPort(6379);
			redisNodeBean.setMaster(false);
			States aa =zk.getState();
			zk.create(path+sub,mapper.writeValueAsString(redisNodeBean).getBytes(),Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);

			RedisNodeBean redisNodeBean1 =  new RedisNodeBean();
			redisNodeBean1.setIp("127.0.0.1");
			redisNodeBean1.setPort(6381);
			redisNodeBean1.setMaster(false);
			zk.create(path+sub,mapper.writeValueAsString(redisNodeBean1).getBytes(),Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);

		}catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//选择redis中的master节点
		SelectionMaster.selection();
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
