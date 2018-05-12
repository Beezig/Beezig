package tk.roccodev.zta.utils;

import java.io.IOException;

import tk.roccodev.zta.ZTAMain;

public class NotificationManager {

	public static void sendNotification(String title, String content) {
		if(ZTAMain.OS.equals("unix")) {
			try {
				String[] cmd = {"notify-send", title, content};
			Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(ZTAMain.OS.equals("mac")) {
			try {
				String[] cmd = {"osascript", "-e", "display notification \"" + content + "\" with title \"" + title +"\""};
			Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
