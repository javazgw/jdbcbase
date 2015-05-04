/**
 * 
 */
package com.base.myproject.server.timetask;

import java.util.Date;
import java.util.TimerTask;

/**
 * @author Administrator
 *		SimpleTaskItem sss= new SimpleTaskItem(new Date(),0,5);
		sss.setTarget_DAY(10);
		sss.setTarget_HOUR_OF_DAY(1);
		sss.setTarget_MIN_OF_HOUR(4);
		sss.setMode(sss.SECOND_OF_MIN);
		sss.setTimes(3);
 */
//用quartz
@Deprecated
public  class TaskItem extends TimerTask  {

	boolean isstart = false;
	boolean iscancel = false;
	//public long startdatetime=0;//开始时间，0是马上开始。
	public long enddatetime=0;//结束时间，0表示无结束时间
	//public long period = 2000;//*60*10;//10分钟；检查周期
	public  int times= -1;//执行次数，-1表示无次数限制
	public Date startdate=new Date();
	
	String isrunning ="";

	public final int MIN_OF_HOUR =1;
	public final int HOUR_OF_DAY= 2;
	public final int DAY_OF_WEEK = 4;
	public final int DAY_OF_MONTH = 8;
	public final int SECOND_OF_MIN =16;

	public int mode = 0; //时间模式
	public int target_MIN_OF_HOUR = 0;
	public int target_HOUR_OF_DAY = 0;
	public int target_DAY = 0;
	//public long target_DAY_OF_MONTH = 0;
	
	public boolean beforestart() {
		if(iscanceled())
			return false;
		return true;
	}

	public void doit()
	{
		Thread t = new Thread(this);
		t.start();
	}

