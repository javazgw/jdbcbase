package com.base.myproject.server.groovy;


import groovy.lang.GroovyObject;

public class GroovyClass {
	private Class<?> clazz;
	
	GroovyClass(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public GroovyObject newInstance() {
		try {
			return (GroovyObject) clazz.newInstance();
		} catch (Exception e) {
			//LogFactory.getLog(GroovyClass.class).error(e.getMessage(), e);
			throw new RuntimeException(e);			
		}
	}
}
