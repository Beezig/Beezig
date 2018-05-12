package tk.roccodev.zta.utils;

import tk.roccodev.zta.ZTAMain;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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
			icon.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					tray.remove(icon);
					
				}

				@Override
				public void mousePressed(MouseEvent e) {

				}

				@Override
				public void mouseReleased(MouseEvent e) {

				}

				@Override
				public void mouseEntered(MouseEvent e) {

				}

				@Override
				public void mouseExited(MouseEvent e) {

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
