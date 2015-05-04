package com.base.myproject.server.database.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class DBExport {

	String[][] dbexport;
	public DBExport(String[][] dbexport)
	{
		this.dbexport = dbexport;
		
	}
	/**
	 * hm 结构 “tablename” ,hmdata 
	 * hmdata(hashtable) 里 TABLENAME ，ROWDATA 其中ROWDATA里放的是vector。vector里放的是ht，key是column，
	 * @param file
	 * @throws Exception
	 */
	public void Export(File file) throws Exception 
	{
		java.util.Hashtable<String, Object> hm = new java.util.Hashtable<String, Object>();
		java.io.FileOutputStream foutputstream = new java.io.FileOutputStream(file);
		ObjectOutputStream objectoutputstream = new ObjectOutputStream(foutputstream); 
		for(int i=0;i<dbexport.length;i++)
		{			
			System.out.println("table="+dbexport[i][0]);
			ExportItem ei = new ExportItem(dbexport[i][0],dbexport[i][1]);
			hm.put(dbexport[i][0], ei.Export());
		}
		objectoutputstream.writeObject(hm);
		objectoutputstream.flush();
		objectoutputstream.close();
	}
}
