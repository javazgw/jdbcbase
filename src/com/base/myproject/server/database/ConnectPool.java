/*
 * ConnectPool.java
 *
 * Created on 2006��8��20��, ����2:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.base.myproject.server.database;

/**
 *
 * @author zgw001
 */




import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import com.base.myproject.server.init.ProConfig;


public class ConnectPool { 
	static public ConnectPool instance; // Ψһʵ��

	static public int clients;

	public Vector drivers = new Vector(); // ��

	public PrintWriter log;

	public Hashtable pools = new Hashtable(); // l��
	
	Properties dbProps = new Properties();


	static synchronized public ConnectPool getInstance() {
		if (instance == null) {
			instance = new ConnectPool();
		}

		clients++;
	//	System.out.println("clients ����"+clients);

		return instance;
	}

	public ConnectPool() {
		init();
	}


	public void freeConnection(String name, Connection con) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			pool.freeConnection(con);
		} else {
			System.out.println("pool ==null");
		}
		clients--;
	}


	public Connection getConnection(String name) {
		java.util.Enumeration enu= pools.keys();
//		while (enu.hasMoreElements())
//		{
//			System.out.println("pools="+enu.nextElement());
//		}
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		
		if (pool != null) {
			// return pool.getConnection();
			return pool.returnConnection();
		}
		return null;
	}

	/**
	 * 被逼无奈写下此函数，原因是连接池出现了bug
	 * 在事务碰到了多个post时候会出现setautocommit错误
	 * 	ConnectPool  cp = ConnectPool.getInstance();
			cp.getConnectionOne("test");
	 * @param name
	 * @return
	 */
	public Connection getConnectionOne(String name) {
		java.util.Enumeration enu= pools.keys();
//		while (enu.hasMoreElements())
//		{
//			System.out.println("pools="+enu.nextElement());
//		}
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		
		if (pool != null) {
			// return pool.getConnection();
			return pool.newConnection();
		}
		return null;
	}


	public Connection getConnection(String name, long time) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			return pool.getConnection(time);
		}
		return null;
	}


	public synchronized void release() {
		// 
		if (--clients != 0) {
			return;
		}

		Enumeration allPools = pools.elements();
		while (allPools.hasMoreElements()) {
			DBConnectionPool pool = (DBConnectionPool) allPools.nextElement();
			pool.release();
		}
		Enumeration allDrivers = drivers.elements();
		while (allDrivers.hasMoreElements()) {
			Driver driver = (Driver) allDrivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);

				log("����JDBC����� " + driver.getClass().getName() + "��ע��");
			} catch (SQLException e) {
				log(e, "�޷���������JDBC������ע��: " + driver.getClass().getName());
			}
		}
	}


	private void createPools(Properties props) {
		Enumeration propNames = props.propertyNames();
		while (propNames.hasMoreElements()) {
			String name = (String) propNames.nextElement();
			if (name.endsWith(".url")) {
				String poolName = name.substring(0, name.lastIndexOf("."));
				String url = props.getProperty(poolName + ".url");
				if (url == null) {
					log("url" + poolName + "");
					System.out.println("url"+url);
					continue;
				}
				String user = props.getProperty(poolName + ".user");
				String password = props.getProperty(poolName + ".password");
				String maxconn = props.getProperty(poolName + ".maxconn", "0");
				int max;
				try {
					max = Integer.valueOf(maxconn).intValue();
				} catch (NumberFormatException e) {
					log("连接数错误: " + maxconn + " : " + poolName);
					max = 0;
				}
				DBConnectionPool pool = new DBConnectionPool(poolName, url,
						user, password, max);
				pools.put(poolName, pool);
				//log("�ɹ�����l�ӳ�2" + poolName);
			}
		}
	}


	private void init() {
		try {
			Properties p = new Properties();
			
			
			dbProps = getProperties();
			
			
//			 DriverManager.registerDriver(new   com.microsoft.jdbc.sqlserver.SQLServerDriver());
//         	
//        	 Connection connection = DriverManager.getConnection(
//        			"jdbc:microsoft:sqlserver://127.0.0.1:1433;databaseName=test","sa","");
//        	
                             
/*			dbProps.setProperty("drivers", "com.microsoft.jdbc.sqlserver.SQLServerDriver");
		//	dbProps.setProperty("logfile", "c:\\DBConnectPool-log.txt");
			dbProps.setProperty("test.maxconn", "10");
			dbProps.setProperty("test.url", "jdbc:microsoft:sqlserver://127.0.0.1:1433;databaseName=base");
//			dbProps.setProperty("test.url", "jdbc:microsoft:sqlserver://119.146.223.195:1433;databaseName=base");
//			dbProps.setProperty("test.user", "base");
//			dbProps.setProperty("test.password", "basewang");
			dbProps.setProperty("test.user", "sa");
			dbProps.setProperty("test.password", "");*/
			
			
			dbProps.setProperty("drivers", ProConfig.getInstance().getProperties().get("drivers").toString());
			
				dbProps.setProperty("test.maxconn", ProConfig.getInstance().getProperties().get("maxconn").toString());
				dbProps.setProperty("test.url", ProConfig.getInstance().getProperties().get("url").toString());

				dbProps.setProperty("test.user", ProConfig.getInstance().getProperties().get("user").toString());
				dbProps.setProperty("test.password", ProConfig.getInstance().getProperties().get("password").toString());
				
				Object names =  ProConfig.getInstance().getProperties().get("names");
				if(names!=null)
				for(String str:names.toString().split(","))
				{
					dbProps.setProperty(str+".url", ProConfig.getInstance().getProperties().get(str+".url").toString());
					dbProps.setProperty(str+".user", ProConfig.getInstance().getProperties().get(str+".user").toString());
					dbProps.setProperty(str+".password", ProConfig.getInstance().getProperties().get(str+".password").toString());
					dbProps.setProperty(str+".maxconn", ProConfig.getInstance().getProperties().get(str+".maxconn").toString());
				}
        
//			dbProps.setProperty("drivers", "com.inet.tds.TdsDriver");
//			
//			//	dbProps.setProperty("logfile", "c:\\DBConnectPool-log.txt");
//				dbProps.setProperty("test.maxconn", "10");
//				dbProps.setProperty("test.url", "jdbc:inetdae7:127.0.0.1:1433?charset=gb2312&database=test");
//				dbProps.setProperty("test.user", "sa");
//				dbProps.setProperty("test.password", "");
	/*
		dbProps.setProperty("drivers", "com.mysql.jdbc.Driver");
			
			//	dbProps.setProperty("logfile", "c:\\DBConnectPool-log.txt");
				dbProps.setProperty("test.maxconn", "10");
				dbProps.setProperty("test.url", "jdbc:mysql://localhost/zz?useUnicode=true&characterEncoding=GBK");
				dbProps.setProperty("test.user", "root");
				dbProps.setProperty("test.password", "123321");
	*/
                   /**
                    * jbdcurl=jdbc\:microsoft\:sqlserver\://192.168.0.32\:1433;databaseName\=kfdata
driverText=com.microsoft.jdbc.sqlserver.SQLServerDriver
                    */
                        
                        
                        

			String logFile = dbProps.getProperty("logfile",
					"DBConnectionManager.log");
//			try {
//
//				log = new PrintWriter(new FileWriter(logFile, true), true);
//			} catch (IOException e) {
//				System.err.println("�޷�����־�ļ�: " + logFile);
//				log = new PrintWriter(System.err);
//			}
			loadDrivers(dbProps);
			createPools(dbProps);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public  void setProperties(Properties dbProps)
	{
		//Properties dbProps = new Properties();
		this.dbProps = dbProps;
		//return 	dbProps;
		
	}
	public Properties getProperties()
	{
	
		return 	dbProps;
	}
	
	/**
	 * 171 * װ�غ�ע������JDBC����� 172 * 173 *
	 * 
	 * @param props
	 *            ���� 174
	 */
	private void loadDrivers(Properties props) {
		String driverClasses = props.getProperty("drivers");
		StringTokenizer st = new StringTokenizer(driverClasses);
		while (st.hasMoreElements()) {
			String driverClassName = st.nextToken().trim();
			try {
				Driver driver = (Driver) Class.forName(driverClassName)
						.newInstance();
				DriverManager.registerDriver(driver);
				drivers.addElement(driver);
				System.out.println(driverClassName);
				//log("�ɹ�ע��JDBC�����" + driverClassName);
			} catch (Exception e) {
				log("�޷�ע��JDBC�����: " + driverClassName + ", ����: " + e);
			}
		}
	}

	/**
	 * ���ı���Ϣд����־�ļ�
	 */
	private void log(String msg) {
	//	log.println(new Date() + ": " + msg);
                System.out.println(new Date()+":"+msg);
	}

	/**
	 * ���ı���Ϣ���쳣д����־�ļ�
	 */
	private void log(Throwable e, String msg) {
	//	log.println(new Date() + ": " + msg);
		e.printStackTrace(log);
	}
        
      
        
 
}