	public boolean start() {
		
//		Thread t = new Thread(){
//			public void run()
//			{
				System.out.println("start");
		 RollCalendar rc = new RollCalendar();
		 Date now = new Date();
		 long a1 = 0;
		 switch(mode) {
		 case MIN_OF_HOUR://每小时中的几分
			 
			System.out.println( rc.getNextCheckDate(startdate, target_DAY, target_HOUR_OF_DAY, target_MIN_OF_HOUR, 1));
			  a1 = rc.getNextCheckDate(startdate, target_DAY, target_HOUR_OF_DAY, target_MIN_OF_HOUR, RollCalendar.MIN_OF_HOUR).getTime()-now.getTime();
			a1 = a1<=0?0:a1;
				
			  System.out.println(a1+"之后触发！！");
			  try {
					Thread.sleep(a1);
					startdate = new Date();
					started();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 break;
			 
		 case HOUR_OF_DAY:
			 
			  a1 = rc.getNextCheckDate(startdate, target_DAY, target_HOUR_OF_DAY, target_MIN_OF_HOUR, RollCalendar.TYPE_HOUR_OF_DAY).getTime()-now.getTime();
				a1 = a1<=0?0:a1;
			  System.out.println(a1/1000+"后触发");
			  try {
					Thread.sleep(a1);
					startdate = new Date();
					started();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 break;
		 case DAY_OF_WEEK:
			  a1 = rc.getNextCheckDate(startdate, target_DAY, target_HOUR_OF_DAY, target_MIN_OF_HOUR, RollCalendar.TYPE_DAY_OF_WEEK).getTime()-now.getTime();
				a1 = a1<=0?0:a1; 
			  try {
						Thread.sleep(a1);
						startdate = new Date();
						started();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 break;
	
			 
		 case DAY_OF_MONTH:
			  a1 = rc.getNextCheckDate(startdate, target_DAY, target_HOUR_OF_DAY, target_MIN_OF_HOUR,RollCalendar.TYPE_DAY_OF_MONTH).getTime()-now.getTime();
				a1 = a1<=0?0:a1; 
			  try {
						Thread.sleep(a1);
						startdate = new Date();
						started();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 break;
			 
		 case SECOND_OF_MIN:
				System.out.println( rc.getNextCheckDate(startdate, target_DAY, target_HOUR_OF_DAY, target_MIN_OF_HOUR,RollCalendar.TYPE_SEC_OF_MIN));
				
			  a1 = rc.getNextCheckDate(startdate, target_DAY, target_HOUR_OF_DAY, target_MIN_OF_HOUR, RollCalendar.TYPE_SEC_OF_MIN).getTime()-now.getTime();
				a1 = a1<=0?0:a1;
			  System.out.println(a1/1000+"秒后触发");
			  
			  try {
						Thread.sleep(a1);
						startdate = new Date();
						started();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 break;
			 
		 }
			
//			}};
//		 t.start();
//		 *RollCalendar rc = new RollCalendar();
//		 *System.out.println(rc.getNextCheckDate(new Date(), 28, 0, 0, 8));
//		
		return true;
	}

	public void starting()
	{
		
	}

	public void started() {
		// TODO Auto-generated method stub
		
	}


	public boolean isstart() {
		// 
		return isstart;
	}


	public boolean iscanceled() {
		// TODO Auto-generated method stub
		return iscancel;
	}
//	public void setstarttime(long starttime)
//	{
//		startdatetime = starttime;
//	}
//
//
//	public long getStartdatetime() {
//		// TODO Auto-generated method stub
//		return startdatetime;
//	}

//
//	public long getPeriod() {
//		// TODO Auto-generated method stub
//		return period;
//	}

	
	public long getEnddatetime() {
		// TODO Auto-generated method stub
		return enddatetime;
	}


	public long getExetimes() {
		// TODO Auto-generated method stub
		return times;
	}

	public void setIscancel(boolean iscancel) {
		this.iscancel = iscancel;
	}
//
//	public void setStartdatetime(long startdatetime) {
//		this.startdatetime = startdatetime;
//	}
//
//	public void setPeriod(long period) {
//		this.period = period;
//	}

	public void setTimes(int times) {
		this.times = times;
	}
	
public boolean isIscancel() {
		return iscancel;
	}


	public void setEnddatetime(long enddatetime) {
	this.enddatetime = enddatetime;
}


	public Date getStartdate() {
		return startdate;
	}


	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}


	//3233666  02095105369   85515469
	@Override
	public void run() {
		
//		if(1==1)
//		while(true)
//		{
//			start();
//		System.out.println("run..");
//		}
		while(true)
		{
		//synchronized (isrunning)
		{
			
		
		//种种原因，已经消亡的任务
		if(iscanceled())
		{
			this.cancel();
			return;
			
		}
		//运行次数已经够了
		if(times==0)
		{
			
			setIscancel(true);
			this.cancel();
			return;
			
		}
//		//还不能开始
//		if(getStartdatetime()!=0 && getStartdatetime() >System.currentTimeMillis())
//		{
//			
//			return;
//		}
		//结束时间已经到了
		if(getEnddatetime()!=0 && getEnddatetime()<System.currentTimeMillis())
		{
			setIscancel(true);
			this.cancel();
			return;
		}
			
		//判断不能开始 zgw 此处没用
		if(!beforestart())
			return ;
		isstart = true;
		start();
		
		
		if(times!=-1)
			times--;
		}
		}
	//	return ;
	
		
	}
    public boolean cancel() {
    
       System.out.print("被取消");
       return    super.cancel();
    }

	public int getTarget_MIN_OF_HOUR() {
		return target_MIN_OF_HOUR;
	}


	public void setTarget_MIN_OF_HOUR(int target_MIN_OF_HOUR) {
		this.target_MIN_OF_HOUR = target_MIN_OF_HOUR;
	}


	public int getTarget_HOUR_OF_DAY() {
		return target_HOUR_OF_DAY;
	}


	public void setTarget_HOUR_OF_DAY(int target_HOUR_OF_DAY) {
		this.target_HOUR_OF_DAY = target_HOUR_OF_DAY;
	}


	public int getTarget_DAY() {
		return target_DAY;
	}


	public void setTarget_DAY(int target_DAY) {
		this.target_DAY = target_DAY;
	}


	public int getMode() {
		return mode;
	}


	public void setMode(int mode) {
		this.mode = mode;
	}

}
