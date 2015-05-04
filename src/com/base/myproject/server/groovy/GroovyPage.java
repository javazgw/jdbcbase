package com.base.myproject.server.groovy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import groovy.lang.GroovyObject;

public class GroovyPage {
	
	GroovyObject pageObject;
	
	public GroovyPage(String url) {
		pageObject = GroovyEngine.getInstance().load(url).newInstance();
	}
	
	public void doRequest(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("gbk");
		pageObject.invokeMethod("request", new Object[]{request, response});
	}
}
