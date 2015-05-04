/**
 * 
 */
package com.base.myproject.server.python;

/**
 * @author Administrator
 *
 */
public class Test1 {

	static Test1 tt = new Test1();
	public Test1()
	{
		
	}
	public static Test1 getTest1()
	{
		return tt;
	}
	public String getSTR()
	{
		System.out.println("test system	");
		return "test1";
	}
}
