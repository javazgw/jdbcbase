/**
 * 
 */
package com.base.myproject.server.json;

/**
 * @author Administrator
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String str = "{data:[{commandname:ne,commandcode:123,continueorrollback:false},{code:12_1,commandname:ne,commandcode:123,continueorrollback:false}]}";
		
		try {
			JSONObject js = new JSONObject(str);
			System.out.println(js.get("data"));
			System.out.println(js.getJSONArray("data").get(0));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
