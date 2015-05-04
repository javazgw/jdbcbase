/**
 * 
 */
package com.base.myproject.server.init;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * 取得配置文件的类
 * @author zgw
 *2009-12-17
 */
public class ProConfig {

	static ProConfig proconfig;
	Properties p = new Properties();
	private ProConfig()
	{
		init();
	}
	public static ProConfig getInstance()
	{
		
		if(proconfig==null)
		{
			proconfig = new ProConfig();
			
		}
		return proconfig;
	}
	void init()
	{
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
		//操作系统不同开始路径有所不同linux，mac类都是/开头
		if(System.getProperty("os.name").toUpperCase().indexOf("WINDOWS")>0)
		{
			path = path.substring(6,path.length());
			
		}
		else 
			path = path.substring(5, path.length());
		
		//部署模式
		  if(path.indexOf("/WEB-INF/classes")>0){
			  path = path.substring(0, path.indexOf("/WEB-INF/classes")+16);
		  }
		  else //if(path.indexOf("jar")>0)
		  {

			 //舍弃，在编译的时候已经将配置文件拷贝到bin下面了
				 // path = "d:";
			
		  }
		  System.out.println("path="+path);
		try {
			p.load(new FileReader(path+"/db.pro"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Properties getProperties()
	{
		return p;
	}
	
	public static void main(String[] argv)
	{
		System.out.println("开始");
		File f = new File("/Users/guangwangzhuang/zgw/eclipse/jdbcbase/bin/");
		System.out.println(f.list()[0]);
		for(String str:f.list())
		{
			System.out.println(str);
		}
		System.out.println(f.getParent());
		Properties p = new Properties();
		String path = ProConfig.class.getProtectionDomain().getCodeSource().getLocation().toString();
		path = path.substring(5, path.length());
		
		try {
			System.out.println("path="+path);
		//	p.load(new FileReader("./bin/db.pro"));
			p.load(new FileReader(path+"db.pro"));
			
			System.out.println(p);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println(	ProConfig.class.getProtectionDomain().getCodeSource().toString());
		System.out.println(System.getProperty("os.name"));
		System.out.println(System.getProperty("sun.cpu.isalist"));
	}
}
