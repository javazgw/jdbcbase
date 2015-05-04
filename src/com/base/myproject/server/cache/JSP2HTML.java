/**
 * 
 */
package com.base.myproject.server.cache;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * 将jsp 转换成为html
 * 
 * @author Administrator
 * 
 */
public class JSP2HTML {
	static JSP2HTML j2h;

	private JSP2HTML() {

	}

	public static JSP2HTML getInstance() {
		if (j2h == null)
			j2h = new JSP2HTML();
		return j2h;
	}

	/**
	 * 
	 * @param urlstr
	 *            url 如果是本系统，那么容器要打开状态
	 * @param tofile
	 *            执行到目标文件
	 * @throws IOException
	 */

	public void Transform(String urlstr, String tofile) throws IOException {
		URL url = new URL(urlstr);
		URLConnection connection = url.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
		httpURLConnection.setDoOutput(true);
		httpURLConnection.connect();
		OutputStream os = httpURLConnection.getOutputStream();
		

		BufferedReader in = new BufferedReader(new InputStreamReader(
				httpURLConnection.getInputStream()));
		String inputLine;
		FileWriter fw = new FileWriter(tofile);
		while ((inputLine = in.readLine()) != null) {
			// System.out.println(inputLine);
			fw.write(inputLine);
			fw.write("\n");
		}
		fw.write("<!-- zgw " + new Date() + "-->");
		in.close();

		fw.flush();
		fw.close();
		os.close();
	}

	public static void main(String[] argc) {
		try {
			JSP2HTML.getInstance()
					.Transform(
							"http://121.12.175.33:8080/yuqing2/busijsp/publicopinion/Analysis.jsp",
							"./aa4.html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
