/**
 * 
 */
package com.base.myproject.server.alert.alertobject;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.base.myproject.server.alert.busiAlert.BusiAction;
import com.base.myproject.server.alert.busiAlert.BusiContent;
import com.base.myproject.server.alert.busiAlert.BusiTarget;

/**
 * @author Administrator
 *
 */
public /*abstract*/ class AlertObject {
	 private Timer timer1 = null;   
	//提醒时间
	Date alertdate;
	//提醒时间延迟
	int delay;
	//完成
	public final static int COMPLATE = 0x01;
	//放弃
	public final static int DEMISSION = 0X02;
	AlertTarget at;
	AlertContent ac;
	AlertAction aa;
	public AlertObject(AlertTarget at,AlertContent ac,AlertAction aa)
	{
		this.at = at;
		this.ac = ac;
		this.aa = aa;
	}
	
	public Date getAlertdate() {
		return alertdate;
	}

	public void setAlertdate(Date alertdate) {
		this.alertdate = alertdate;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public AlertTarget getAt() {
		return at;
	}

	public void setAt(AlertTarget at) {
		this.at = at;
	}

	public AlertContent getAc() {
		return ac;
	}

	public void setAc(AlertContent ac) {
		this.ac = ac;
	}

	public AlertAction getAa() {
		return aa;
	}

	public void setAa(AlertAction aa) {
		this.aa = aa;
	}

	public static  void main(String[] argv)
	{
		 //final AlertObject ao = new AlertObject(new BusiTarget(),new BusiContent(),new BusiAction());
		 int limit = 10000;
		 final AlertObject[] ao_array=new  AlertObject[limit];
		 for(int  i = 0;i<limit;i++)
		 {
			 ao_array[i] = new AlertObject(new BusiTarget(),new BusiContent(),new BusiAction());
		 }
		 
		 Timer timer1 = new Timer();   
		 
		 TimerTask tt  = new TimerTask(){

		
			@Override
			public void run() {
				 int count = 0;
				 while(true)
				 {
				for(int i=0;i<ao_array.length;i++)
				{
				System.out.println(count++ +"-"+ ao_array[i].getAc());
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 }
			}
			 
		 };
		 timer1.schedule(tt, 100);
	}
}
