package com.base.myproject.server.ant;

import com.base.myproject.server.file.Tools;

public class FileExists {

	public static void main(String[] argv)
	{
		if( Tools.isExistsDir(argv[0]))
		{
			throw new java.lang.RuntimeException("文件存在,不能被覆盖，请检查");
		}
	}
}
