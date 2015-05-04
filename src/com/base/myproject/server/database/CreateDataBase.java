/**
 * 
 */
package com.base.myproject.server.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.base.myproject.server.database.poolx.ConnectPoolxmgr;
import com.base.myproject.server.database.poolx.ConnectionObject;

/**
 * @author Administrator
 *
 */
public class CreateDataBase {

	public CreateDataBase()
	{
		
	}
	
	/**
	 * 查找数据库名是否存在。name 是要查找的数据库名
	 * true 存在 否则不存在
	 * @param name
	 * @return
	 * @throws Exception
	 */
	
	public boolean isexistDB(String name) throws Exception
	{
		
//			PoolMan pm = new PoolMan();
		 ConnectionObject con = ConnectPoolxmgr.getInstance()
					.getConnectionObject("test");
					
		try{
		ResultSet rs = con.executeQuery("select count(*) c From master.dbo.sysdatabases where name='"+name+"'");
		if(rs.next())
		{
			if(rs.getInt("c")>0)
				return true;
			return false;
		}
		}
		finally
		{
			try {
				ConnectPoolxmgr.getInstance().freeusing(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	/**
	 * 根据数据库备份生成新数据库
	 * 
	 * @param dbname 生成的数据库名
	 * @param fromfile 数据库备份文件
	 * @param topath 新数据库的数据和日记文件位置
	 * @return
	 * @throws Exception
	 */
	public boolean createDataBase(String dbname,String fromfile,String topath) throws Exception
	{
		if(!isexistDB(dbname))
		{
//		PoolMan pm = new PoolMan();
			ConnectionObject con = ConnectPoolxmgr.getInstance()
					.getConnectionObject("test");
			try{
			
		//Connection con = pm.getPool("test");
		con.UpdateException("RESTORE DATABASE "+dbname+"  FROM DISK = '"+fromfile+"'  WITH MOVE 'base_Data' TO '"+topath+"\\"+dbname+".mdf',MOVE 'base_Log' TO '"+topath+"\\"+dbname+".ldf'");
		
			}
			finally
			{
				
				try {
					ConnectPoolxmgr.getInstance().freeusing(con);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		return true;
			
			
		}
		return false;
		
	}
	
	
	public static void main(String[] argv) throws Exception
	{
		
		if(argv.length!=3)
    	{
    		System.err.println("请录入生成的数据库名，备份文件路径，生成的数据库文件路径");
    		throw new RuntimeException("请录入生成的数据库名，备份文件路径，生成的数据库文件路径");
    	}

		CreateDataBase cdb = new CreateDataBase();
		
		
	
			System.out.println(cdb.isexistDB(argv[0]));
			if(!cdb.createDataBase(argv[0],argv[1],argv[2]))
			{
		 		throw new RuntimeException("生成不成功，有可能数据库已经存在或者备份文件不存在，或者生成的目标文件路径不存在");
		 		  
			}
	
	}
}
