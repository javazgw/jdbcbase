package com.base.myproject.server.database;

import java.util.Vector;

public final  class DataVector {
	static Vector v = new Vector();
	private  DataVector()
	{
		
	}
	public static Vector getVector()
	{
		return v;
	}

}
