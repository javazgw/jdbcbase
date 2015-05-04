/**
 * 
 */
package com.base.myproject.server.database.poolx;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import com.base.myproject.server.init.ProConfig;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;

/**
 * @author guangwangzhuang 负责初始化和管理连接池，超过数量释放否则新建立连接
 */
public class ConnectPoolxmgr {

	Vector<ConnectionObject> pools = new Vector<ConnectionObject>();
	int poolnum = 10;
	Properties dbProps = new Properties();
	static ConnectPoolxmgr cpxmgr = null;

	public Vector<Driver> drivers = new Vector<Driver>();

	private ConnectPoolxmgr() {

		init();
	}

	public static ConnectPoolxmgr getInstance() {
		if (cpxmgr == null) {
			cpxmgr = new ConnectPoolxmgr();
		}
		return cpxmgr;
	}

	public synchronized void freeusing(ConnectionObject con) throws SQLException {
//		if(con.stmt!=null)
//			con.stmt.close();
		// 超出最大，关闭连接
		if (pools.size() > poolnum) {
			con.getCon().close();

			pools.remove(con);

		}
		// 否则设置已经不是alive了，在pools中继续存在
		else {
			con.freeconnect();
		}

	}

	/**
	 * 关闭所有连接，清空连接池
	 * 
	 * @throws SQLException
	 */
	public synchronized void freeall() throws SQLException {

		for (ConnectionObject con : pools) {
			con.getCon().close();
		}
		pools.clear();
	}

