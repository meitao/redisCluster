package zook.redis;

import java.io.IOException;

import zook.redis.Selection.SelectionMaster;
import zook.redis.register.RegisterNode;

/**
 * Hello world!
 *
 */
public class RedisCluster 
{
	public static void main( String[] args )
	{
		//启动redis服务器
		Runtime rt = Runtime.getRuntime(); 
		String file = "start.bat"; 
		try {
			rt.exec("cmd.exe /c start " + file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		//将redis服务器信息注册至zookeeper，并监听集群的状态
		RegisterNode.register();
		//选择redis中的master节点
		SelectionMaster.selection();

	}

}
