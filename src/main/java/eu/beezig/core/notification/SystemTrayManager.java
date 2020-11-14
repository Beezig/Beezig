/*
 * Copyright (C) 2017-2020 Beezig Team
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

package eu.beezig.core.notification;

import eu.beezig.core.Beezig;
import eu.beezig.core.config.Settings;
import eu.beezig.core.util.ExceptionHandler;
import org.apache.commons.lang3.SystemUtils;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SystemTrayManager {
    private TrayIcon mainIcon;
    private boolean supported;

    public SystemTrayManager() {
        if(SystemUtils.IS_OS_WINDOWS && Settings.MSG_PING.get().getBoolean()) {
            try {
                SystemTray tray = SystemTray.getSystemTray();
                mainIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(Beezig.class.getResource("/img/logo.png")), "Beezig");
                mainIcon.setImageAutoSize(true);
                mainIcon.setToolTip("Beezig");
                mainIcon.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        tray.remove(mainIcon);
                    }
                });
                tray.add(mainIcon);
                supported = true;
            } catch (UnsupportedOperationException ignored) {
                Beezig.logger.warn("System tray is NOT supported.");
            } catch (AWTException e) {
                ExceptionHandler.catchException(e);
            }
        }
    }

    public void sendNotification(IncomingMessage message) {
        Beezig.api().playSound("note.pling", 1f);
        String title = Beezig.api().translate(message.getType() == NotificationManager.MessageType.BROADCAST
            ? "msg.notify.incoming.broadcast" : "msg.notify.incoming", message.getSender());
        String content = message.getMessage();
        try {
            if (SystemUtils.IS_OS_UNIX) {
                Runtime.getRuntime().exec(new String[]{"notify-send", title, content});
            } else if (SystemUtils.IS_OS_MAC) {
                Runtime.getRuntime().exec(new String[]{"osascript", "-e", "display notification \"" + content + "\" with title \"" + title + "\""});
            } else if (supported) {
                mainIcon.displayMessage(title, content, TrayIcon.MessageType.INFO);
            }
        } catch (Exception ex) {
            ExceptionHandler.catchException(ex, "Notify send");
        }
    }
}
