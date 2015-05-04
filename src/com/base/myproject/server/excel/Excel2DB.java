/**
 * 
 */
package com.base.myproject.server.excel;

import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.base.myproject.server.database.PoolMan;
import com.base.myproject.server.groovy.GroovyEngine;


/**
 * 
 * 有极其严重的漏洞，万一有人在excel破坏执行破坏性的sql就惨了。如何限制呢？
 * 这些代码暴露在excel里。我需要加密吗？
 * 
 * 指定格式的excel导入到数据库
 * @author Administrator
 *excel 格式
 *第一行第一列表名，第二列groovy程序 执行函数beforeImport() afterImport()
 *第二行字段名称
 *第三行字段数据类型
 *第四行保留行，查询用
bcode	
bcode	bname
varchar	
select bcode,bname from bcode	
1929	dkdkd
393993	djla
 */
public class Excel2DB {

	String filename ;
	String tablename ;
	Cell[] columns;
	Cell[] types;
	Cell[] codename;
	int columnscount;
	int rows;
	String groovystr;
	HashMap<String , String> defaultvalues = new HashMap<String , String>();
	GroovyObject groovyObject = null;

	public Excel2DB(String filename)
	{
		this.filename = filename;
	}
	public boolean Import() throws BiffException, IOException, SQLException
	{
		
		Workbook workbook = Workbook.getWorkbook(new File(filename));
		Sheet sheet = workbook.getSheet(0);
		columnscount = sheet.getColumns() ;
		rows = sheet.getRows();
		tablename = sheet.getCell(0, 0).getContents();
		groovystr = sheet.getCell(1, 0).getContents();
		System.out.println("groovystr="+groovystr);
		columns = sheet.getRow(1);
		types = sheet.getRow(2);
		codename = sheet.getRow(3);
	
		beforeImport();
		PoolMan pm = new PoolMan();
		String insertsql = "insert into "+tablename +"  (";
		String values = "";
		for(int i = 0 ; i<columns.length;i++)
		{
			insertsql +=columns[i].getContents()+",";
			values +="?,";
		}
	
		
		//默认值 
		//此处设置默认值只是得到默认值的个数，方便组织sql语句
		//zgw 20120226
		if (!(groovystr==null || groovystr.trim().equals("")))
		{
		 groovyObject = GroovyEngine.getInstance().loadstr(groovystr).newInstance();
		//返回的值应该是个HashMap
		@SuppressWarnings("unchecked")
		HashMap<String ,String> obj =(HashMap<String ,String>)groovyObject.invokeMethod("ImportSettingDefaultValues", new Object[] {});
		defaultvalues.putAll(obj);
		}
		//默认值
		for(String key:defaultvalues.keySet())
		{
			insertsql +=key+",";
			values +="?,";
		}
		
		insertsql = insertsql.substring(0, insertsql.length()-1);
		values = values.substring(0, values.length()-1);
		insertsql +=") values ("+values+")";
		
		System.out.println(insertsql);
		Connection CON = pm.getConnection("test");
		boolean isautocommit =CON.getAutoCommit();
		CON.setAutoCommit(false);
		try{
		PreparedStatement ps = CON.prepareStatement(insertsql);
		boolean isempty;
		int inportrows = 0;
		for(int j = 4;j<rows;j++)
		{
	//		ps.clearParameters();
			isempty = true;//默认空
			
			for(int k = 0 ; k<columns.length;k++)
			{
				//System.out.print(sheet.getCell(k, j).getContents().trim());
				//System.out.print("   ");
				if(isempty && !sheet.getCell(k, j).getContents().trim().equals(""))
				{
					//System.err.print(" kong "+sheet.getCell(k, j).getContents());
					isempty  = false;
				}
//				if(types[k].getContents().equalsIgnoreCase("double"))
//					ps.setDouble(k+1, Double.parseDouble(sheet.getCell(k, j).getContents().trim()));
				ps.setString(k+1, sheet.getCell(k, j).getContents().trim());
				
			}
			
			inportrows ++;
			
			//默认值 
			//此处是真正设置默认值，每行执行的目的是为了有些特殊的譬如objectid是实时计算的并不是固定值
			//zgw 20120226
			if (!(groovystr==null || groovystr.trim().equals("")))
			{
			
			//返回的值应该是个HashMap
			@SuppressWarnings("unchecked")
			HashMap<String ,String> obj =(HashMap<String ,String>)groovyObject.invokeMethod("ImportSettingDefaultValues", new Object[] {});
			defaultvalues.putAll(obj);
			}
			
			
			//默认值
			int  k=0;
			for(String value:defaultvalues.values())
			{
				ps.setString(++k +columns.length , value);
			}
			
			
			//行里面无值,跳出到下一行
			if(isempty)
			{
			
				ps.clearParameters();
				
				continue;
			}
		
			ps.addBatch();
		}
		System.out.println("inport row ="+inportrows);
		//System.out.println(ps);
//		ps.execute();
		System.out.println(ps.executeBatch());
		afterImport();
		workbook.close();
		CON.commit();
		}
		catch(Exception e)
		{
			CON.rollback();
			System.err.println("roolback");
			e.printStackTrace();
		}
		finally
		{
			CON.setAutoCommit(isautocommit);
		}
		return false;
	}
	private void beforeImport()
	{
		if(!(groovystr==null || groovystr.trim().equals("")))
		{
		 groovyObject = GroovyEngine.getInstance().loadstr(groovystr).newInstance();
		Object obj = groovyObject.invokeMethod("beforeImport", new Object[] {});
		}
	}

	private void afterImport()
	{
		if (!(groovystr==null || groovystr.trim().equals("")))
		{
		 groovyObject = GroovyEngine.getInstance().loadstr(groovystr).newInstance();
		Object obj = groovyObject.invokeMethod("afterImport", new Object[] {});
	
		}
		
		}
	public static void main(String[] argv) throws BiffException, IOException, SQLException
	{
		int limit = 1;
		Thread[] t_array = new Thread[limit];
		for(int i=0;i<limit;i++)
		{
		Thread t = new Thread(){
			public void run()
			{
				try {
					Excel2DB e2d = new Excel2DB("d:\\tempt\\huiyuan-4.xls");
					HashMap<String, String> d = new HashMap<String, String>();
					d.put("codename", "2");
					e2d.setDefaultvalues(d);
					e2d.Import();
				} catch (BiffException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t_array[i]= t;
		}
		for(int i =0;i<limit;i++)
		{
			t_array[i].start();
		}
//	t.start();
		
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public Cell[] getColumns() {
		return columns;
	}
	public void setColumns(Cell[] columns) {
		this.columns = columns;
	}
	public Cell[] getTypes() {
		return types;
	}
	public void setTypes(Cell[] types) {
		this.types = types;
	}
	public Cell[] getCodename() {
		return codename;
	}
	public void setCodename(Cell[] codename) {
		this.codename = codename;
	}
	public int getColumnscount() {
		return columnscount;
	}
	public void setColumnscount(int columnscount) {
		this.columnscount = columnscount;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public HashMap<String, String> getDefaultvalues() {
		return defaultvalues;
	}
	public void setDefaultvalues(HashMap<String, String> defaultvalues) {
		this.defaultvalues = defaultvalues;
	}
	
}
