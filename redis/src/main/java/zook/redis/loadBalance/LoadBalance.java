package zook.redis.loadBalance;



/**
 * 软负载均衡
 * @author Administrator
 *
 */
public interface LoadBalance {
	/**
	 * 负载均衡
	 * @param param
	 * @return
	 */
	public String loadBalance();
	
}
