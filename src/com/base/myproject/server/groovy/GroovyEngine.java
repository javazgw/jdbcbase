package com.base.myproject.server.groovy;

import groovy.lang.GroovyClassLoader;

import java.io.InputStream;

import org.codehaus.groovy.control.CompilerConfiguration;

public class GroovyEngine {
	GroovyClassLoader classLoader;
	private static GroovyEngine instance;
	
	public static GroovyEngine getInstance() {
		if (instance == null) {
			instance = new GroovyEngine();
		}
		return instance;
	}
	
	private GroovyEngine() {
    	ClassLoader parent = getClass().getClassLoader();
    	CompilerConfiguration config = CompilerConfiguration.DEFAULT;
    	config.setSourceEncoding("utf-8");
    	classLoader = new GroovyClassLoader(parent);
	}
	
	public GroovyClass load(String path) {
		InputStream is = null;
		try {
			System.out.println(classLoader.getResource("."));
	    	is = classLoader.getResourceAsStream(path);
	    	return new GroovyClass(classLoader.parseClass(is));
		}
		finally {
			try {
				is.close();
			}
			catch (Exception e) {
				//LogFactory.getLog(GroovyEngine.class).error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
			
		}
    	
	}
	public GroovyClass loadstr(String str)
	{
		return new GroovyClass(classLoader.parseClass(str));
	}
}
