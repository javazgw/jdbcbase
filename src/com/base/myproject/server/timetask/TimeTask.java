/**
 * 
 */
package com.base.myproject.server.timetask;

import java.awt.Toolkit;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * @author Administrator
 *
 */
//用quartz
@Deprecated
public class TimeTask {

	Taskgroups tg ;

	
	public Taskgroups getTg() {
		return tg;
	}

	public void setTg(Taskgroups tg) {
		this.tg = tg;
	}

	public void start()
	{
		if(tg==null)
			return;
		
		Thread t = new Thread(){
			public void run()
			{
				
				for(  TaskItem sti : tg.getAllTaskItem())
				{
					
					sti.doit();
//					Thread t = new Thread(){
//						public void run()
//						{
				/*	 Timer timer = new Timer();
					 
					if(sti.getStartdate()==null)
					{
					
						timer.schedule(sti,0, sti.getPeriod());
						
							
					}
					else
					{
						
				
							timer.schedule(sti, sti.getStartdate(), sti.getPeriod());
							
					
					}*/
//						}
//					};
//					t.setPriority(Thread.MIN_PRIORITY);
//					t.start();
				
					
				}
		
	
			}};
			t.setPriority(Thread.MIN_PRIORITY);
			t.start();
	}
	

	public static void main(String[] argv)
	{
/*		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, 4);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		int day = c.get(Calendar.DAY_OF_YEAR);
		
		if(day < Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
			 
			  c.add(Calendar.WEEK_OF_YEAR, 1);
			} 
		
		
		System.out.println( c.getTime());
		System.out.println( Calendar.getInstance().getTime());
		
		
		RollCalendar rc = new RollCalendar();
		System.out.println(rc.getNextCheckDate(new Date(), 28, 0, 0, 8));
		
		if(1==1)
			return;*/
		TaskItem sti1 = new TaskItem(){
			public void started() {
				
				System.out.println("---sti1");
				
			}
		};
		sti1.setTimes(3);
		
		TaskItem sti2 = new TaskItem(){
			
			public void started() {
				
				System.out.println("---sti2");
				
			}
			
		};
		
		sti2.setTarget_DAY(1);
		sti2.setTarget_HOUR_OF_DAY(1);
		sti2.setTarget_MIN_OF_HOUR(15);
		sti2.setMode(sti2.HOUR_OF_DAY);
		sti2.setTimes(10);
		
		SimpleTaskItem sss= new SimpleTaskItem(new Date(),0,5);
		sss.setTarget_DAY(10);
		sss.setTarget_HOUR_OF_DAY(1);
		sss.setTarget_MIN_OF_HOUR(4);
		sss.setMode(sss.SECOND_OF_MIN);
		sss.setTimes(10);
		
		
	
		///sss.setEnddatetime(java.lang.System.currentTimeMillis()+10000);
//		Taskgroups.getInstance().addTaskItem(sti1);
		Taskgroups.getInstance().addTaskItem(sti2);
		Taskgroups.getInstance().addTaskItem(sss);
		
	
	
		

		TaskItem sti3 = new TaskItem(){
			
			public void started() {
				
		
				System.out.println("sti3....");
				try {
					Thread.sleep(-1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//throw new java.lang.RuntimeException("cheshi");
				
				
			
			}
			
		};
		sti3.setTimes(20);
		sti3.setTarget_DAY(10);
		sti3.setTarget_HOUR_OF_DAY(1);
		sti3.setTarget_MIN_OF_HOUR(4);
		sti3.setMode(sss.SECOND_OF_MIN);
	
		//sti3.setStartdatetime(System.currentTimeMillis()+1000*10);
		//sti3.setEnddatetime(System.currentTimeMillis()+1000*20);
		//sti3.setPeriod(1);
		Taskgroups.getInstance().addTaskItem(sti3);
		TimeTask tt = new TimeTask();
		tt.setTg(Taskgroups.getInstance());
		tt.start();
	
		
	}
}
