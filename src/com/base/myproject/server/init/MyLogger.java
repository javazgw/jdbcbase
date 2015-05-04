/**
 * 
 */
package com.base.myproject.server.init;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author zgw
 *2009-12-29
 */
public class MyLogger extends Logger {
	 static String FQCN = MyLogger.class.getName() + ".";
	protected MyLogger(String name) {
		super(name);

		
	}
	 public 
	  void debug(Object message) {
	    super.log(FQCN, Level.DEBUG, message + " world.", null);    
	  }

	 static Logger logger =null;// Logger.getLogger(MyLogger.class.getName());
	 
	static public Logger getInstance()
	 {
		 if(logger==null)
			 
		 {
			 
			 String path = MyLogger.class.getProtectionDomain().getCodeSource().getLocation().toString();
			 System.out.println(path);
			
			 //path = path.substring(6, path.length());
				//操作系统间的不同导致路径不同mac的路径以 / 开头
				if(System.getProperty("os.name").toUpperCase().indexOf("WINDOWS")>0)
					path = path.substring(5, path.length());
				else
					path = path.substring(5, path.length());	
				
				
//				  if(path.indexOf("/WEB-INF/lib")>0){
//					  path = path.substring(0, path.indexOf("/WEB-INF/lib"));
//					  path+="/WEB-INF/lib";
//				  }

             if(path.indexOf("/WEB-INF/classes")>0){
                 path = path.substring(0, path.indexOf("/WEB-INF/classes")+16);
             }
				  
				  else
				  {
					//舍弃，在编译的时候已经将配置文件拷贝到bin下面了
					//  path = "d:";
				  }
				 System.out.println("log4j path = " +path+"/Serverlog4j.properties");
				//PropertyConfigurator.configure ( "d:/Serverlog4j.properties" ) ;
				  PropertyConfigurator.configure ( path+"/Serverlog4j.properties" ) ;
			 logger = Logger.getLogger(MyLogger.class.getName());
		 }
		 
		 return logger;
	 }
	 public static void main(String[] argc)
	 {
		//String path =" D:/zgw/myproject/workspace/eclipse3.5/gwtbase/war/WEB-INF/classes/Serverlog4j.properties";
		String path ="d:/temp/aa.jar";
		
		//path = path.substring(0, path.indexOf("/WEB-INF/classes")+16);
		
		path = path.substring(0, path.lastIndexOf("/"));
		System.out.println("path="+path);
		System.out.println(""+path);
		 for(int i=0;i<1;i++)
		 {
			 MyLogger.getInstance().debug("ddd");
		 logger.info("ddd");
		 }
//		 MyLogger mylogger = new MyLogger("dd");
//		 mylogger.debug("333");
	 }
}
