/**
 * 
 */
package com.base.myproject.server.database.export;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Administrator
 *
 */
public class ReverseThread extends Thread {
	  private PrintWriter out = null;
	  private BufferedReader in = null;

	  public ReverseThread(PrintWriter out, BufferedReader in) {
	    this.out = out;
	    this.in = in;
	  }

	  public void run() {
	    if (out != null && in != null) {
	      try {
	        String input;
	        while ((input = in.readLine()) != null) {
	          out.println(reverseIt(input));
	          out.flush();
	        }
	        out.close();
	      } catch (IOException e) {
	        System.err.println("ReverseThread run: " + e);
	      }
	    }
	  }

	  private String reverseIt(String source) {
	    int i, len = source.length();
	    StringBuffer dest = new StringBuffer(len);

	    for (i = (len - 1); i >= 0; i--)
	      dest.append(source.charAt(i));
	    return dest.toString();
	  }
	}

