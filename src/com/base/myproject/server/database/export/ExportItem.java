/**
 * 
 */
package com.base.myproject.server.database.export;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.base.myproject.server.database.importdb.Importdb;
import com.base.myproject.server.database.poolx.ConnectPoolxmgr;
import com.base.myproject.server.database.poolx.ConnectionObject;

/**
 * @author Administrator
 *
 */
public class ExportItem implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3380347999905331284L;

	String table;
	String sql;
	
	public ExportItem(String table,String sql)
	{
		this.table = table;
		this.sql = sql;
	}
	public java.util.Hashtable<String, Object> Export() throws Exception
	{
//		PoolMan pm = new PoolMan();
//		pm.getPool("test");
		 ConnectionObject con = ConnectPoolxmgr.getInstance()
					.getConnectionObject("test");
			
			java.util.Hashtable<String, Object> hm = new java.util.Hashtable<String,Object>();
			java.util.Vector<Object> v = new java.util.Vector<Object>();
			try{
				
			
		ResultSet rs = con.executeQuery(this.sql);
		int count = rs.getMetaData().getColumnCount();
		while(rs.next())
		{
			
			java.util.Hashtable<String, Object> data = new java.util.Hashtable<String,Object>();
		for (int i = 1; i <= count; i++) {
			
			Object o = rs.getObject(i);
			if(o!=null)
				data.put(rs.getMetaData().getColumnName(i),o);
			else
				data.put(rs.getMetaData().getColumnName(i),DBNULL.getInstance());
				
			//ExportItem ei = new ExportItem(rs.getMetaData().getColumnName(i),rs.getObject(i));
			//v.add(ei);
//			objectoutputstream.writeObject(ei);
//			System.out.println(rs.getCharacterStream(i));
//			Reader reader = rs.getCharacterStream(i);
			//System.out.println(reader);
			//reader.r
//			if(reader!=null)
//			IOUtils.copy(reader, filewriter);
//			filewriter.write("##");
		}
			v.add(data);
		//reader.close( ); 
		}
		hm.put("TABLENAME", this.table);
		hm.put("ROWDATA", v);
		//objectoutputstream.writeObject(hm);
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
		return hm;
	}
	public static void aa( String a)
	{
		System.out.println("1 in a="+a);
		a="123";
		System.out.println("1 in a="+a);
	}
	public static void main(String[] argv) throws Exception
	{
	/*	 File file=new File("c://output.txt");
		 java.io.FileOutputStream foutputstream = new java.io.FileOutputStream(file);
			ObjectOutputStream objectoutputstream = new ObjectOutputStream(foutputstream); 
		ExportItem ei = new ExportItem("sqltable","select * from sqltable");
		ExportItem ei2 = new ExportItem("ccode","select * from ccode");
		
		ei.Export(objectoutputstream);
		ei2.Export(objectoutputstream);
		objectoutputstream.flush();
		objectoutputstream.close();*/
		DBExport dbexport = new DBExport(new String[][]{{"sqltable","select  * from sqltable"}});
		dbexport.Export(new File("c://output.txt"));
	
		Importdb id = new Importdb();
		id.Import(new File("c://output.txt"));
		
//		String ss = "321";
//		aa(ss);
//		System.out.println("1 out a="+ss);
		//id.insert(null, "bcode");
		/*
		PoolMan pm = new PoolMan();
		pm.getPool("test");
		ResultSet rs = pm.executeQuery("select * from sqltable");
		int count = rs.getMetaData().getColumnCount();
		 File file=new File("c://output.txt");
	
//		 java.io.FileWriter filewriter = new java.io.FileWriter(file);
		java.io.FileOutputStream foutputstream = new java.io.FileOutputStream(file);
		ObjectOutputStream objectoutputstream = new ObjectOutputStream(foutputstream); 
		Object o1 = new String("dd");
		objectoutputstream.writeObject("a");
		objectoutputstream.writeObject(o1);
		
		
		
		
		java.util.Hashtable<String, Object> hm = new java.util.Hashtable<String,Object>();
		java.util.Vector<Object> v = new java.util.Vector<Object>();
		while(rs.next())
		{
			
			java.util.Hashtable<String, Object> data = new java.util.Hashtable<String,Object>();
		for (int i = 1; i <= count; i++) {
			
			Object o = rs.getObject(i);
			if(o!=null)
				data.put(rs.getMetaData().getColumnName(i),o);
			//ExportItem ei = new ExportItem(rs.getMetaData().getColumnName(i),rs.getObject(i));
			//v.add(ei);
//			objectoutputstream.writeObject(ei);
//			System.out.println(rs.getCharacterStream(i));
//			Reader reader = rs.getCharacterStream(i);
			//System.out.println(reader);
			//reader.r
//			if(reader!=null)
//			IOUtils.copy(reader, filewriter);
//			filewriter.write("##");
		}
			v.add(data);
		//reader.close( ); 
		}
		hm.put("ROWDATA", v);
		objectoutputstream.writeObject(hm);
		objectoutputstream.flush();
		objectoutputstream.close();
		
		
		FileInputStream in = new FileInputStream(file); 
		ObjectInputStream s = new ObjectInputStream(in); 
		String a = (String)s.readObject();   //恢复对象; 
		String b = (String)s.readObject(); 
		java.util.Hashtable<String, Object> hm2= (java.util.Hashtable<String, Object>)s.readObject(); 
		System.out.println(hm2.get("ROWDATA"));
		
		System.out.println(a);
		System.out.println(b);
//		filewriter.flush();
//		filewriter.close( ); 
	*/}
}
