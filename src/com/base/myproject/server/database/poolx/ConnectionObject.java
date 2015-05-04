/**
 * 
 */
package com.base.myproject.server.database.poolx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Vector;

import com.base.myproject.server.database.ConnectPool;
import com.base.myproject.server.init.MyLogger;

/**
 * @author guangwangzhuang
 * 
 */
public class ConnectionObject {
	Connection con;
	// Statement stmt;
	long lastconectiontime;
	boolean isalive = true;
	private String name;

	private String password;

	private String URL;

	private String user;

	// public ConnectionObject(String name, String URL, String user,
	// String password) {
	// this.name = name;
	// this.URL = URL;
	// this.user = user;
	// this.password = password;
	// lastconectiontime = System.currentTimeMillis();
	// }

	public ConnectionObject(String URL, String user, String password) {
		// this.name = name;
		this.URL = URL;
		this.user = user;
		this.password = password;
		lastconectiontime = System.currentTimeMillis();
		newConnection();

		// ConnectPoolxmgr.getInstance().addConnectionObject(this);
	}

	public ConnectionObject(String URL) {
		this(URL, null, null);

	}

	public void newConnection() {

		try {
			if (user == null) {

				con = DriverManager.getConnection(URL);
			} else {

				con = DriverManager.getConnection(URL, user, password);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public long getLastconectiontime() {
		return lastconectiontime;
	}

	public void setLastconectiontime(long lastconectiontime) {
		this.lastconectiontime = lastconectiontime;
	}

	public boolean isIsalive() {
		
		
		return isalive ;
	}

	public void setIsalive(boolean isalive) {
		this.isalive = isalive;
	}

	/**
	 * 用完记得调用 ConnectPoolxmgr.getInstance().freeusing(con);
	 * 
	 * @param SqlStr
	 * @return
	 * @throws Exception
	 */
	public ResultSet executeQuery(String SqlStr) throws Exception {
		return executeQuery("test", SqlStr);
	}

	/**
	 * 用完记得调用 @see ConnectPoolxmgr.getInstance().freeusing(con);
	 * 
	 * @param poolname
	 * @param SqlStr
	 * @return
	 * @throws Exception
	 */
	public ResultSet executeQuery(String poolname, String SqlStr)
			throws Exception {
		ResultSet result = null;

		try {

			Statement stmt = con.createStatement();

			System.out.println("stmt is null?" + (stmt == null)
					+ "con is null ?" + (con == null) + " sqlstr =" + SqlStr);
			assert stmt == null : "stmt 为空";
			assert con == null : "con 为空";
			assert SqlStr == null : "查询语句为空";
			result = stmt.executeQuery(SqlStr);

		} catch (java.sql.SQLException e) {
			System.err.println("con " + (con == null));
			System.out.println(SqlStr);
			System.err.println(e.getMessage());

			throw new Exception("查询的时候报错" + SqlStr);
		} catch (java.lang.NullPointerException e3) {
			e3.printStackTrace();

		} catch (Exception e2) {
			e2.printStackTrace();
		} finally {

		}

		return result;
	}

	public String executeQueryXML(String sqlstr) throws Exception {
		return executeQueryXML(sqlstr, null, null, null);
	}

	/**
	 * 搜索所有的数据 用完记得调用 @see ConnectPoolxmgr.getInstance().freeusing(con);
	 * 
	 * @param sqlstr
	 * @return
	 * @throws Exception
	 */
	public String executeQueryXMLNeedAllRow(String sqlstr) throws Exception {
		ResultSet rs = executeQuery(sqlstr);
		StringBuffer sbf = new StringBuffer();
		int columncount = rs.getMetaData().getColumnCount();

		String[] column_names = new String[columncount];
		for (int i = 0; i < columncount; i++) {
			column_names[i] = rs.getMetaData().getColumnName(i + 1);
		}
		sbf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sbf.append("<records>");
		while (rs.next()) {
			sbf.append("<record>");
			for (int j = 0; j < columncount; j++) {
				sbf.append("<" + column_names[j] + ">");
				String temp = rs.getString(column_names[j]);
				sbf.append(temp == null ? "" : temp);
				sbf.append("</" + column_names[j] + ">");
			}
			sbf.append("</record>");
		}
		sbf.append("</records>");
		rs.close();
		return new String(sbf);
	}

	/**
	 * 用完记得调用 @see ConnectPoolxmgr.getInstance().freeusing(con);
	 * 
	 * @param sqlstr
	 * @param incsql
	 * @param limit
	 * @param start
	 * @return
	 * @throws Exception
	 */
	public String executeQueryXML(String sqlstr, String incsql, String limit,
			String start) throws Exception {
		// System.out.println("sqlstr="+sqlstr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int limitint = 0, startint = 0, rowcount = 0;
		String deleteorder = sqlstr;
		// zgw 2010-8-28 屏蔽原来 计算count的做法，采用套用的方法，避免多个form出现而导致有bug
		/*
		 * if(sqlstr.indexOf("order by")>0) { deleteorder =
		 * deleteorder.substring(0,deleteorder.indexOf("order by")); } String
		 * coutsql = "select count(*) as rownum "
		 * +deleteorder.substring(sqlstr.lastIndexOf("from"));
		 */
		if (limit != null && start != null && !limit.equals("0")) {
			limitint = Integer.parseInt(limit);
			startint = Integer.parseInt(start);
			// order by 在子句会有bug
			// 避免bug的方法是在 select TOP 100 PERCENT //20100828 bug 解决
			String coutsql = " select count(*) as rownum from ("
					+ sqlstr.replaceAll(
							"order\\s*by\\s*(\\w*\\s*)(?i)(desc|asc|\\W)", " ")
					+ ") as a";
			// System.out.println("countsql = "+coutsql);
			ResultSet rs = executeQuery(coutsql);

			if (rs.next())
				rowcount = rs.getInt("rownum");
			// 如果记录数太多每页超过200条记录，那么就扩大最大页数
			// if(rowcount/limitint >200)
			// {
			// limitint = rowcount/200;
			// }

		}
		String inc = "0";
		if (incsql != null) {
			ResultSet rs = executeQuery(incsql);
			if (rs.next())
				inc = rs.getString("inc");

		}
		StringBuffer sbf = new StringBuffer();
		ResultSet rs = executeQuery(sqlstr);

		int columncount = rs.getMetaData().getColumnCount();

		String[] column_names = new String[columncount];
		for (int i = 0; i < columncount; i++) {
			column_names[i] = rs.getMetaData().getColumnName(i + 1);
		}
		sbf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		if (limit != null && start != null && !inc.equals("0")) {
			sbf.append("<records totle='" + rowcount + "' autoinc='" + inc
					+ "'>");
		} else if (limit != null && start != null) {
			sbf.append("<records totle='" + rowcount + "'>");

		} else if (!inc.equals("0")) {
			sbf.append("<records  autoinc='" + inc + "'>");

		} else {
			sbf.append("<records>");
		}
		int currow = 0;

		while (rs.next()) {

			if (limit != null && start != null && !limit.equals("0")) {
				if ((startint) <= currow && currow < (startint + limitint)) {
					;
				} else {
					currow++;
					continue;
				}
			}
			sbf.append("<record>");
			for (int j = 1; j <= columncount; j++) {
				// sbf.append("<"+column_names[j]+">");
				// String temp = rs.getString(column_names[j]);
				//
				// sbf.append(temp==null?"":temp);
				// sbf.append("</"+column_names[j]+">");

				sbf.append("<" + rs.getMetaData().getColumnName(j) + ">");
				String temp = "";

				if (rs.getMetaData().getColumnClassName(j)
						.equals("java.sql.Timestamp")) {
					Timestamp d = rs.getTimestamp(j);
					// rs.getTimestamp(columnLabel)
					if (d == null)
						temp = "";
					else {
						temp = sdf.format(d);
					}
				} else {
					temp = rs.getString(j);
				}
				sbf.append(temp == null ? "" : temp);
				// System.out.println("temp  "+temp+" type="+rs.getMetaData().getColumnType(j)
				// +" class name"+rs.getMetaData().getColumnClassName(j));
				sbf.append("</" + rs.getMetaData().getColumnName(j) + ">");
			}
			sbf.append("</record>");
			currow++;
		}
		// sbf.append("<totle>"+currow+"</totle>");
		sbf.append("</records>");
		// System.out.println(sbf);

		rs.close();
		return new String(sbf);
	}

	public int UpdateException(String SqlStr) throws SQLException {
		return UpdateException("test", SqlStr);
	}

	/**
	 * 由调用的地方抛出异常，执行数据库更新 用完记得调用 @see
	 * ConnectPoolxmgr.getInstance().freeusing(con);
	 * 
	 * @param poolname
	 * @param SqlStr
	 * @return
	 * @throws Exception
	 */
	private int UpdateException(String poolname, String SqlStr) throws SQLException {

		// try {
		int result = -1;

		if (con == null)
			System.out.println("con is null");
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
		result = stmt.executeUpdate(SqlStr);
		// here add one line by jnma 12.11
		MyLogger.getInstance().debug("Update=" + SqlStr);
		// con.commit();
		if (result == 0)
			System.out.println("数据未被更新" + SqlStr);

		return result;
		// }
		// finally {
		// if (stmt != null)
		// stmt.close();
		//
		// }
	}

    /**
     * 如果表table总某字段columnname有值value,那么啥事不干,否则插入
     * @param table
     * @param columnname
     * @param value
     */
    public void replacecolumn(String table ,String columnname,String value) throws Exception {

        Statement stmt = con.createStatement();
        ResultSet result = null;
        result = stmt.executeQuery("select * from "+ table +" where "+columnname+" = '"+value+"'");
        int count = 0 ;
        //count 超过2个内容直接跳出了,不允许更新,这样对数据不安全

        while(result.next() /*&& count<2*/)
        {
           System.out.println( result.getString("name"));
            count++;
        }
       result.close();
        stmt.close();
        String sql ="";
        if(count==0)
        {
            sql= "insert into "+table +" ("+columnname+") value("+value+")";
            UpdateException(sql);
            MyLogger.getInstance().debug("replacecolumn="+sql);
        }



    }


	/*
	 * ConnectionObject con = ConnectPoolxmgr.getInstance()
	 * .getConnectionObject("test");
	 * con.Update("insert into taobao_item (detail_url) values("+i+")");
	 * ConnectPoolxmgr.getInstance().freeusing(con);
	 */
	public int Update(String SqlStr) throws Exception {
		return Update("test", SqlStr);
	}

	/**
	 * 不抛出异常 和UpdateException比 历史原因此函数最好就不要调用了 用完记得调用 @see
	 * ConnectPoolxmgr.getInstance().freeusing(con);
	 * 
	 * @deprecated
	 */
	private int Update(String poolname, String SqlStr) throws Exception {

		int result = -1;
		try {
			if (con == null)
				System.out.println("con is null");
			Statement stmt = con
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			result = stmt.executeUpdate(SqlStr);
			// here add one line by jnma 12.11
			MyLogger.getInstance().debug("Update=" + SqlStr);
			// con.commit();
			if (result == 0)
				System.out.println("数据未被更新" + SqlStr);
		} catch (java.sql.SQLException e) {
			System.err.println("ִ��delete,update,insert SQL���");
			MyLogger.getInstance().debug(
					"Update-- delete,update,insert SQL error=" + SqlStr);
			e.printStackTrace();
		} finally {
			// 20130808 不需要关闭连接池中已经处理
			/*
			 * if (stmt != null) stmt.close();
			 */
		}
		return result;
	}

    /**
     * 执行sql语句这个和executeupdate不一样,这个设计成为可以执行执行存储过程的sql
     * @param sql
     * @throws Exception
     */

    public void execute(String sql)  throws Exception
    {
        try {
            MyLogger.getInstance().debug("执行=" + sql);

            con.setAutoCommit(false);
            Statement stmt = con
                    .createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);

            stmt.execute(sql);
            con.commit();
        } catch (Exception e) {

            MyLogger.getInstance().debug("执行失败=" + sql);
            con.rollback();


            throw new java.lang.Exception(e);
        } finally {

            con.setAutoCommit(true);

        }

    }
	/**
	 * 在事务中执行sql。一条失败就回滚。 注意如果是mysql记得用分号分隔开。 用完记得调用 @see
	 * ConnectPoolxmgr.getInstance().freeusing(con);
	 * 
	 * @param sql
	 * @throws Exception
	 */
	public void Updates(String sql) throws Exception {
		// ConnectPool cp = ConnectPool.getInstance();
		// Connection con = cp.getConnectionOne("test");
		// Statement stmt = null;
		try {
			MyLogger.getInstance().debug("执行=" + sql);

			con.setAutoCommit(false);
			Statement stmt = con
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);

			stmt.executeUpdate(sql);
			con.commit();
		} catch (Exception e) {
			System.out.println("1");
			MyLogger.getInstance().debug("执行失败=" + sql);
			con.rollback();
			System.out.println("2");
			// 不要抛出
			// e.printStackTrace();

			throw new java.lang.Exception(e);
		} finally {

			con.setAutoCommit(true);
			// 20130808 不需要关闭连接池中已经处理
			/*
			 * if (stmt != null) stmt.close();
			 */
			// ConnectPool.getInstance().freeConnection("test", con);
			// con.close();
		}

	}

	/**
	 * 多条sql语句同时执行
	 * 
	 * @param SqlArray
	 * @return
	 * @throws Exception
	 * @deprecated
	 */
	public boolean handleTransaction(Vector SqlArray) throws Exception {
		boolean result = false;
		int errornum = 0;

		int ArraySize = SqlArray.size();
		int i = 0;

		try {
			try {

				Statement stmt = con.createStatement();
				con.setAutoCommit(false);

				String temp = "";
				for (i = 1; i <= ArraySize; i++) {

					{
						temp = (String) SqlArray.elementAt(i - 1);
						MyLogger.getInstance().debug(
								"handleTransaction=" + i + " ArraySize= "
										+ ArraySize + " " + temp);
						stmt.addBatch(temp);
						if (i % 1000 == 0) {
							errornum += stmt.executeBatch().length;

							stmt.clearBatch();
						}

					}

				}

				stmt.executeBatch();

				stmt.clearBatch();

				{
					con.commit();
					con.setAutoCommit(true);// ����

				}

				result = true;
			} catch (java.sql.BatchUpdateException e) {
				try {

					System.out.println((String) SqlArray.elementAt(errornum
							+ e.getUpdateCounts().length));
					System.out.println(e.toString() + e.getErrorCode()
							+ e.getMessage() + e.getLocalizedMessage());
					System.out.println(e.getErrorCode());
					System.out.println("失败 返回");

					MyLogger.getInstance().debug(
							"handleTransaction error="
									+ (String) SqlArray.elementAt(errornum
											+ e.getUpdateCounts().length));
					MyLogger.getInstance().debug(
							"handleTransaction error =" + e.toString()
									+ e.getErrorCode() + e.getMessage()
									+ e.getLocalizedMessage());
					MyLogger.getInstance().debug(
							" handleTransaction error e.getErrorCode()="
									+ e.getErrorCode());
					con.rollback();
					e.printStackTrace();

				} catch (java.sql.SQLException Te) {
					System.err.println("  " + Te.getMessage());
					Te.printStackTrace();
				}
			} catch (Exception e2) {
				con.rollback();
				e2.printStackTrace();
			}
			try {
				con.setAutoCommit(true);
			} catch (java.sql.SQLException e) {
				System.err.println("提交失败！");
				e.printStackTrace();
			}
		} finally {

			// con.close();
		}
		return result;
	}

	/**
	 * @see handleTransaction 外层捕捉异常
	 * @param SqlArray
	 * @return
	 * @throws Exception
	 */
	public boolean handleTransactionX(Vector SqlArray) throws Exception {
		boolean result = false;
		int errornum = 0;

		int ArraySize = SqlArray.size();
		int i = 0;

		try {
			try {

				Statement stmt = con.createStatement();
				con.setAutoCommit(false);

				String temp = "";
				for (i = 1; i <= ArraySize; i++) {

					{
						temp = (String) SqlArray.elementAt(i - 1);

						MyLogger.getInstance().debug(
								"handleTransaction=" + i + " ArraySize= "
										+ ArraySize + " " + temp);
						stmt.addBatch(temp);
						if (i % 1000 == 0) {
							errornum += stmt.executeBatch().length;

							stmt.clearBatch();
						}

					}

				}

				stmt.executeBatch();

				stmt.clearBatch();

				{
					con.commit();
					// con.setAutoCommit(true);// ����

				}

				result = true;
			} catch (Exception e) {
				try {

					// System.out.println((String)
					// SqlArray.elementAt(errornum+e.getUpdateCounts().length));
					// System.out.println(e.toString()+e.getErrorCode()+e.getMessage()+e.getLocalizedMessage());
					System.out.println(e.getMessage());
					System.out.println("失败 返回");

					// MyLogger.getInstance().debug("handleTransaction error="+(String)
					// SqlArray.elementAt(errornum+e.getUpdateCounts().length));
					// MyLogger.getInstance().debug("handleTransaction error ="+e.toString()+e.getErrorCode()+e.getMessage()+e.getLocalizedMessage());
					MyLogger.getInstance().debug(e.getMessage());
					con.rollback();
					throw new java.lang.RuntimeException(e.getMessage());

				} catch (java.sql.SQLException Te) {

					throw new java.lang.RuntimeException(Te.getMessage());
				}
			}

		} finally {

			try {
				con.setAutoCommit(true);
			} catch (java.sql.SQLException e) {
				System.err.println("提交失败！");
				throw new java.lang.RuntimeException(e.getMessage());
			}

			// con.close();
		}
		return result;
	}

	/**
	 * 释放
	 * 
	 * @throws SQLException
	 */
	public void freeconnect() throws SQLException {

		setIsalive(false);
		// if (stmt != null)
		// stmt.close();
	}
}
