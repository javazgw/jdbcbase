/**
 * 
 */
package com.base.myproject.server.database.poolx;

import java.sql.Connection;
import java.util.Vector;

/**
 * @author guangwangzhuang
 * 负责查询，更新，事务等操作，通过@ConnectPoolxmgr获得数据库连接，并在此处进行释放
 * 一般代码调用不直接调用ConnectPoolxmgr。
 */
public class ConnectPoolx {
	static public int clients;
	static public ConnectPoolx instance;
	
	public Vector drivers = new Vector();
	Vector<Connection> pools = new Vector<Connection>();
	static synchronized public ConnectPoolx getInstance() {
		if (instance == null) {
			instance = new ConnectPoolx();
		}

		clients++;
	//	System.out.println("clients ����"+clients);

		return instance;
	}
	public ConnectPoolx() {
		
	}
	public void freeConnection(String name, Connection con) {
	
	}
	

	

}
