/*
 * Main.java
 *
 * Created on 2006��8��20��, ����2:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.base.myproject.server.database;


import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.base.myproject.server.database.poolx.ConnectPoolxmgr;
import com.base.myproject.server.database.poolx.ConnectionObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.base.myproject.server.init.ProConfig;
public class Main {
	
	/**
	 * @param str
	 * @throws IOException
	 */
	public static void main2(String[] str) throws IOException
	{

//		ProConfig proconfig = ProConfig.getInstance();
//		System.out.println(proconfig.getProperties().get("drivers"));
//		
		
		String sql = "select * from hr_hostel_amount  where datediff(month,'2010-09-08',createdate )=0 order  by createdate desc";
//		Pattern  pa = Pattern .compile("\\s*([a-zA-Z_0-9]+)\\s*=\\s*([^;]+);");
//		
//		Matcher m = pa.matcher(sql);
		sql = sql.replaceAll("order\\s*by \\s*(\\w*\\s*)(?i)(desc|asc|\\W)", " ");
		System.out.println(sql);
	}
	public static void main(String[] str) throws Exception
	{
        ConnectionObject con2 = ConnectPoolxmgr.getInstance()
                .getConnectionObject("test");
        con2.replacecolumn("systemsetting","name","1231");

        if(1==1)
            return;
		final PoolMan pm = new PoolMan();
	        Connection con = null;
	        con = pm.getPool("test");
	     //   pm.Update("");
	        
	        JFrame jf = new JFrame();
	       final TextArea ta = new TextArea("sadfasd");
	       JButton getvalue = new JButton("获取");
	        JButton jb = new JButton("提交（不能回退）");
	       final JTextField jtf = new JTextField(20);
	        //ta.setSize(640, 400);
	        ta.setRows(20);
	        ta.setColumns(50);
	        JLabel jl = new JLabel("关键字");
	        jl.setLabelFor(jtf);
	        
	        jf.getContentPane().setLayout(new FlowLayout());
	        jf.getContentPane().add(jl);
	        jf.getContentPane().add(jtf);
	        jf.getContentPane().add(ta);
	        jf.getContentPane().add(getvalue);
	        jf.getContentPane().add(jb);
	        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        jf.setVisible(true);
	        jf.setSize(640,480);
	        
	        jb.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						String value = ta.getText();
						value=value.replaceAll("'", "''");
						System.out.println(value);
						pm.Update("update sqltable set sqlvalue='"+value+"' where name='"+jtf.getText()+"'");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
	        	
	        });
	        
	        getvalue.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					
				String jtfstr = jtf.getText();
				ResultSet rs;
				try {
					rs = pm
							.executeQuery("select sqlvalue from sqltable where name='"
									+ jtfstr + "'");

					if (rs.next()) {
						ta.setText(rs.getString("sqlvalue"));
					}

					else {
						ta.setText("没有获得数据");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					
				}
	        	
	        });
	        
	        
	        
	}
    public static void main9(String[] str) throws SQLException {
    	//ProConfig proconfig = ProConfig.getInstance();
		System.out.println(ProConfig.getInstance().getProperties().get("drivers"));
		System.out.println(ProConfig.getInstance().getProperties().get("url"));
		System.out.println(ProConfig.getInstance().getProperties().get("maxconn"));
        String driverClasses = "123\n21`";
       // jdbc1() ;
       
//        StringTokenizer st =  new StringTokenizer(driverClasses);
//      while (st.hasMoreElements()) {
//			String driverClassName = st.nextToken().trim();
//                        System.out.println("driverclassname="+driverClassName);
//      }
      
         PoolMan pm = new PoolMan();
        Connection con = null;
        java.util.Date d = new java.util.Date(System.currentTimeMillis());
        System.out.println(d.getMinutes() + ":" + d.getSeconds());
        ResultSet rs = null;
        String icode = "";
     //   Image image = null;
     //   byte[] data = null;
        for (int i = 0; i < 1; i++) {
            try {
                
                con = pm.getPool("test");
                rs = pm.executeQuery("SELECT * FROM gcode");
                rs.next();
            //    data = rs.getBytes("image1");
                System.out.println(rs.getString("gcode"));
             //   image = Toolkit.getDefaultToolkit().createImage(data);// dsafffffffffffffffffffffss
           //     icode = rs.getString("keyword");
                // pm.close("test");
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    pm.close("test");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
        //icode = null;//Util.HTMLEncode(icode);
       // System.out.println("icode=" + icode);
       
   //   testmysql();
        java.util.Date d2 = new java.util.Date(System.currentTimeMillis());
        System.out.println("����" + d2.getMinutes() + ":" + d2.getSeconds());
        
    }
    
    public static void testmysql()
    {
            PoolMan pm = new PoolMan();
        Connection con = null;

        ResultSet rs1 = null;
        try {
            con = pm.getPool("test");
            
            rs1 = pm.executeQuery("SELECT * FROM gcode");
            rs1.next();
                System.out.println(rs1.getString("gcode"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
           public static void main1 (String args[]) throws Exception {
    Main test = new Main();
    test.Test();
  }
             public void Test() throws Exception {
    // Get connection
    /*DriverManager.registerDriver(new
    com.microsoft.jdbc.sqlserver.SQLServerDriver());
    Connection connection = DriverManager.getConnection(
    "jdbc:microsoft:sqlserver://127.0.0.1:1433;databaseName=javaweb","sa","");
  */  
//                 testmysql();
//                 System.out.println("--begin--");
//     DriverManager.registerDriver(new  com.mysql.jdbc.Driver());
//    Connection connection = DriverManager.getConnection(
//"jdbc:mysql://127.0.0.1:3306/dbio?useUnicode=true&characterEncoding=GBK","root","123321");
//    System.out.println("--end--");
            	  String   url="jdbc:inetdae7:10.1.22.134:1433?charset=gb2312&database=MyDatabase";   
//            	  String   driver="com.inet.tds.TdsDriver";   			 
//            			 DriverManager.registerDriver(new   com.inet.tds.TdsDriver());
             	
            	 Connection connection = DriverManager.getConnection(
            			"jdbc:inetdae7:127.0.0.1:1433?charset=gb2312&database=test","sa","");

            	 /*DriverManager.registerDriver(new   com.microsoft.jdbc.sqlserver.SQLServerDriver());
            	
            	 Connection connection = DriverManager.getConnection(
            			"jdbc:microsoft:sqlserver://127.0.0.1:1433;databaseName=test","sa","");


            	 */
            	 //            			    System.out.println("--end--");
//  
//   // jdbc:jtds:sqlserver://host\instance:port/database
    if (connection != null) {
//      System.out.println();
//      System.out.println("Successfully connected");
//      System.out.println();
//      // Meta data
//      DatabaseMetaData meta = connection.getMetaData();
//      System.out.println("\nDriver Information");
//      System.out.println("Driver Name: "
//        + meta.getDriverName());
//      System.out.println("Driver Version: "
//        + meta.getDriverVersion());
//      System.out.println("\nDatabase Information ");
//      System.out.println("Database Name: "
//        + meta.getDatabaseProductName());
//      System.out.println("Database Version: "+
//      meta.getDatabaseProductVersion());
            try {
                
                
                
                java.sql.Statement   stmt =  connection.createStatement();
                ResultSet  result = stmt.executeQuery("SELECT * FROM gcode");
                result.next();
                System.out.println(result.getString("gcode"));
            } finally {
            }
    }
        }
             
             public static  void jdbc1() throws SQLException {
//         		
//         		DriverManager.registerDriver(new  com.mysql.jdbc.Driver());
//             	
//            	 Connection connection = DriverManager.getConnection(
//            			"jdbc:mysql://localhost/zz?useUnicode=true&characterEncoding=GBK","root","123321");
//              java.sql.Statement   stmt =  connection.createStatement();
//              ResultSet  result = stmt.executeQuery("SELECT * FROM gcode");
//              result.next();
//              System.out.println(result.getString("gcode"));

//         		PoolMan pm = new PoolMan();
//         		Connection con = null;
//         		ResultSet rs1 = null;
         //
//         		try {
//         		//	con = pm.getPool("test");
         //
//         			rs1 = pm
//         					.executeQuery("select * from usertbl");
//         			
//         			while(rs1.next())
//         			{
//         				System.out.println(rs1.getString("gcode"));
//         			}
//         		
//         		} catch (Exception ex) {
//         			ex.printStackTrace();
//         		}
         	}

    public static void   xmltest()
       {

	   //Create the Document object   
      Document mapDoc = null;
	  //Define a new Document object
      Document dataDoc = null;
      //Create the new Document
      Document newDoc = null;
      try {
         //Create the DocumentBuilderFactory
         DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
         //Create the DocumentBuilder
         DocumentBuilder docbuilder = dbfactory.newDocumentBuilder();
         //Parse the file to create the Document
         mapDoc = docbuilder.parse("mapping.xml");
		 //Instantiate a new Document object
         dataDoc = docbuilder.newDocument();

         //Instantiate the new Document
         newDoc = docbuilder.newDocument();
	  } catch (Exception e) {
         System.out.println("Problem creating document: "+e.getMessage());
      }

	  //Retrieve the root element 
      Element mapRoot = mapDoc.getDocumentElement();
      //Retrieve the (only) data element and cast it to Element
      Node dataNode = mapRoot.getElementsByTagName("data").item(0);
      Element dataElement = (Element)dataNode;
      //Retrieve the sql statement
      String sql = dataElement.getAttribute("sql");
	  
	  //Output the SQL statement
      System.out.println(sql);
	   
      //For the JDBC-ODBC bridge, use
      //driverName = "sun.jdbc.odbc.JdbcOdbcDriver"
      //and
      //connectURL = "jdbc:odbc:pricing"
      
             PoolMan pm = new PoolMan();
        Connection db = null;
      
  
      //Create the ResultSetMetaData object, which will hold information about
      //the ResultSet
      ResultSetMetaData resultmetadata = null;
	  
	  //Create a new element called "data"	  
      Element dataRoot = dataDoc.createElement("data");
        try {
            db = pm.getPool("test");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
     
   
      //Create the Statement object, used to execute the SQL statement
      PreparedStatement statement = null;
      //Create the ResultSet object, which ultimately holds the data retreived
      ResultSet resultset = null;
      try {
         statement = db.prepareStatement("select * from maintable");
         //Execute the query to populate the ResultSet
         resultset = statement.executeQuery();
         
		 //Get the ResultSet information
         resultmetadata = resultset.getMetaData();
         //Determine the number of columns in the ResultSet
         int numCols = resultmetadata.getColumnCount();
		 
		 //Check for data by moving the cursor to the first record (if there is one)
         while (resultset.next()) {
            //For each row of data
            //Create a new element called "row"
            Element rowEl = dataDoc.createElement("row");
            for (int i=1; i <= numCols; i++) {
               //For each column index, determine the column name
               String colName = resultmetadata.getColumnName(i);
               //Get the column value
               String colVal = resultset.getString(i);
			   //Determine if the last column accessed was null
               if (resultset.wasNull()) {
                  colVal = "and up";
               }
               //Create a new element with the same name as the column
               Element dataEl = dataDoc.createElement(colName);
               //Add the data to the new element
               dataEl.appendChild(dataDoc.createTextNode(colVal));
               //Add the new element to the row
               rowEl.appendChild(dataEl);
            }
            //Add the row to the root element			
            dataRoot.appendChild(rowEl);

         }
      } catch (SQLException e) {
         System.out.println("SQL Error: "+e.getMessage());
      } finally {
         System.out.println("Closing connections...");
         try {
            db.close();
         } catch (SQLException e) {
            System.out.println("Can't close connection.");
         }
	  }
	  //Add the root element to the document
      dataDoc.appendChild(dataRoot);
      //Retrieve the root element (also called "root")
      Element newRootInfo = (Element)mapRoot.getElementsByTagName("root").item(0);
      //Retrieve the root and row information
      String newRootName = newRootInfo.getAttribute("name");
      String newRowName = newRootInfo.getAttribute("rowName");
      //Retrieve information on elements to be built in the new document
      NodeList newNodesMap = mapRoot.getElementsByTagName("element");

	  //Create the final root element with the name from the mapping file
      Element newRootElement = newDoc.createElement(newRootName);
	  
      //Retrieve all rows in the old document
      NodeList oldRows = dataRoot.getElementsByTagName("row");
      for (int i=0; i < oldRows.getLength(); i++){
	   
         //Retrieve each row in turn
         Element thisRow = (Element)oldRows.item(i);
		 
         //Create the new row
         Element newRow = newDoc.createElement(newRowName);
         			   
         for (int j=0; j < newNodesMap.getLength(); j++) {
             
            //For each node in the new mapping, retrieve the information 			 
            //First the new information...  
            Element thisElement = (Element)newNodesMap.item(j); 
            String newElementName = thisElement.getAttribute("name");	

            //Then the old information
            Element oldElement = (Element)thisElement.getElementsByTagName("content").item(0);
            String oldField = oldElement.getFirstChild().getNodeValue();


            //Get the original values based on the mapping information
            Element oldValueElement = (Element)thisRow.getElementsByTagName(oldField).item(0);
            String oldValue = oldValueElement.getFirstChild().getNodeValue();
			
            //Create the new element
            Element newElement = newDoc.createElement(newElementName);
            newElement.appendChild(newDoc.createTextNode(oldValue));
            
			//Retrieve list of new elements
            NodeList newAttributes = thisElement.getElementsByTagName("attribute");
            for (int k=0; k < newAttributes.getLength(); k++) {
               //For each new attribute
               //Get the mapping information
               Element thisAttribute = (Element)newAttributes.item(k);
               String oldAttributeField = thisAttribute.getFirstChild().getNodeValue();
               String newAttributeName = thisAttribute.getAttribute("name");

               //Get the original value
               oldValueElement = (Element)thisRow.getElementsByTagName(oldAttributeField).item(0);
               String oldAttributeValue = oldValueElement.getFirstChild().getNodeValue();
		
               //Create the new attribute		
               newElement.setAttribute(newAttributeName, oldAttributeValue);
            }
            
			//Add the new element to the new row
            newRow.appendChild(newElement);

         }
         //Add the new row to the root
         newRootElement.appendChild(newRow);
      }  
      //Add the new root to the document
      newDoc.appendChild(newRootElement);
	  
	  System.out.println(newRootElement.toString());
      }

       public static void main3(String[] argv)
       {
           
           xmltest();
       }
}
