package tk.roccodev.zta.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import tk.roccodev.zta.ZTAMain;

public class Updater {

	
	private static String FETCH_URL = "https://roccodev.pw/blog/versioning/5zig-timv/";
		

	public static boolean checkForUpdates() throws Exception{
		URL url = new URL(FETCH_URL + "latest.txt");
		 URLConnection conn = url.openConnection();
	       conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36(KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36");
		Scanner sc = new Scanner(conn.getInputStream());
		boolean tr = sc.nextInt() > ZTAMain.getCustomVersioning();
		sc.close();
		conn.getInputStream().close();
		return tr;
	}
	
	public static boolean isVersionBlacklisted(int ver) throws IOException{
		URL url = new URL(FETCH_URL + "disabled.txt");
		
		 URLConnection conn = url.openConnection();
	       conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36(KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36");
	        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String strLine;

		
		while ((strLine = reader.readLine()) != null)   {
		int ver1 = 0;
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
