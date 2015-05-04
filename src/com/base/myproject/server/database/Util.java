/*
 * Util.java
 *
 * Created on 2006��8��20��, ����2:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.base.myproject.server.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.base.myproject.server.database.poolx.ConnectPoolxmgr;
import com.base.myproject.server.database.poolx.ConnectionObject;


public class Util {
    
    static String forum = "";
    //��д�����html��ʾ��ʽ,
    /*
     *str Ϊ����
     *code Ϊ��ݿ�codetable�����
     *idx Ϊ��ݿ�codetable����� ���
     *code idx Ϊ����̳�Ŵ��Ĳ���,
     *pagenum ��һҳΪ0
     *������޾������һҳ
     **/
    public static String HTMLEncode(String str,String code,String idx,int pagenum) {
        String[] strarr= str.split("\n");
        java.lang.StringBuffer bs=new StringBuffer();
        int len = strarr.length;
        //
        //ÿҳ������
        int onepagelen = 120;
        //��ҳ��
        int totlepagenum = strarr.length/onepagelen+(strarr.length%onepagelen==0?0:1);
        int i=onepagelen* (pagenum>=totlepagenum?totlepagenum:pagenum);
        
        int tempi=i;
        for( ;i<len && i <onepagelen+tempi ;i++) {
            
            bs.append(strarr[i]+"\n");
        }
        
        str = new String(bs);
        str=str.replaceAll("\n", "<LI>");
        str=str.replaceAll(" ", "&nbsp;");
        String    strtemp = "<table border=\"0\" width=\"100%\" align=\"left\">";
        //��һҳ��ʱ�����ʾͼƬ
        if (pagenum==0) {
            ArrayList arraylist =   getImagePath( code, idx);
            if(arraylist!=null)
                for(int k=0;k< arraylist.size();k++) {
                if(arraylist.get(k)!=null)
                    strtemp =strtemp +" <tr><td align=\"center\" valign=\"baseline\"><img border=\"0\" src=\""+arraylist.get(k).toString()+"\" width=\"146\" height=\"131\"></td></tr>";
                }
        }
        str = strtemp +"<tr><td><OL class='hl-main' ><LI>"+str;
        
        
        str = str+"</OL></td></tr>";
        str = str+"<tr><td align=\"center\" valign=\"baseline\">";
        for(int j=0;j<totlepagenum;j++) {
            str = str+"   <font color=\"#0000FF\"><a onclick=\"sendString('laction','"+code+":"+idx+":"+j+"','4');\">" +
                    "<span style=\"font:size=14 ;cursor:hand; text-decoration:underline\">"+j+"</span></a></font>";
        }
        str = str+"</td></tr>";
        //��������
        //  str = str+"<tr><td>"+getForumHTML(code,idx)+"</td></tr>";
        str = str+"</table>";
        //<jsp:include    page=\"forum.jsp?g_code=zgw\" flush=\"false\"/>
        return str;
    }
    public static String getForumHTML(String code,String idx) {
        
        String forum="<span style=\"\">";
        forum+="<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"84\"><tr></tr><tr valign=\"top\">";
        forum+="<form >	<td><input value=\"1\" name=\"Rating1\" id=\"Rating1\" type=\"RADIO\"/>̫�ã�</td></tr>";
        forum+="<tr valign=\"top\"><td><input value=\"1\" name=\"Rating1\" id=\"Rating2\" type=\"RADIO\"/>���Կ���</td></tr>";
        forum+="<tr valign=\"top\"><td><input value=\"1\" name=\"Rating1\" id=\"Rating3\" type=\"RADIO\"/>�ղ�</td></tr>";
        forum+="<tr valign=\"top\"><td><input value=\"1\" name=\"Rating1\" id=\"Rating4\" type=\"RADIO\"/>ѧϰ</td></tr>";
        forum+="<tr valign=\"top\"><td><input value=\"1\" name=\"Rating1\" id=\"Rating5\" type=\"RADIO\"/>���</td></tr></form >	";
        forum+="</table>";
        forum+="	<span>";
        forum+="<p></p>";
        
        forum+="</span>";
        
        
        forum+="<p><textarea rows=\"9\" name=\"S1\" cols=\"59\" id=\"forumtext\"></textarea></p>";
        forum+="<span>";
        forum+="<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"600\">";
        forum+="<tr valign=\"top\"><td width=\"254\"> ";
        forum+="�û���:<font size=\"3\" ><input name=\"username\" size=\"20\" width=\"50%\" id =\"usernameid\"></font>";
        forum+="</td>";
        forum+="<td >";
        forum+="����:<font size=\"3\" ><input name=\"password\" size=\"20\" width=\"50%\" type=password id = \"passwordid\"></font>";
        forum+="</td>";
        forum+="</tr>";
        forum+="<tr>";
        forum+="<td width=\"254\">";
        forum+="<span>";
        forum+=" У����:<font size=\"3\"><input name=\"T1\" size=\"12\" ></font></span></td>";
        forum+=" <td>";
        forum+="</td>";
        forum+="</tr>";
        forum+="</table>";
        forum+="</span>";
        
        forum+="<p><font size=\"3\"><input type=\"submit\" value=\"�ύ\" name=\"B1\" onclick=\"sendStringUnReturn('forumaction','"+code+"','"+idx+"','90');\" ></font></p>";
        forum+="<p><font color=\"000000\">. ��ע���û����ܷ������¡�δע���û��뷵��ҳע�ᡣ<br>";
        forum+=". ��ȷ�������������ع�ҷ��ɷ��桢���������<br>";
        forum+=". ���������Ը����ɴ˲����һ�к�������߸��𣬱���վ���е��κ����Ρ�</font></p>";
        forum+="</span>    ";
        forum+="<input type=\"hidden\" name=\"codetablecode\" value=\"1\" onclick=\"sendString('forumaction','121','90');\">";
        // System.out.println(forum);
        
        return forum;
    }
    static java.util.ArrayList getImagePath(String code,String idx) {
        //PoolMan pm = new PoolMan();
       // Connection con = null;
    	 ConnectionObject con = ConnectPoolxmgr.getInstance()
					.getConnectionObject("test");
					
        ResultSet rs = null;
        java.util.ArrayList  arraylist = new java.util.ArrayList();
        String sql  = "SELECT * FROM codelinkimage where icode='"+code+"' and id='"+idx+"'";
        try {
            
           
            rs = con.executeQuery(sql);
            
            while (rs.next()) {
                System.out.println( rs.getString("imagepath"));
                arraylist.add(rs.getString("imagepath")) ;
            }
            return  arraylist;//rs.getArray("imagepath");
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally{
        	try {
				ConnectPoolxmgr.getInstance().freeusing(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
        return null;
    }
    static public String postERRORJSP() {
        String error = "<p>ʵ�ڱ�Ǹ!���������Ϣ˵�����ǵĴ����Ѿ��ǳ�����!</p><p>�ǳ���л���ܹ����������Ϣͨ���ʼ����ݸ�����.����һ����ʱ�������.</p><p>����������ʼ�������ð���������:</p><p>ʱ��:</p><p>����һ��������ҳ�ĸ�λ��:</p><p>�������Щ����:</p><p>�뷢�͵�:javazgw@gmail.com</p>";
        return error;
        
    }
    //���ַ�ת��Ϊ 
    /***
     * &amp;                        &
     *
     * &lt:                         <
     *
     * &gt                         >
     *
     * &quot;                       "
     *
     * &apos;                       '
     *
     *
     ***/
    static public String XMLEncode(String str){
        str=str.replaceAll("&", "&amp;");
        str=str.replaceAll("<", "&lt;");
        str=str.replaceAll(">", "&gt;");
 
        str=str.replaceAll("\"", "&quot;");
        str=str.replaceAll("'", "&#39;");
        System.out.println(str);
        return str;
    }
}

