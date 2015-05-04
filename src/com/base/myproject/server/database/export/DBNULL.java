/**
 * 
 */
package com.base.myproject.server.database.export;

/**
 * @author Administrator
 *
 */
public class DBNULL implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 995788937005459096L;
	public static DBNULL dbnull= null;
	private DBNULL()
	{
		
	}
	public static DBNULL getInstance()
	{
		if(dbnull==null)
			dbnull = new DBNULL();
		return dbnull;
	}
	 public boolean equals(Object o)     
	 {
		 if(this==o)
			 return true;
		 if(o instanceof DBNULL)
			 return true;
		 
			 return false;
	 }
}
