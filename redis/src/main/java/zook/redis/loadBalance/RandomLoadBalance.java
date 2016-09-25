package zook.redis.loadBalance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class RandomLoadBalance implements LoadBalance{
	/**
	 * 负载均衡服务器列表
	 */
	private List<String>serverList ;
	
	public RandomLoadBalance(Map<String, Integer> param){
		
		Set<String> keySet = param.keySet();
		Iterator<String> it = keySet.iterator();
		//ip列表list
		serverList = new ArrayList<String>();   
		while (it.hasNext()) {                    
			String server = it.next();
			Integer weight = param.get(server);                
			for (int i = 0; i < weight; i++) {
				serverList.add(server);
			}
		} 
		
	}
	

	 

	@Override
	public String loadBalance() {
		Random random = new Random();
		int randomPos = random.nextInt(serverList.size());              
		return serverList.get(randomPos);    
	}

}
