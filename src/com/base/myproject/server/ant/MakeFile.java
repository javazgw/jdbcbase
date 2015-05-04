/**
 * 
 */
package com.base.myproject.server.ant;

import com.base.myproject.server.file.Tools;

/**
 * @author Administrator
 *
 */
public class MakeFile {
	public static void main(String[] argv)
	{
		
		 Tools.createfile(argv[0],argv[1]);
	}
}