	/**
	 * 获得连接 查看连接池里有没有没有在用的连接，有就返回，否则就重建一个，并且加入到连接池中
	 * 线程安全,要做到只要能获取到的con都是合法的.
	 * @return
	 */
	public synchronized ConnectionObject getConnectionObject(String name) {
		System.out.println("pools size = "+pools.size());
		for (ConnectionObject con : pools) {
			try {
				//如果没有在使用(isalive) 和 这是合法的,避免数据库太久没有使用连接断开而连接词还存在连接这个时候会宝错的
				//于是多加了con.getCon().isValid(10000)的判断
				
				
				if (!con.isIsalive() && con.getCon().isValid(10000))
				{
					con.setIsalive(true);
					return con;
				}
					
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//如果运行到此处,那么有两种情况1,新的开始pools里面没有内容,2.数据库断开了pools里面有很多已经断开的链接需要即使清理
		Vector<ConnectionObject> poolstemp = new Vector<ConnectionObject>();
		for (ConnectionObject con : pools) {
			try {
				if ( !con.getCon().isValid(10000))
				{
					poolstemp.add(con);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		pools.removeAll(poolstemp);
		
		
		ConnectionObject con = new ConnectionObject(dbProps.getProperty(name
				+ ".url"), dbProps.getProperty(name + ".user"),
				dbProps.getProperty(name + ".password"));
		con.setIsalive(true);
		pools.add(con);
		return con;
	}

	public Connection getConnection(String name) {
		return getConnectionObject(name).getCon();
	}

	/**
	 * 加载链接的配置
	 */
	private void init() {
		try {
			// Properties p = new Properties();

			dbProps = getProperties();

			dbProps.setProperty("drivers", ProConfig.getInstance()
					.getProperties().get("drivers").toString());

			dbProps.setProperty("test.maxconn", ProConfig.getInstance()
					.getProperties().get("maxconn").toString());
			dbProps.setProperty("test.url", ProConfig.getInstance()
					.getProperties().get("url").toString());

			dbProps.setProperty("test.user", ProConfig.getInstance()
					.getProperties().get("user").toString());
			dbProps.setProperty("test.password", ProConfig.getInstance()
					.getProperties().get("password").toString());

			Object names = ProConfig.getInstance().getProperties().get("names");
			if (names != null)
				for (String str : names.toString().split(",")) {
					dbProps.setProperty(str + ".url", ProConfig.getInstance()
							.getProperties().get(str + ".url").toString());
					dbProps.setProperty(str + ".user", ProConfig.getInstance()
							.getProperties().get(str + ".user").toString());
					dbProps.setProperty(
							str + ".password",
							ProConfig.getInstance().getProperties()
									.get(str + ".password").toString());
					dbProps.setProperty(str + ".maxconn", ProConfig
							.getInstance().getProperties()
							.get(str + ".maxconn").toString());
				}

			/**
			 * jbdcurl=jdbc\:microsoft\:sqlserver\://192.168.0.32\:1433;
			 * databaseName\=kfdata
			 * driverText=com.microsoft.jdbc.sqlserver.SQLServerDriver
			 */

			String logFile = dbProps.getProperty("logfile",
					"DBConnectionManager.log");

			loadDrivers(dbProps);
			createPools(dbProps);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Properties getProperties() {

		return dbProps;
	}

	public void addConnectionObject(ConnectionObject con)
	{
		pools.add(con);
	}
	/**
	 * 注册配置文件中定义的数据库的驱动
	 * 有可能有几个驱动，在有几个驱动的情况下记得加载对应的jar
	 * @param props
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

			} catch (Exception e) {

			}
		}
	}

	/**
	 * 开始的时候就创建配置文件里指定的最大连接数
	 * @param props
	 * @throws SQLException 
	 */
	private void createPools(Properties props) throws SQLException {
		Enumeration propNames = props.propertyNames();
		String user=null,password=null,maxconn=null,url = null;
		while (propNames.hasMoreElements()) {
			String name = (String) propNames.nextElement();
			if (name.endsWith(".url")) {
				String poolName = name.substring(0, name.lastIndexOf("."));
				 url = props.getProperty(poolName + ".url");
				if (url == null) {
					// log("url" + poolName + "");
					System.out.println("url" + url);
					continue;
				}
				 user = props.getProperty(poolName + ".user");
				 password = props.getProperty(poolName + ".password");
				 maxconn = props.getProperty(poolName + ".maxconn", "0");
				
				try {
					poolnum = Integer.valueOf(maxconn).intValue();
				} catch (NumberFormatException e) {
					// log("连接数错误: " + maxconn + " : " + poolName);
					poolnum = 10;
				}
				
				
			}
		}
		
		for(int i =0;i<poolnum;i++)
		{
		ConnectionObject co = new ConnectionObject( url,
				user, password);
		pools.add(co);
		freeusing(co);
		}
	}

	public static void main(String[] argc) throws Exception {
		final long t1 = System.currentTimeMillis();
		/*for (int i = 0; i < 100; i++) {
			ConnectPoolxmgr.getInstance();
//			System.out.println(ConnectPoolxmgr.getInstance().getProperties()
//					.get("test.url"));
			ConnectionObject con = ConnectPoolxmgr.getInstance()
					.getConnectionObject("test");

			// Connection con =
			// ConnectPoolxmgr.getInstance().getConnection("test");

			Statement stmt = con.getCon().createStatement();
			ResultSet result = stmt
					.executeQuery("select bcode,bname from bcode");
			while (result.next()) {
//				System.out.println(result.getString("bcode") + ":"
//						+ result.getString("bname"));
			}

			stmt.close();
		
			ConnectPoolxmgr.getInstance().freeusing(con);
		}*/
		
		for(int i = 0;i<20;i++)
		{
			System.out.println("回车继续");
			Scanner scanner = new Scanner(System.in);
			 String str = scanner.nextLine();
			ConnectionObject con = ConnectPoolxmgr.getInstance()
					.getConnectionObject("test");
		
	         
			//Thread.sleep(4000);
			System.out.println("index = "+i+" isvalid==="+con.getCon().isValid(1000));
			con.UpdateException("insert into taobao_item (detail_url) values("+i+")");
			
			ConnectPoolxmgr.getInstance().freeusing(con);
		
		}
		Thread t = new Thread()
		{
			public void run()
			{
				System.out.println(ConnectPoolxmgr.getInstance().pools.size());
				for(int i = 0;i<500;i++)
				{
				Vector<String> sqlv = new Vector<String>();
				sqlv.add("insert into taobao_item (detail_url,title) values('1','zgw')");
				sqlv.add("insert into taobao_item (detail_url,title) values('2','zgw2')");
				ConnectionObject con = ConnectPoolxmgr.getInstance()
						.getConnectionObject("test");
				try {
					con.handleTransactionX(sqlv);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					ConnectPoolxmgr.getInstance().freeusing(con);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				System.out.println(ConnectPoolxmgr.getInstance().pools.size());
				long t2 = System.currentTimeMillis();
				System.out.println((t2 - t1)/1000 );
				
				System.out.println("end..");
				
			
			}
		};
	
		t.start();
		
		Thread th2 = new Thread()
		{
			public void run()
			{
				System.out.println(ConnectPoolxmgr.getInstance().pools.size());
				for(int i = 0;i<500;i++)
				{
				Vector<String> sqlv = new Vector<String>();
				sqlv.add("insert into taobao_item (detail_url,title) values('1','zgw')");
				sqlv.add("insert into taobao_item (detail_url,title) values('2','zgw2')");
				ConnectionObject con = ConnectPoolxmgr.getInstance()
						.getConnectionObject("test");
				try {
					con.handleTransactionX(sqlv);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					ConnectPoolxmgr.getInstance().freeusing(con);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				System.out.println(ConnectPoolxmgr.getInstance().pools.size());
				long t2 = System.currentTimeMillis();
				System.out.println((t2 - t1)/1000 );
				
				System.out.println("end..");
				
			
			}
		};
	
		th2.start();
		
		try {
			ConnectPoolxmgr.getInstance().freeall();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
