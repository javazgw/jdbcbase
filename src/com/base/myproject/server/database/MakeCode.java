/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.base.myproject.server.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.base.myproject.server.database.poolx.ConnectPoolxmgr;
import com.base.myproject.server.database.poolx.ConnectionObject;

/**
 * 分配内码用到
 * @author zgw
 */
public class MakeCode {
static PoolMan pm = new PoolMan();
   static     Connection con = null;
   static     ResultSet rs1 = null;
//   static boolean ismachicclose = true; //探测机器是否重启，如果重启则从数据库取数。否则从变量取数
//   static int incint = 0; //自增变量。
    private MakeCode() {
    }
    final static java.util.HashMap<String, Integer> ht = new java.util.HashMap<String, Integer>();
    final static java.util.HashMap<String, Integer> codenameht = new java.util.HashMap<String, Integer>();
/**
 * 根据表名和字段名分配自增编号
 * @param tblname
 * @param colname
 * @return
 */
    public static int makeCode(String tblname,String colname)
    {
    	return makeCode(tblname,colname,0,0);
    }
    public static int makeCode(String tblname, String colname,int start,int len) {
       
        if (ht.containsKey(tblname + "_" + colname)) {
            ht.put(tblname + "_" + colname, ht.get(tblname + "_" + colname).intValue()+1);
            return ht.get(tblname + "_" + colname).intValue();
        } else {
        	 ConnectionObject con = ConnectPoolxmgr.getInstance()
 					.getConnectionObject("test");
            try {
//                con = pm.getPool("test");
                //mysql的写法 用ifnull
                if(start==0&&len==0)
                {
                rs1 = con.executeQuery("SELECT isnull(max("+colname+"),0)+1 as maxnum FROM  " +tblname);
                }
                else if(start !=0 && len!=0)
                {
                	rs1 = con.executeQuery("SELECT isnull(max(SUBSTRING("+colname+","+start+","+len+")),0)+1 as maxnum FROM  " +tblname);
                }
                if(rs1.next())
                {
                    int num = rs1.getInt("maxnum");
                    ht.put(tblname + "_" + colname,num);
                    return num;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
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
        }
       
            throw new java.lang.Error("分配内码出错！");
             
        
    }
    /**
     * 分配外码用
     * 根据表明，字段名，外码开始字符自增加编码形成外码，自增外码是有长度限制的（len）
     * 
     * @param tblname
     * @param colname
     * @param stratstr
     * @param start
     * @param len 没考虑，默认为5位，懒得写了
     * @return
     */
    public static String makeCode(String tblname, String colname,String stratstr,int len) {
    	String outcode = "";
    	 if (ht.containsKey(tblname + "_" + colname+"_"+stratstr)) {
             ht.put(tblname + "_" + colname+"_"+stratstr, ht.get(tblname + "_" + colname+"_"+stratstr).intValue()+1);
            
             int code = ht.get(tblname + "_" + colname+"_"+stratstr);
             
             
             return stratstr+("00000"+ code).substring(("00000"+ code).length()-5);
         } 
    	 else
    	 {
    		 ConnectionObject con = ConnectPoolxmgr.getInstance()
 					.getConnectionObject("test");
             try {
//                 con = pm.getPool("test");
                 //mysql的写法 用ifnull
                 
                rs1 = con.executeQuery("select  max(right('0000000'+ cast(RIGHT("+colname+", 5)+1 as varchar(5)),5)) as codename from "+tblname+" where "+colname+" like '"+stratstr+"[0-9][0-9][0-9][0-9][0-9]'" );
                
                 if(rs1.next())
                 {
                     int num = rs1.getInt("codename");
                     ht.put(tblname + "_" + colname+"_"+stratstr,num);
                     return stratstr + ("00000"+ num).substring(("00000"+ num).length()-5);
                 }
                 else 
                 {
                	 ht.put(tblname + "_" + colname+"_"+stratstr,1);
                	
                	return stratstr+"00001";
                 }
             } catch (Exception ex) {
                 ex.printStackTrace();
             }
             finally
             {try {
 				ConnectPoolxmgr.getInstance().freeusing(con);
 			} catch (SQLException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
             }
         
    		 
    	 }
    	return outcode;
    	
    }
//    public static int makeCode() 
//    
//    {
//    	PoolMan pm = new PoolMan();
//    	
//    	int i=2147483647;
//    	return i;
//    }
    public static void main(String[] argv)
    {
        for(int i=0;i<100;i++)
        {
      System.out.println( makeCode("storeslist","codename","zgw43",5));
        }
    
    /*	Date d = new Date();
		java.text.SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		java.text.SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		String datestr = sdf.format(d);
		String ordercode = sdf2.format(d);
		System.out.println(ordercode);*/
    }
}
