/*
 * DBConnectionPool.java
 *
 * Created on 2006��8��20��, ����2:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.base.myproject.server.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;



class DBConnectionPool {
	// private int checkedOut;
	private Vector freeConnections = new Vector();

	private   int maxConn;

	private  String name;

	private  String password;

	private  String URL;

	private  String user;

	
	public DBConnectionPool(String name, String URL, String user,
			String password, int maxConn) {
		this.name = name;
		this.URL = URL;
		this.user = user;
		this.password = password;
		this.maxConn = maxConn;
	}


	public synchronized void freeConnection(Connection con) {
		
		try {
			if (con.isClosed()) {
				System.out.println("before freeConnection con is closed");
			}
			freeConnections.addElement(con);
			Connection contest = (Connection) freeConnections.lastElement();
			if (contest.isClosed()) {
				System.out.println("after freeConnection contest is closed");
			}
			notifyAll();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}


	public synchronized Connection getConnection() {
		Connection con = null;
		System.out.println("freeConnections.size() = "+freeConnections.size());
		if (freeConnections.size() > 0) {
			// ��ȡ���е�һ�����l��
			con = (Connection) freeConnections.firstElement();
			freeConnections.removeElementAt(0);
			try {
				if (con.isClosed()) {
					log("��l�ӳ�" + name + "ɾ��һ����Чl��");
					System.out.println("��l�ӳ�" + name + "ɾ��һ����Чl��");
					// �ݹ�����Լ�,�����ٴλ�ȡ����l��
					con = getConnection();
				}
			} catch (SQLException e) {
				log("��l�ӳ�" + name + "ɾ��һ����Чl��ʱ����");
				System.out.println("��l�ӳ�" + name + "ɾ��һ����Чl�ӳ��");
				// �ݹ�����Լ�,�����ٴλ�ȡ����l��
				con = getConnection();
			}
			if (freeConnections.size() > maxConn) {
				System.out.println(" ɾ��һ�����l�� ");
				releaseOne();
			}
		}

		else if ((maxConn == 0) || (freeConnections.size() < maxConn)) {
			con = newConnection();
		}

		return con;
	}

	public synchronized Connection returnConnection() {
		Connection con = null;
		//System.out.println("freeConnections.size()2 = "+freeConnections.size());
		// �������С�����l��,����һ����l��
              //  System.out.println("����l�ӳ���:"+freeConnections.size() );
              //  System.out.println("���l�ӳ���"+maxConn);
		System.out.println(freeConnections.size() +"  "+maxConn);
		if (freeConnections.size() < maxConn) {
			con = newConnection();
		}
		// ������ô������l�ӣ�����һ����õľ�l��
		else if (freeConnections.size() >= maxConn) {

			con = (Connection) freeConnections.firstElement();
			
			freeConnections.removeElementAt(0);
			
			try {
				if (con.isClosed()) {
				
					returnConnection();
				}
			} catch (SQLException e) {
			
				returnConnection();
			}
		}
		return con;
	}

	/**
	 * ��l�ӳػ�ȡ����l��.����ָ���ͻ������ܹ��ȴ���ʱ�� �μ�ǰһ��getConnection()����.
	 * 
	 * @param timeout
	 *            �Ժ���Ƶĵȴ�ʱ������
	 */
	public synchronized Connection getConnection(long timeout) {
		long startTime = new Date().getTime();
		Connection con;
		while ((con = getConnection()) == null) {
			try {
				wait(timeout);
			} catch (InterruptedException e) {
			}
			if ((new Date().getTime() - startTime) >= timeout) {
		
				return null;
			}
		}
		return con;
	}

	/**
	 * �ر�����l��
	 */
	public synchronized void release() {
		Enumeration allConnections = freeConnections.elements();
		while (allConnections.hasMoreElements()) {
			Connection con = (Connection) allConnections.nextElement();
			try {
				con.close();
				
			} catch (SQLException e) {
			
			}
		}
		freeConnections.removeAllElements();
	}

	/**
	 * �ر�һ��l��
	 */
	public synchronized void releaseOne() {
		if (freeConnections.firstElement() != null) {
			Connection con = (Connection) freeConnections.firstElement();
			try {
				con.close();
				
			} catch (SQLException e) {

				
			}
		} else {
			System.out
					.println("releaseOne() bug.......................................................");

		}
	}

	/**
	 * �����µ�l��
	 */
	public Connection newConnection() {
		Connection con = null;

		try {
			if (user == null) {
				
				
				con = DriverManager.getConnection(URL);
			} else {
			
				//System.out.println(this.user+"--"+user);
				con = DriverManager.getConnection(URL, user, password);
			}
			

		} catch (SQLException e) {
			System.out.println("..3");
//			return null;
			e.printStackTrace();
		}
		return con;
	}
	private void log(String str)
	{
		System.err.println("1 "+str);
	}
	private void log(Exception e,String str)
	{
		System.err.println("2 "+str+"\n"+e.getMessage());
	}
}
