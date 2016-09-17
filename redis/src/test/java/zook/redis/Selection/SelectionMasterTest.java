package zook.redis.Selection;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import zook.redis.connect.ZookConnect;
import zook.redis.register.RedisNodeBean;

public class SelectionMasterTest {

	@Test
	public void test() {
		String path ="/test1" ;
		String sub ="/aaa" ;

		ZooKeeper zk = 	ZookConnect.getConnect() ;
		ObjectMapper mapper = new ObjectMapper();
		RedisNodeBean nodeBean1 =  new RedisNodeBean();
		RedisNodeBean nodeBean2 =  new RedisNodeBean();
		RedisNodeBean nodeBean3 =  new RedisNodeBean();
		try {
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			

//			nodeBean2.setIp(addr.getHostAddress());
			nodeBean2.setIp("127.0.0.1");
			nodeBean2.setPort(6380);
			nodeBean2.setMaster(false);

			nodeBean3.setIp("127.0.0.1");
			nodeBean3.setPort(6381);
			nodeBean3.setMaster(false);

			zk.create(path+sub,mapper.writeValueAsString(nodeBean2).getBytes(),Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
			zk.create(path+sub,mapper.writeValueAsString(nodeBean3).getBytes(),Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
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

		SelectionMaster.selection();
	}

}
