package zook.redis.connect;

public class Test3 extends Test  {
	public static void main(String args[]){
		   Test3 c = new Test3();  
	        System.out.println(c.nonStaticStr);  
	        System.out.println(c.staticStr);  
	        c.staticMethod();//输出的结果都是父类中的非静态属性、静态属性和静态方法,推出静态属性和静态方法可以被继承  
	          
	        System.out.println("-------------------------------");  
	          
	        Test c1 = new Test3();  
	        System.out.println(c1.nonStaticStr);  
	        System.out.println(c1.staticStr);  
	        c1.staticMethod();//结果同上，输出的结果都是父类中的非静态属性、静态属性和静态方法,推出静态属性和静态方法可以被继承  
	      
	        System.out.println("-------------------------------");  
	        Test1 b = new Test1();  
	        System.out.println(b.nonStaticStr);  
	        System.out.println(b.staticStr);  
	        b.staticMethod();  
	          
	        System.out.println("-------------------------------");  
	        Test b1 = new Test1();  
	        System.out.println(b1.nonStaticStr);  
	        System.out.println(b1.staticStr);  
	        b1.staticMethod();//结果都是父类的静态方法，说明静态方法
	}
}
