package tk.roccodev.zta.utils;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
			icon.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					tray.remove(icon);
					
				}
			});
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
		Class disp = null;
		try {
			disp = Class.forName("org.lwjgl.opengl.Display");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			return (boolean)disp.getMethod("isActive").invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	
}
