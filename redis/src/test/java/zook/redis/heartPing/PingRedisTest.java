package zook.redis.heartPing;

import junit.framework.TestCase;

import org.junit.Test;

public class PingRedisTest extends TestCase
{ 
	@Test
	public void test() {
		PingRedis.heartPing();
	}
}

