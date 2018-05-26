package tk.roccodev.zta.updater;

import tk.roccodev.zta.Log;
import tk.roccodev.zta.ZTAMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Updater {

	
	private static String FETCH_URL = "https://roccodev.pw/blog/versioning/5zig-timv/";
		

	public static boolean checkForUpdates() throws Exception{
		URL url = new URL(FETCH_URL + "latest.txt");
		 URLConnection conn = url.openConnection();
	       conn.addRequestProperty("User-Agent", Log.getUserAgent());
		Scanner sc = new Scanner(conn.getInputStream());
		boolean tr = sc.nextInt() > ZTAMain.getCustomVersioning();
		sc.close();
		conn.getInputStream().close();
		return tr;
	}
	
	public static boolean isVersionBlacklisted(int ver) throws IOException{
		URL url = new URL(FETCH_URL + "disabled.txt");
		
		 URLConnection conn = url.openConnection();
	       conn.addRequestProperty("User-Agent", Log.getUserAgent());
	        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String strLine;

		
		while ((strLine = reader.readLine()) != null)   {
		int ver1;
		try{
			ver1 = Integer.parseInt(strLine);
		}catch(Exception e){
			return false;
		}
			if(ver <= ver1) return true;
			
		 
		}

		
		reader.close();
		conn.getInputStream().close();
		return false;
	}
	

}
