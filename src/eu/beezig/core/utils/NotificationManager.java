/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
 *
 * This file is part of Beezig.
 *
 * Beezig is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beezig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beezig.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.beezig.core.utils;

import eu.beezig.core.BeezigMain;
import org.lwjgl.opengl.Display;

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
                String name = BeezigMain.laby ? "hivelogo.jpg" : "/libraries/hivelogo.jpg";
                TrayIcon icon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(BeezigMain.class.getResource(name)), "Beezig");
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
