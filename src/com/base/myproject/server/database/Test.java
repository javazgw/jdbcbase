/*
 * Test.java
 *
 * Created on 2006��10��5��, ����11:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.base.myproject.server.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

/**
 * 
 * @author zgw
 */
public class Test {

	/** Creates a new instance of Test */
	public Test() {
	}

	public static void main(String[] argv) throws Exception {
		
		long l1 = System.currentTimeMillis();
		System.out.println(l1);
		
		String path = Test.class.getProtectionDomain().getCodeSource().getLocation().toString();
		System.out.println(path);
		String aa = "DL-aaa-XL-XXXL-M-XXXL";
		aa = aa.replaceAll("", "").replaceAll("-M", "").replaceAll("-L", "").replaceAll("-XL", "")
		.replaceAll("-S", "").replaceAll("-XXL", "").replaceAll("-L", "").replaceAll("-XXXL", "");
		System.out.println(aa);
		
		
		
		/*for(int k=0;k<10000;k++)
		{
			
//			DBConnectionPool dbp = new DBConnectionPool("aa","jdbc:microsoft:sqlserver://127.0.0.1:1155;databaseName=dangkou;SelectMethod=cursor","sa","",0);
//			dbp.newConnection();
			
			ConnectPool  cp = ConnectPool.getInstance();
			cp.getConnectionOne("lenove");
			final PoolMan pm = new PoolMan();
			ResultSet rs = pm.executeQuery("lenove", "select count(*) as c from cgcode");
			if(rs.next())
				System.out.println("lenove = "+rs.getString("c"));

		}*/
		PoolMan pm = new PoolMan();
		ResultSet rs1 = pm.executeQuery("test", "SELECT cgicode,buyprice,icode FROM storeslistg s1 where ifnull(codename,'') =1 and buyprice is null");
		String icode="",cgicode="";
		while(rs1.next())
		{
			icode = rs1.getString("icode");
			cgicode = rs1.getString("cgicode");
			cgicode = cgicode.replaceAll("", "").replaceAll("-M", "").replaceAll("-L", "").replaceAll("-XL", "")
			.replaceAll("-S", "").replaceAll("-XXL", "").replaceAll("-L", "").replaceAll("-XXXL", "");
		/*	ResultSet rs2 = pm.executeQuery("test", "SELECT cgicode,upric as buyprice FROM storeslistg s1 where ifnull(codename,'') !=1 and inoutflag=1 " +
					"and upric is not null and cgicode = '"+cgicode+"'");
			
			while(rs2.next())
			{
				System.out.print(rs2.getString("cgicode"));
				System.out.print(rs2.getString("buyprice"));
				System.out.println("成功-------");
				//pm.Update("update storeslistg set buyprice='"+rs2.getString("buyprice")+"' where icode ='"+icode+"'");
				break;
			}*/
			
			
		}
		
		long l2 = System.currentTimeMillis();
		System.out.println(l2-l1);
/*		long l2 = System.currentTimeMillis();
		System.out.println(l2-l1);
		
		final PoolMan pm = new PoolMan();
		ResultSet rs = pm.executeQuery("lenove", "select count(*) as c from cgcode");
		if(rs.next())
			System.out.println("target = "+rs.getString("c"));
		Connection  cp  = pm.getConnection("lenove");
		DatabaseMetaData md  = cp.getMetaData();
		
		 ResultSet rs2 = md.getTables(null, null, "%", null);
		 while(rs2.next())
		 {
			 System.out.println("target = "+rs2.getString(3));
		 }
		 
		 DatabaseMetaData md1  = cp.getMetaData();
		 ResultSet rs3= md1.getColumns(null, null, "%", "%");
		 while(rs3.next())
		 {
//			 System.out.println("target2 = "+rs3.getString(3));
			 System.out.println("target2 = "+rs3.getString("TYPE_NAME")+" type = "+rs3.getInt("DATA_TYPE")+" size="+rs3.getString("COLUMN_SIZE")+"--"+rs3.getString(2)+rs3.getString(3)+"--"+rs3.getString(4));
		 }
	
		  System.out.println(cp.getCatalog());*/
		  
//		ResultSet rs1 = pm.executeQuery( "select count(*) as c from cgcode");
//		if(rs1.next())
//			System.out.println("test= "+rs1.getString("c"));

//		ConnectPool  cp = ConnectPool.getInstance();
//		cp.getConnectionOne("test");
		/*for (int j = 0; j < 10; j++)
		{
			Thread t = new Thread() {
				public void run() {
					for (int i = 0; i < 30; i++) {

						try {
							Vector str = new Vector();
							str.add("insert into cgcode (cgcode) values ('123')");
							str.add("insert into cgcode (cgcode) values ('321')");

							pm.executeQuery("select * from bcode");
							pm.handleTransactionX(str);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
			t.start();
		}*/

		/*
		 * try { Connection con; Statement s1 = null; ResultSet r1 = null;
		 * Statement s2 = null; ResultSet r2 = null;
		 * Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver"); con =
		 * DriverManager.getConnection( //
		 * "jdbc:microsoft:sqlserver://localhost:1155;DatabaseName=pubs;SelectMethod=Direct;User=sa;Password="
		 * ); //fix 1
		 * "jdbc:microsoft:sqlserver://localhost:1155;DatabaseName=pubs;SelectMethod=Cursor;User=sa;Password="
		 * );
		 * 
		 * for(int i=0;i<10;i++) { con.setAutoCommit(false);
		 * 
		 * try { System.out.println("i="+i); s1 = con.createStatement();
		 * con.setAutoCommit(false); con.setAutoCommit(true);
		 * con.setAutoCommit(true); r1 =
		 * s1.executeQuery("SELECT * FROM authors");
		 * 
		 * //fix 2 //r1.close(); //s1.close();
		 * 
		 * s2 = con.createStatement(); r2 =
		 * s2.executeQuery("SELECT * FROM publishers");
		 * 
		 * } catch (SQLException ex) { System.out.println(ex); } finally {
		 * //con.setAutoCommit(true); } }
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
		/*
		 * Connection con = null; con = pm.getPool("test");
		 * 
		 * Statement stmt = null; stmt = con.createStatement();
		 */
		// for(int i=0;i<2;i++)
		// {
		/*
		 * con.setAutoCommit(false);
		 * stmt.addBatch("insert into bcode (bname) select count(*) from bcode"
		 * );
		 * stmt.addBatch("insert into bcode (bname) select count(*) from bcode"
		 * );
		 * stmt.addBatch("insert into bcode (bname) select count(*) from bcode"
		 * ); stmt.executeBatch(); con.commit(); con.setAutoCommit(true);
		 */

		// pm.executeQuery("select bcode from bcode");
		// pm.executeQuery("select bname from bcode");
		// pm.executeQuery("select bcode from bcode");
		// pm.executeQuery("select bname from bcode");
		// pm.executeQuery("select bcode from bcode");
		// pm.executeQuery("select bname from bcode");
		// pm.executeQuery("select bcode from bcode");
		// pm.executeQuery("select bname from bcode");
		//
		// pm.executeQuery("select bcode from bcode");
		// pm.executeQuery("select bname from bcode");
		//
		// pm.executeQuery("select bcode from bcode");
		// pm.executeQuery("select bname from bcode");
		// pm.executeQuery("select bcode from bcode");
		// pm.executeQuery("select bname from bcode");
		// con.setAutoCommit(false);

		// final PoolMan pm = new PoolMan();
		// Connection con = null;
		/*
		 * con = pm.getPool("test");
		 * 
		 * ResultSet rs = pm.executeQuery("select bcode from bcode");
		 * while(rs.next()) { System.out.println( rs.getString("bcode")); }
		 * 
		 * CallableStatement proc = null; proc =
		 * pm.getConnection("test").prepareCall(
		 * "{call insert_new_rowX(?,?,?,?,?,?,?,?,?)}"); proc.setString(1,
		 * "storeslist"); //set @p1=1,@p2=1; //CALL insert_new_rowX(
		 * 'storeslist',@p1,'busistatus,inoutflag,busicode,bcode',"'1','1','1100','9999'",@p2,'dINC','','4','codename');
		 * proc.setInt(2, 1); proc.registerOutParameter(2,
		 * java.sql.Types.INTEGER); proc.setString(3,
		 * "busistatus,inoutflag,busicode,bcode"); proc.setString(4,
		 * "'1','1','1100','9999'"); proc.setInt(8, 4);//默认长度4 proc.setString(5,
		 * ""); proc.registerOutParameter(5, java.sql.Types.VARCHAR);
		 * //if(si.getBusiparmas()!=null &&
		 * si.getBusiparmas().containsKey("CODENAMEPARAMS"))
		 * 
		 * 
		 * 
		 * 
		 * 
		 * proc.setString(6, "INC"); //}
		 * 
		 * //if(si.getBusiparmas()!=null &&
		 * si.getBusiparmas().get("CODENAMEFILTER")!=null) {
		 * proc.setString(7," 1=1"); }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * { proc.setString(9, "codename"); }
		 * 
		 * proc.executeUpdate(); //icode = proc.getInt(2);
		 * System.out.println(proc.getInt(2)+"");
		 * System.out.println(proc.getString(5)); //
		 * substring(concat('00000000000000000000', //
		 * cast(ifnull(max(cast(substring( bcode,2,4) as SIGNED)),0)+1 as
		 * char(20))), // LENGTH (concat('00000000000000000000', //
		 * cast(ifnull(max(cast(substring(bcode,2,4) as SIGNED)),0)+1 as
		 * char(20))))-3+1,4 )
		 */
		// }

	}
}
