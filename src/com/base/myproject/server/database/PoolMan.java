/*
 * PoolMan.java
 *
 * Created on 2006��8��20��, ����2:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.base.myproject.server.database;


/**
 * Title: ConnectPool.java
 * Description: ��ݿ����
 * Copyright: Copyright (c) 2002/12/25
 * Company:
 * Author :
 * remark : ����ָ��ع�
 * Version 2.0
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Vector;

import com.base.myproject.server.init.MyLogger;



public class PoolMan extends ConnectPool{
    
    private ConnectPool connMgr;
    
    private Statement stmt;
    
    private Connection con;
    
    private ResultSet rst;
    
    /**
     * ����l�ӳ�ʼ��
     * 
     */
    
    public Connection getPool(String name) throws Exception {
        try {
        	
            connMgr = ConnectPool.getInstance();
            if(connMgr==null)
                System.out.println("connMgr 的值为空");
            con = connMgr.getConnection(name);
            if(con==null)
            {
                System.err.println("con 的值为空!!!!!!!!!!!!!");
              //  con = connMgr.getConnection(name);
            }
            //con.setAutoCommit(false);
        } catch (Exception e) {
            System.err.println("获取连接失败了");
            
            e.printStackTrace();
            connMgr.release();
        }
        
        return con;
    }
    public Connection getPool() throws Exception {
    	return getPool("test");
    }
    /**
     * ͬ���Ϸ���,����l�ӿ��еȴ�ʱ�� ���÷���
     */
    
    public Connection getPool_t(String name, long time) throws Exception {
        try {
            connMgr = ConnectPool.getInstance();
            con = connMgr.getConnection(name, time);
            con.setAutoCommit(false);
        } catch (Exception e) {
            System.err.println("���ܴ���l��!");
            
        }
        return con;
    }
    /**
     * ִ�в�ѯ����1
     *Ĭ�ϵ�l�ӳ�����Ϊtest
     */
    public ResultSet executeQuery(String SqlStr) throws Exception {
        return  executeQuery("test",SqlStr);
    }
    /**
     * ִ�в�ѯ����1
     */
    public ResultSet executeQuery(String poolname,String SqlStr) throws Exception {
        ResultSet result = null;
        Connection con =  getPool(poolname);
    //	ConnectPool  cp = ConnectPool.getInstance();
//   	 Connection con = cp.getConnectionOne("test");
        try {
     
        	if(con==null)
        	{
        		System.err.println("...................");
        		ConnectPool  cp = ConnectPool.getInstance();
//    			cp.getConnectionOne("test");
        		System.out.println("1=="+con);
        		con = cp.getConnectionOne("test");
        		System.out.println("2=="+con);
        	}
             stmt = con.createStatement();
             
             
            result = stmt.executeQuery(SqlStr);
   
            
            
            /*
             * ��mysql�в�������
             */
           // con.commit();
        } catch (java.sql.SQLException e) {
            System.err.println("con "+(con==null));
            System.out.println(SqlStr);
            System.err.println(e.getMessage());
           
            throw new Exception("查询的时候报错"+SqlStr);
        }
        finally
        {
        	//Connection con
        	//zgw 2011-0904
//        	if(stmt!=null)
//        		stmt.close();
      // 	ConnectPool.getInstance().freeConnection(poolname, con);
        	//zgw 2011-0904
        	
        	//con.close();
        }
        
        return result;
    }
    
    /**
     * ִ�в�ѯ����2
     */
    public ResultSet getRst(String SqlStr) throws Exception {
        // ResultSet result = null;
    	ConnectPool  cp = ConnectPool.getInstance();
   	 Connection con = cp.getConnectionOne("test");
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rst = stmt.executeQuery(SqlStr);
            // here add one line by jnma 12.11
            con.commit();
        } catch (java.sql.SQLException e) {
            //e.printStackTrace();
            throw new Exception("ִ�в�ѯ�����");
        }
        finally
        {
        	if(stmt!=null)
        		stmt.close();
        	con.close();
        }
        return rst;
    }
    public String executeQueryXML(String sqlstr) throws Exception
    {
    	return executeQueryXML(sqlstr,null,null,null);
    }
    /**
     * 搜索所有的数据
     * @param sqlstr
     * @return
     * @throws Exception
     */
    public String executeQueryXMLNeedAllRow(String sqlstr) throws Exception
    {
    	ResultSet rs = executeQuery(sqlstr);
    	StringBuffer sbf = new StringBuffer();
    	int columncount = rs.getMetaData().getColumnCount();
    	
    	String[] column_names= new String[columncount];
    	for(int i=0;i<columncount;i++)
    	{
    		column_names[i]=rs.getMetaData().getColumnName(i+1);
    	}
    	sbf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    	sbf.append("<records>");
    	while(rs.next())
    	{
    	sbf.append("<record>");
		for(int j=0;j<columncount;j++)
		{
			sbf.append("<"+column_names[j]+">");
			String temp = rs.getString(column_names[j]);
			sbf.append(temp==null?"":temp);
			sbf.append("</"+column_names[j]+">");
		}
		sbf.append("</record>");
    	}
		sbf.append("</records>");
    	
    	return new String(sbf);
    }
    
    public String executeQueryXML(String sqlstr,String incsql,String limit,String start) throws Exception
    {
    	//System.out.println("sqlstr="+sqlstr);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	int limitint=0 ,startint =0,rowcount=0;
    	String deleteorder = sqlstr;
    	//zgw 2010-8-28 屏蔽原来 计算count的做法，采用套用的方法，避免多个form出现而导致有bug
    /*	if(sqlstr.indexOf("order by")>0)
    	{
    		deleteorder = deleteorder.substring(0,deleteorder.indexOf("order by"));
    	}
    	String coutsql = "select count(*) as rownum " +deleteorder.substring(sqlstr.lastIndexOf("from"));*/
    	if(limit !=null && start!=null && !limit.equals("0"))
    	{
    		limitint = Integer.parseInt(limit);
    		startint = Integer.parseInt(start);
    		//order by 在子句会有bug 
    		//避免bug的方法是在 select TOP 100 PERCENT //20100828 bug 解决
    		String coutsql = " select count(*) as rownum from (" +sqlstr.replaceAll("order\\s*by\\s*(\\w*\\s*)(?i)(desc|asc|\\W)", " ") +") as a";
    		//System.out.println("countsql = "+coutsql);
    	ResultSet rs = executeQuery(coutsql);
    	
    	if(rs.next())
    	 rowcount = rs.getInt("rownum");
    	//如果记录数太多每页超过200条记录，那么就扩大最大页数
//    	if(rowcount/limitint >200)
//    	{
//    		limitint = rowcount/200;
//    	}
    	
    	}
    	String inc = "0";
    	if(incsql!=null)
    	{
    		ResultSet rs = executeQuery(incsql);
        	if(rs.next())
        	inc = rs.getString("inc");
    		
    	}
    	StringBuffer sbf = new StringBuffer();
    	ResultSet rs = executeQuery(sqlstr);
    	
    	int columncount = rs.getMetaData().getColumnCount();
    	
    	String[] column_names= new String[columncount];
    	for(int i=0;i<columncount;i++)
    	{
    		column_names[i]=rs.getMetaData().getColumnName(i+1);
    	}
    	sbf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    	if(limit !=null && start!=null && !inc.equals("0"))
    	{
    	sbf.append("<records totle='"+rowcount+"' autoinc='"+inc+"'>");
    	}
    	else if(limit !=null && start!=null )
    	{
    		sbf.append("<records totle='"+rowcount+"'>");
    	    
    	}
    	else if(!inc.equals("0"))
    	{
    		sbf.append("<records  autoinc='"+inc+"'>");
    	    
    	}
    	else
    	{
    		sbf.append("<records>");
    	}
    	int currow = 0;
    	
    	while(rs.next())
    	{
    		
    		
    		if(limit !=null && start!=null && !limit.equals("0"))
    		{
    			if((startint )<=currow && currow<(startint+limitint))
    			{
    				;
    			}
    			else
    			{
    				currow++;
    				continue;
    			}
    		}
    		sbf.append("<record>");
    		for(int j=1;j<=columncount;j++)
    		{
//    			sbf.append("<"+column_names[j]+">");
//    			String temp = rs.getString(column_names[j]);
//    			
//    			sbf.append(temp==null?"":temp);
//    			sbf.append("</"+column_names[j]+">");
    			
    			sbf.append("<"+rs.getMetaData().getColumnName(j)+">");
    			String temp ="";
    			
    			
    			if(rs.getMetaData().getColumnClassName(j).equals("java.sql.Timestamp"))
    			{
    				Timestamp d = rs.getTimestamp(j);
    				//rs.getTimestamp(columnLabel)
    				if(d==null)
    					temp="";
    				else
    				{
    				temp = sdf.format(d);
    				}
    			}
    			else
    			{
    			 temp = rs.getString(j);
    			}
    			sbf.append(temp==null?"":temp);
    			//System.out.println("temp  "+temp+" type="+rs.getMetaData().getColumnType(j) +" class name"+rs.getMetaData().getColumnClassName(j));
    			sbf.append("</"+rs.getMetaData().getColumnName(j)+">");
    		}
    		sbf.append("</record>");
    		currow++;
    	}
    //	sbf.append("<totle>"+currow+"</totle>");
    	sbf.append("</records>");
    	//System.out.println(sbf);
    	return new String(sbf);
    }
    
    
  
        /**
     * ִ�и���
     */
    public int UpdateException(String SqlStr)throws Exception{
     return   Update("test",SqlStr);
    }
    
    public int UpdateException(String poolname,String SqlStr) throws Exception {
//    	  Connection con =  getPool(poolname);
      	ConnectPool  cp = ConnectPool.getInstance();
   	 Connection con = cp.getConnectionOne("test");
    	  try{
        int result = -1;
       
        	if(con==null)
        		System.out.println("con is null");
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            result = stmt.executeUpdate(SqlStr);
            // here add one line by jnma 12.11
            MyLogger.getInstance().debug("Update="+SqlStr);
           // con.commit();
            if (result == 0)
                System.out.println("数据未被更新"+SqlStr);
      
        return result;
    	  }
    	  finally
    	  {
    		  if(stmt!=null)
          		stmt.close();
//    		  ConnectPool.getInstance().freeConnection("test", con);
    		  con.close();
    	  }
    }
    public int Update(String SqlStr)throws Exception{
        return   Update("test",SqlStr);
       }
    /**
     * ִ�и���
     */
    public int Update(String poolname,String SqlStr) throws Exception {
//    	 Connection con= getPool(poolname);
     	ConnectPool  cp = ConnectPool.getInstance();
   	 Connection con = cp.getConnectionOne("test");
        int result = -1;
        try {
        	if(con==null)
        		System.out.println("con is null");
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            result = stmt.executeUpdate(SqlStr);
            // here add one line by jnma 12.11
            MyLogger.getInstance().debug("Update="+SqlStr);
           // con.commit();
            if (result == 0)
                System.out.println("数据未被更新"+SqlStr);
        } catch (java.sql.SQLException e) {
            System.err.println("ִ��delete,update,insert SQL���");
            MyLogger.getInstance().debug("Update-- delete,update,insert SQL error="+SqlStr);
            e.printStackTrace();
        }
        finally
        {
        	if(stmt!=null)
        		stmt.close();
//        	 ConnectPool.getInstance().freeConnection("test", con);
        	 con.close();
        }
        return result;
    }
    /**
	 * 在事务中执行sql。一条失败就回滚。
	 * 注意如果是mysql记得用分号分隔开。
	 * @param sql
	 * @throws Exception
	 */
	public void Updates(String sql) throws Exception 
	{
    	ConnectPool  cp = ConnectPool.getInstance();
   	 Connection con = cp.getConnectionOne("test");
		 Statement stmt = null;
		 try {
			 MyLogger.getInstance().debug("执行="+sql);
		
		 con.setAutoCommit(false);
		   stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		  
		  stmt.executeUpdate(sql);
		 con.commit();
		 }
		catch(Exception e)
		 {
			 System.out.println("1");
			 MyLogger.getInstance().debug("执行失败="+sql);
			 con.rollback();
			 System.out.println("2");
			 //不要抛出
			// e.printStackTrace();  
			 
			 
			 throw new java.lang.Exception(e);
		 }
		 finally
		 { 
			 con.setAutoCommit(true);
			 if(stmt!=null)
	        		stmt.close();
//			 ConnectPool.getInstance().freeConnection("test", con);
			 con.close();
		 }
		  
		 
	}
    
    /**
     * ִ��������
     */
    public boolean handleTransaction(Vector SqlArray) throws Exception {
        boolean result = false;
        int errornum=0;
     
        int ArraySize = SqlArray.size();
        int i=0;
//        Connection con= getPool();
    	ConnectPool  cp = ConnectPool.getInstance();
   	 Connection con = cp.getConnectionOne("test");
        try{
        try {
        	
            stmt = con.createStatement();
            con.setAutoCommit(false);
           
            String temp="";
            for ( i = 0; i < ArraySize; i++) {

            	
            	{
            		temp = (String) SqlArray.elementAt(i);
            		MyLogger.getInstance().debug("handleTransaction="+i+" ArraySize= "+ArraySize+" "+temp);
            		stmt.addBatch(temp);
            	if(i%1000==0)
            	{
            		errornum+=stmt.executeBatch().length;
            		
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
            	
            	System.out.println((String) SqlArray.elementAt(errornum+e.getUpdateCounts().length));
                System.out.println(e.toString()+e.getErrorCode()+e.getMessage()+e.getLocalizedMessage());
                System.out.println( e.getErrorCode());
                System.out.println("失败 返回");
                
                MyLogger.getInstance().debug("handleTransaction error="+(String) SqlArray.elementAt(errornum+e.getUpdateCounts().length));
                MyLogger.getInstance().debug("handleTransaction error ="+e.toString()+e.getErrorCode()+e.getMessage()+e.getLocalizedMessage());
                MyLogger.getInstance().debug(" handleTransaction error e.getErrorCode()="+ e.getErrorCode());
                con.rollback();
                e.printStackTrace();
        		
            } catch (java.sql.SQLException Te) {
                System.err.println("  "+Te.getMessage());
                Te.printStackTrace();
            }
        }
        try {
            con.setAutoCommit(true);
        } catch (java.sql.SQLException e) {
            System.err.println("提交失败！");
            e.printStackTrace();
        }
        }
        finally
        {
        	if(stmt!=null)
        		stmt.close();
//        	 ConnectPool.getInstance().freeConnection("test", con);
        	con.close();
        }
        return result;
    }
    /**
     * 外层捕捉异常
     * @param SqlArray
     * @return
     * @throws Exception
     */
    public  boolean handleTransactionX(Vector SqlArray) throws Exception {
        boolean result = false;
        int errornum=0;
       System.out.println("--22-------------");
        int ArraySize = SqlArray.size();
        int i=0;

//        Connection con=getPool();
        
    	ConnectPool  cp = ConnectPool.getInstance();
    	 Connection con = cp.getConnectionOne("test");
        Statement stmt =null;
        try{
        try {
        	
        	stmt = con.createStatement();
            con.setAutoCommit(false);
           
            String temp="";
            for ( i = 0; i < ArraySize; i++) {

            	
            	{
            		temp = (String) SqlArray.elementAt(i);
            		
            		MyLogger.getInstance().debug("handleTransaction="+i+" ArraySize= "+ArraySize+" "+temp);
            		stmt.addBatch(temp);
            	if(i%1000==0)
            	{
            		errornum+=stmt.executeBatch().length;
            		
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
        } catch (java.sql.BatchUpdateException e) {
            try {
            	
            	System.out.println((String) SqlArray.elementAt(errornum+e.getUpdateCounts().length));
                System.out.println(e.toString()+e.getErrorCode()+e.getMessage()+e.getLocalizedMessage());
                System.out.println( e.getErrorCode());
                System.out.println("失败 返回");
                
                MyLogger.getInstance().debug("handleTransaction error="+(String) SqlArray.elementAt(errornum+e.getUpdateCounts().length));
                MyLogger.getInstance().debug("handleTransaction error ="+e.toString()+e.getErrorCode()+e.getMessage()+e.getLocalizedMessage());
                MyLogger.getInstance().debug(" handleTransaction error e.getErrorCode()="+ e.getErrorCode());
                con.rollback();
                throw new java.lang.RuntimeException(e.getMessage());
        		
            } catch (java.sql.SQLException Te) {
//                System.err.println("  "+Te.getMessage());
//                Te.printStackTrace();
                throw new java.lang.RuntimeException(Te.getMessage());
            }
        }
      
        }
        finally
        {
        	if(stmt!=null)
        		stmt.close();
        	  try {
                  con.setAutoCommit(true);
              } catch (java.sql.SQLException e) {
                  System.err.println("提交失败！");
                  throw new java.lang.RuntimeException(e.getMessage());
              }
//        	ConnectPool.getInstance().freeConnection("test", con);
              con.close();
        }
        return result;
    }
    /**
     * �ͷ�l��
     */
    public void close(String name) throws Exception {
        try {
            if (stmt != null)
                stmt.close();
            if (con != null) {
                connMgr.freeConnection(name, con);
                
              //  System.out.println(" [c �����ͷ�һ��l�� ] ");
                
            }
        } catch (java.sql.SQLException e) {
            System.err.println("释放数据库连接的时候出错");
        }
    }
    /**
     *ȡ�� ConnectPool
     */
    public ConnectPool getConnectPool() {
        return connMgr;
    }

    public  static void  main(String[] argv)
    {
    	PoolMan pm = new PoolMan();
    	
    	long c = System.currentTimeMillis();
    	for(int i=0;i<1;i++)
    	{
    	System.out.println(i);
    	try {
			//pm.executeQueryXML("select bcode from bcode","select 1 as inc ","1","1");
			ResultSet rs = pm.executeQuery("select bcode from bcode");
			
			while(rs.next())
			{
				System.out.println(rs.getString(1));
			}
			
			//pm.getConnectPool().
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	}
    	System.out.println((System.currentTimeMillis()-c)/1000);
    }
}

