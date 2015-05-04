/**
 * 
 */
package com.base.myproject.server.groovy.samples;

import groovy.lang.GroovyObject;

import java.util.LinkedHashMap;

import com.base.myproject.server.groovy.GroovyEngine;

/**
 * @author Administrator
 *
 */
public class Sample2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		GroovyObject groovyObject = GroovyEngine.getInstance().loadstr("class GroovyCalculator {def x,y;def hm =[name:\"Andy\", \"VPN-#\":45]; def add() { print \"我\";x=2*x;hm}}").newInstance();
		
		groovyObject.setProperty("x", 22);
		groovyObject.setProperty("y", 33);
		LinkedHashMap lhm = new LinkedHashMap();
		lhm.put("name", "zgw");
		groovyObject.setProperty("hm",lhm);
		Object obj = groovyObject.invokeMethod("add", new Object[] {

				});

				System.out.println(((LinkedHashMap)obj).get("name"));

	
	}

}
