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

package eu.beezig.core.command.commands;

import eu.beezig.core.Beezig;
import eu.beezig.core.command.Command;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.obs.ObsFileOperations;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.TextButton;
import eu.beezig.hiveapi.wrapper.utils.download.Downloader;
import eu.beezig.hiveapi.wrapper.utils.json.JArray;
import eu.the5zig.mod.util.component.MessageComponent;
import eu.the5zig.mod.util.component.style.MessageAction;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class ObsCommand implements Command {
    @Override
    public String getName() {
        return "obs";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/bobs"};
    }

    @Override
    public boolean execute(String[] args) {
        if(!ServerHive.isCurrent()) return false;
        if(args.length == 0) {
            sendUsage("/bobs [connect/open/folder/delete/confirmdelete] (uuid)");
            return true;
        }
        String mode = args[0];
        if("connect".equalsIgnoreCase(mode)) {
            try {
                Beezig.get().getProcessManager().refreshObsAuth();
            } catch (Exception e) {
                ExceptionHandler.catchException(e);
                Message.error(Message.translate("error.obs"));
            }
        } else if("open".equalsIgnoreCase(mode)) {
            if(args.length < 2) {
                sendUsage("/bobs open [uuid]");
                return true;
            }
            UUID uuid = UUID.fromString(args[1]);
            ObsFileOperations.open(uuid);
        } else if("folder".equalsIgnoreCase(mode)) {
            if(args.length < 2) {
                sendUsage("/bobs folder [uuid]");
                return true;
            }
            UUID uuid = UUID.fromString(args[1]);
            ObsFileOperations.openFolder(uuid);
        } else if("delete".equalsIgnoreCase(mode)) {
            if(args.length < 2) {
                sendUsage("/bobs delete [uuid]");
                return true;
            }
            UUID uuid = UUID.fromString(args[1]);
            ObsFileOperations.delete(uuid);
        } else if("confirmdelete".equalsIgnoreCase(mode)) {
            if(args.length < 2) {
                sendUsage("/bobs confirmdelete [uuid]");
                return true;
            }
            UUID uuid = UUID.fromString(args[1]);
            ObsFileOperations.confirmDelete(uuid);
        } else if("install".equalsIgnoreCase(mode)) {
            int arch = "x86".equals(System.getProperty("os.arch")) ? 32 : 64;
            if(!SystemUtils.IS_OS_WINDOWS && !SystemUtils.IS_OS_MAC) {
                MessageComponent base = new MessageComponent(Message.errorPrefix() + Message.translate("error.obs.install.os") + " ");
                TextButton btn = new TextButton("btn.obs.install.guide.name", "btn.obs.install.guide.desc", "§e");
                btn.getStyle().setOnClick(new MessageAction(MessageAction.Action.OPEN_URL, "https://go.beezig.eu/obs-linux"));
                base.getSiblings().add(btn);
                Beezig.api().messagePlayerComponent(base, false);
                return true;
            }
            Message.info(Message.translate("msg.obs.installing"));
            try {
                Downloader.getJsonArray(new URL("https://gitlab.com/api/v4/projects/19200869/releases")).thenAcceptAsync(arr -> {
                    try {
                        if (arr.getInput().isEmpty()) {
                            Message.error(Message.translate("error.obs.install.version"));
                            return;
                        }
                        JArray links = arr.getJObject(0).getJObject("assets").getJArray("links");
                        if (links.getInput().isEmpty()) links = arr.getJObject(1).getJObject("assets").getJArray("links");
                        File pluginsDir = null;
                        String downloadUrl = null;
                        for (int i = 0; i < links.getInput().size(); i++) {
                            downloadUrl = links.getJObject(i).getString("direct_asset_url");
                            if (SystemUtils.IS_OS_MAC && downloadUrl.endsWith(".dylib")) {
                                pluginsDir = new File("/Applications/OBS.app/Contents/Plugins");
                                break;
                            } else if (SystemUtils.IS_OS_WINDOWS && downloadUrl.endsWith(".dll") && downloadUrl.contains(Integer.toString(arch, 10))) {
                                pluginsDir = new File(arch == 64 ? "C:/Program Files/obs-studio/obs-plugins/32bit" : "C:/Program Files/obs-studio/obs-plugins/64bit");
                                break;
                            }
                        }
                        if (pluginsDir == null || !pluginsDir.exists()) {
                            Message.error(Message.translate("error.obs.install.obs"));
                            return;
                        }
                        URL download = new URL(downloadUrl);
                        HttpURLConnection conn = (HttpURLConnection) download.openConnection();
                        conn.addRequestProperty("User-Agent", Message.getUserAgent());
                        try (FileOutputStream os = new FileOutputStream(new File(pluginsDir, FilenameUtils.getName(download.getPath())))) {
                            os.write(IOUtils.toByteArray(conn.getInputStream()));
                        }
                        Message.info(Message.translate("msg.obs.install.success"));
                    } catch (Exception ex) {
                        ExceptionHandler.catchException(ex);
                        Message.error(Message.translate("error.obs.install"));
                    }
                }).exceptionally(ex -> {
                    ExceptionHandler.catchException(ex);
                    Message.error(Message.translate("error.obs.install"));
                    return null;
                });
            } catch (MalformedURLException e) {
                ExceptionHandler.catchException(e);
            }
        }
        return true;
    }
}
