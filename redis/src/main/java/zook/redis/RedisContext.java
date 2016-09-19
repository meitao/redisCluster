package zook.redis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Administrator
 *
 */
public class RedisContext {
	
	public static String redisPort = "redisPort" ;
	
	public static String zookeeperNode="zookeeperNode" ;
	
	public static String redisPath="redisPath" ;
	
	public static String redisNodePath="redisNodePath" ;
	
	private static Logger log = LoggerFactory.getLogger(RedisContext.class);   

	private static Properties env  ;

	static 
	{
		env = new Properties();
		log.info("加载  properties文件 ------");
		try {
//			LocateRegistry.createRegistry(3000);
			env.load(new FileInputStream("/home/config/sys.properties"));
			//			env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
			//			env.setProperty(Context.PROVIDER_URL, "rmi://localhost:3000");
			//			ctx = new InitialContext(env);
			//			RmiSimple rmiSimple = new RmiSimple();
			//			rmiSimple.setValue("dddd");
			//			ctx.bind("rmiSimple", rmiSimple);


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public static Properties  getContext(){
		return env;
	}
	
	
	public static String getPropertie(String id){
		return env.getProperty(id);
	}

}
