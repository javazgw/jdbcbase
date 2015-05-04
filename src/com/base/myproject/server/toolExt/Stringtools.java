/**
 * 
 */
package com.base.myproject.server.toolExt;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 *
 */
public class Stringtools {
	/*
	 * 解析的格式是
	tablename=hr_goods_get;busistatus=busistatus;endstatus=80;
	busicode=busicode;icode=icode;
	*/

	public static  Hashtable<String, String>  String2HashTable(String str)
	{
		Pattern  pa = Pattern .compile("\\s*([a-zA-Z_0-9]+)\\s*=\\s*([^;]+);");
		
		Matcher m = pa.matcher(str);
		Hashtable<String, String> ht = new Hashtable<String, String>();
		while(m.find())
		{
			System.out.println("found = "+m.group(1)+" start ="+m.start()+" end="+m.end());
			ht.put(m.group(1), m.group(2));
		}
		return ht;
	}
	
	public static void main(String[] argv)
	{
		String str = "tablename=hr_goods_get;busistatus=busistatus;endstatus=524288;busicode=busicode;icode=icode;updatesql_524288=update hr_goodsdetail set status=1 where upcode in (select codename from hr_goods_get where icode ='{icode}');";
	
		Hashtable<String, String> ht = String2HashTable(str);
		System.out.println(ht.get("updatesql_524288"));
		 
	}
}
