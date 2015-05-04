/**
 * 
 */
package com.base.myproject.server.groovy.samples;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;

/**
 * @author Administrator
 *
 */
public class Sample1 {

	public static void main(String[] argv) throws Exception
	{
		ClassLoader parent = ClassLoader.getSystemClassLoader();

		GroovyClassLoader loader = new GroovyClassLoader(parent);
		
		Class gclass = loader.parseClass("class GroovyCalculator {int add(int x, int y) { print \"我\";x +y +x*2 ;}}");

		GroovyObject groovyObject = (GroovyObject) gclass.newInstance();
		
		Object obj = groovyObject.invokeMethod("add", new Object[] {

		new Integer(2), new Integer(1) });

		System.out.println(obj);


	}
}
