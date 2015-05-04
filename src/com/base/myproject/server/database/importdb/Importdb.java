package com.base.myproject.server.database.importdb;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Vector;

import com.base.myproject.server.database.PoolMan;
import com.base.myproject.server.database.export.DBNULL;

public class Importdb {
	PoolMan pm = new PoolMan();
	Connection con ;
	public Importdb() throws Exception
	{
		 con = pm.getPool("test");
	}

	public void Import(File file) throws Exception
	{
		FileInputStream in = new FileInputStream(file); 
		ObjectInputStream objectstream = new ObjectInputStream(in); 
		
	//	con.prepareStatement(sql)
		java.util.Hashtable<String, Object> hm= (java.util.Hashtable<String, Object>)objectstream.readObject();
		for(String key:hm.keySet())
		{
			java.util.Hashtable<String, Object> hmdata = (java.util.Hashtable<String, Object>)hm.get(key);
			String tablename =key;
			
			DatabaseMetaData dbMeta = con.getMetaData();
			ResultSet tblRSet = dbMeta.getColumns(null, null, tablename, null);
			
			
			Vector<String> exceptV = new Vector<String>();
			 
			while(tblRSet.next())
			{
				if(tblRSet.getObject("TYPE_NAME").toString().indexOf("identity")>0)
				{
					exceptV.add(tblRSet.getObject("COLUMN_NAME").toString());
				}
//				System.out.println("---");
//				System.out.println(tblRSet.getObject("COLUMN_NAME"));
//				System.out.println(tblRSet.getObject("DATA_TYPE"));
//				System.out.println(tblRSet.getObject("TYPE_NAME"));
//				System.out.println(tblRSet.getObject("COLUMN_DEF"));
//				System.out.println(tblRSet.getObject("SQL_DATA_TYPE"));
//				System.out.println(tblRSet.getObject("SOURCE_DATA_TYPE"));
//				
//				System.out.println("---");
			}
			tblRSet.close();
//			 PreparedStatement ps = null;
			 dbPreparedStatement dbps = new dbPreparedStatement();
			//for(String datakey:hmdata.keySet())
			{
				
				Vector<Hashtable<String,Object>> v = (Vector<Hashtable<String,Object>>)hmdata.get("ROWDATA");
				for(java.util.Hashtable<String, Object> data :v)
				{
					/*for(String datakey:data.keySet())
					{
						System.out.println(data.get(datakey));
						insert(data,tablename);
					}*/
					System.out.println(".tablename="+tablename);
					insert(data,tablename,exceptV,dbps);
				}
			}
			if(dbps!=null && dbps.getPreparedsstatement()!=null)
				System.out.println(dbps.getPreparedsstatement().executeBatch().length);
			
		}
		pm.close("test");
		//ResultSet rs = pm.executeQuery(this.sql);
	}
	
	public void insert(Hashtable<String,Object> hm,String table,Vector exceptV,dbPreparedStatement ps ) throws Exception
	{
//		PoolMan pm = new PoolMan();
//		Connection con = pm.getPool("test");
		
//		ResultSet tblRSet = dbMeta.getTables(null, null, null, new String[]{"TABLE"});
		StringBuffer sql = new StringBuffer("insert into  "+table +"(");
		System.out.println("table="+table);
		
		int count=0;
		for(String key:hm.keySet())
		{
			if(exceptV!=null && exceptV.contains(key))
				continue;
			sql.append(key+",");
//			System.out.println("table="+table+"  key="+key);
			count++;
		}
		
	//	ps.add
		//
		sql.deleteCharAt(sql.length()-1);
		sql.append(") values (");
		for(int i=0;i<count;i++)
		{
			sql.append("?,");
			
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(")");
		System.out.println(sql);
		if(ps.getPreparedsstatement()==null)
			ps.setPreparedsstatement(con.prepareStatement(sql.toString()));
		
		int i = 1;
		for(String key:hm.keySet())
		{
			if(exceptV!=null && exceptV.contains(key))
				continue;
			
			if(!hm.get(key).equals(DBNULL.getInstance()))
			{
				System.out.println("key = "+key+" value="+hm.get(key)+" null="+DBNULL.getInstance());
				ps.getPreparedsstatement().setObject(i++, hm.get(key));
			}
				
			else
				ps.getPreparedsstatement().setObject(i++, "");
		}
		ps.getPreparedsstatement().addBatch()	;
	}
	class dbPreparedStatement
	{
		PreparedStatement preparedsstatement = null;
		public dbPreparedStatement()
		{
			
		}
		public PreparedStatement getPreparedsstatement() {
			return preparedsstatement;
		}
		public void setPreparedsstatement(PreparedStatement preparedsstatement) {
			this.preparedsstatement = preparedsstatement;
		}
		
		
	}
}

