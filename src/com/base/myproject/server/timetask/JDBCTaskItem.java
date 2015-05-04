/**
 * 
 */
package com.base.myproject.server.timetask;

import java.util.Date;

/**
 * @author Administrator
 *
 */
//用quartz
@Deprecated
public class JDBCTaskItem extends TaskItem{

	public JDBCTaskItem(Date startdatetime,long enddatetime,int times)
	{
		this.setTimes(times);
		this.setStartdate(startdatetime);
		this.setEnddatetime(enddatetime);
	}
	
	public boolean start()
	{
		
		
		return true;
		
	}
	
}
