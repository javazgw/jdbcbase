package com.base.myproject.server.timetask;

import java.util.Calendar;
import java.util.Date;
//用quartz
@Deprecated
public class SimpleTaskItem extends TaskItem{

	public SimpleTaskItem(Date startdatetime,long enddatetime,int times)
	{
		this.setTimes(times);
		this.setStartdate(startdatetime);
		this.setEnddatetime(enddatetime);
		
	}
	public boolean start()
	{
		
		super.start();
		System.out.println("--simples");
		

		return true;
		
	}
}
