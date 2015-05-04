package com.base.myproject.server.database.poolx;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: guangwangzhuang
 * Date: 14-1-15
 * Time: 上午12:10
 * To change this template use File | Settings | File Templates.
 */
public class DBtools {
   static DBtools dbts = null;
    private DBtools()
    {

    }
    public static DBtools getinstance()
    {
        if(dbts==null)
            dbts = new DBtools();

        return dbts;
    }
    public void Update(String insert_sql) throws SQLException {
        ConnectionObject con = ConnectPoolxmgr.getInstance()
                .getConnectionObject("test");
        try {


            System.out.println("---"+insert_sql);
              con.UpdateException(insert_sql);

        }

        finally{
            try {
                ConnectPoolxmgr.getInstance().freeusing(con);
            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }
    public void execute(String insert_sql) throws Exception {
        ConnectionObject con = ConnectPoolxmgr.getInstance()
                .getConnectionObject("test");
        try {



            con.execute(insert_sql);

        }

        finally{
            try {
                ConnectPoolxmgr.getInstance().freeusing(con);
            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }





}
