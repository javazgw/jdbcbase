package com.base.myproject.server.cache;

import java.util.Date;


public abstract class HTcacheItem  {

	//添加日期
	Date adddate = null;
	//缓存对象
	Object cacheitem;
	//每个缓存的名称，放入到@HTCache里名称不能重复，否则会被冲掉
	String id = "";
	String cronexpression;
	public HTcacheItem(String id,Object o)
	{
		this.id = id;
		this.cacheitem = o;
	}
	public Date getAdddate() {
		return adddate;
	}
	public void setAdddate(Date adddate) {
		this.adddate = adddate;
	}
	public Object getCacheitem() {
		return cacheitem;
	}
	public void setCacheitem(Object cacheitem) {
		this.cacheitem = cacheitem;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCronexpression() {
		return cronexpression;
	}
	public void setCronexpression(String cronexpression) {
		this.cronexpression = cronexpression;
	}

	abstract void exe();
	
	
}
