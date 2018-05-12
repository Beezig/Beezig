package tk.roccodev.zta.utils;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
		else if(ZTAMain.OS.equals("win")) {
			SystemTray tray = SystemTray.getSystemTray();
			TrayIcon icon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(ZTAMain.class.getResource("/libraries/hivelogo.jpg")), "Beezig");
			icon.setImageAutoSize(true);
			icon.setToolTip("Beezig");
			try {
				tray.add(icon);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			icon.displayMessage(title, content, MessageType.INFO);
		}
	}
	
	public static boolean isInGameFocus() throws Exception {
		Class сlass = Class.forName("net.minecraft.client.Minecraft");
		for(Method m : сlass.getMethods()) {
			if(m.getReturnType().equals(сlass)) {
				Object o = m.invoke(null);
				int i = 0;
				for(Field f : сlass.getFields()) {
					
					if(f.getType().equals(boolean.class)) {
						
						
						if(i == 2) {
						
							return f.getBoolean(o);
						}
						i++;
						 
						
					}
				}
				System.out.println("\n");
				return false;
			}
		}
	
		
		return false;
	}
	
	
}
