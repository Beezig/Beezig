package tk.roccodev.beezig.utils;

import org.lwjgl.opengl.Display;
import tk.roccodev.beezig.BeezigMain;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class NotificationManager {

    public static void sendNotification(String title, String content) {
        switch (BeezigMain.OS) {
            case "unix":
                try {
                    String[] cmd = {"notify-send", title, content};
                    Runtime.getRuntime().exec(cmd);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case "mac":
                try {
                    String[] cmd = {"osascript", "-e", "display notification \"" + content + "\" with title \"" + title + "\""};
                    Runtime.getRuntime().exec(cmd);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case "win":
                SystemTray tray = SystemTray.getSystemTray();
                TrayIcon icon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(BeezigMain.class.getResource("/libraries/hivelogo.jpg")), "Beezig");
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
                break;
        }
    }

    public static boolean isInGameFocus() {
        return !Display.isActive();
    }


}
