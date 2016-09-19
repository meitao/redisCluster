package zook.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zook.redis.Selection.SelectionMaster;
import zook.redis.register.RegisterNode;

/**
 * Hello world!
 *
 */
public class RedisCluster 
{
	private static Logger log = LoggerFactory.getLogger(RedisCluster.class);  
	
	public static void main( String[] args )
	{
		//启动redis服务器
//		Runtime rt = Runtime.getRuntime(); 
//		String file = "../start.bat"; 
//		try {
//			rt.exec("cd ..");
//			rt.exec("redis-server  redis.windows.conf");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 

		//将redis服务器信息注册至zookeeper，并监听集群的状态
		try{
			RegisterNode.register();
			//选择redis中的master节点
			SelectionMaster.selection();
		}catch(Exception e){
			log.error("Exception :", e);
			e.printStackTrace();
		}
	

	}

}
