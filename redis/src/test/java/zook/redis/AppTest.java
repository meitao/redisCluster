package zook.redis;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
   
    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	Map<String,String> sessionPool = new ConcurrentHashMap<String,String>();
    	sessionPool.put("1", "1");
    	
		Random random = new Random();
		int randomPos ;   
		for(int i=0;i<10;i++){
			randomPos = random.nextInt(3);  
			System.out.println(randomPos);
		}
    	
    	String[]a ="".split(",");
//    	System.out.println(a.length);
    }
}
