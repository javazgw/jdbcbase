/**
 * 
 */
package com.base.myproject.server.alert.alertobject;

/**
 * @author Administrator
 *
 *提醒的对象，名称，id，组别
 */
public interface AlertTarget {
	public String getName();
	public String getID();
	public String getGroup();
}
