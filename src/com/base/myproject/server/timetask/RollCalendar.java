/**
 * 
 */
package com.base.myproject.server.timetask;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * zgw 2010-03-29
 * @author Administrator
 *RollCalendar rc = new RollCalendar();
 *System.out.println(rc.getNextCheckDate(new Date(), 28, 0, 0, 8));
 *去某个时间后的另一个时间，譬如今天是周二，未来最近的周一的时间。
 *同理 目前可以取得 每小时的分钟，每天的小时，每周的某天，每月的某天
 *		
 */
//用quartz
@Deprecated
public class RollCalendar extends GregorianCalendar{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6664730486279599277L;
	public static final int MIN_OF_HOUR =1;
	public static final int TYPE_HOUR_OF_DAY= 2;
	public static final int TYPE_DAY_OF_WEEK = 4;
	public static final int TYPE_DAY_OF_MONTH = 8;
	public static final int TYPE_SEC_OF_MIN= 16;
	
	
	
	RollCalendar() {
	    super();
	   // this.set(HOUR_OF_DAY, 24);
	    setFirstDayOfWeek(MONDAY);
	  }  
	
	 public Date getNextCheckDate(Date now,int d,int hour,int minute,int type) {
		 this.setTime(now);
		 Calendar c = Calendar.getInstance();
		 c.setTime(now);
		 
		 switch(type) {
		 
		 case TYPE_SEC_OF_MIN://分钟的秒
			 	this.set(SECOND, d);
				//this.set(MILLISECOND, 0);
				//this.set(MINUTE, 0);
				
				long second = this.getTimeInMillis();
				if(second <= c.getTimeInMillis()) {
					 this.add(MINUTE, 1);
					 
					} 
				
				
			 break;
		 case MIN_OF_HOUR://每小时中的几分
			 	this.set(SECOND, 0);
				this.set(MILLISECOND, 0);
				this.set(MINUTE, minute);
				
				long hourt = this.getTimeInMillis();
				if(hourt < c.getTimeInMillis()) {
					 this.add(HOUR, 1);
					 
					} 
				
				
			 break;
		 case TYPE_HOUR_OF_DAY:  //每天中的几点
			 
			 	this.set(SECOND, 0);
				this.set(MILLISECOND, 0);
				
				this.set(MINUTE, minute);
				
				System.out.println(this.get(HOUR_OF_DAY));
				System.out.println(hour);
				System.out.println("-"+this.getTime());
				this.set(HOUR_OF_DAY, hour);
				
				System.out.println(this.getTime());
				//int day = this.get(HOUR_OF_DAY);
				long day = this.getTimeInMillis();
				if(day < c.getTimeInMillis()) {
					 this.add(DAY_OF_YEAR, 1);
					
					} 
				
			 break;
		 case TYPE_DAY_OF_WEEK: //一周中的某天 一周开始是周日，要改
			 this.set(SECOND, 0);
				this.set(MILLISECOND, 0);
				this.set(MINUTE, minute);
				this.set(HOUR_OF_DAY, hour);
				System.out.println("0:"+this.getTime());
				this.set(DAY_OF_WEEK, d);
				System.out.println("0:"+this.getTime());
				long day2 = this.getTimeInMillis();
				if(day2 < c.getTimeInMillis()) {
					 this.add(WEEK_OF_MONTH, 1);
					} 
			 break;
		 case TYPE_DAY_OF_MONTH: //一个月中的某天
			 this.set(SECOND, 0);
				this.set(MILLISECOND, 0);
				this.set(MINUTE, minute);
				this.set(HOUR_OF_DAY, hour);
				System.out.println("0:"+this.getTime());
				this.set(DAY_OF_MONTH, d);
				System.out.println("0:"+this.getTime());
				long day3 = this.getTimeInMillis();
				if(day3 < c.getTimeInMillis()) {
					 this.add(MONTH, 1);
					} 
			 break;
			 

		 }
			return getTime();
	 }
}
