package zook.redis.register;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zook.redis.RedisContext;
import zook.redis.connect.ZookConnect;
import zook.redis.heartPing.PingRedis;
import zook.redis.watcher.ChildWatcher;

public class RegisterNode {

	public static final  String path = RedisContext.getPropertie(RedisContext.redisPath) ;

	public static final String sub = RedisContext.getPropertie(RedisContext.redisNodePath);

	public static  ZooKeeper zk = 	ZookConnect.getConnect() ;

	private static Logger log = LoggerFactory.getLogger(RegisterNode.class);   

	public static void register(){

		// 创建另外一个子目录节点
		try {
			log.info(" 开始为redis 注册 zookeeper节点 ");
			
//			zk.getChildren(path, new ChildWatcher(zk)) ;
			ObjectMapper mapper = new ObjectMapper();
			RedisNodeBean redisNodeBean =  new RedisNodeBean();

			InetAddress addr = InetAddress.getLocalHost();
			redisNodeBean.setIp(addr.getHostAddress());
			redisNodeBean.setPort(Integer.parseInt(RedisContext.getPropertie(RedisContext.redisPort)));
			redisNodeBean.setMaster(false);

			zk.create(path+sub,mapper.writeValueAsString(redisNodeBean).getBytes(),Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);

			monitor();
		} catch (KeeperException e) {
			log.error("KeeperException : ",e);
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("InterruptedException : ",e);
			e.printStackTrace();
		} catch (JsonGenerationException e) {
			log.error("JsonGenerationException : ",e);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.error("JsonMappingException : ",e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException : ",e);
			e.printStackTrace();
		}
	}
	/**
	 *  监听redis服务器运行状态(如果redis服务器down掉要删除redis在服务器上的临时节点)
	 */
	public static void monitor(){
		log.info("监听redis服务器运行状态---");
		new Thread( new Runnable() {

			@Override
			public void run() {
				try {
					while(true){

						String ping = PingRedis.heartPing();

						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}catch(Exception e){

							e.printStackTrace();
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					try {
						//删除临时节点
						zk.delete(path+sub, 0);
						log.info("删除zookeeper上临时节点>>"+path+sub);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					} catch (KeeperException e1) {
						e1.printStackTrace();
					}
				}
			}
		}).start();
	}
}
