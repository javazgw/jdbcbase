/**
 * 
 */
package com.base.myproject.server.timetask;

import java.util.Vector;

/**
 * @author Administrator
 *
 */
//用quartz
@Deprecated
public class Taskgroups {

	Vector< TaskItem> v = new Vector<TaskItem>();
	static Taskgroups tg = null;
	
	private Taskgroups()
	{
		
	}
	static public Taskgroups getInstance()
	{
		if(tg==null)
		{
			tg = new Taskgroups();
		}
		return tg;
	}
	void addTaskItem(TaskItem ti)
	{
		v.add(ti);
	}
	void removeTaskItem(TaskItem ti)
	{
		v.remove(ti);
		
	}
	void removeAll()
	{
		v.clear();
	}
	Vector<TaskItem> getAllTaskItem()
	{
		return v;
	}
}
