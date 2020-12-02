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
import eu.the5zig.mod.util.component.MessageComponent;
import eu.the5zig.mod.util.component.style.MessageAction;

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
            MessageComponent base = new MessageComponent(Message.infoPrefix() + Message.translate("msg.obs.install.os") + " ");
            TextButton btn = new TextButton("btn.obs.install.guide.name", "btn.obs.install.guide.desc", "§e");
            btn.getStyle().setOnClick(new MessageAction(MessageAction.Action.OPEN_URL, "https://go.beezig.eu/obs-controller"));
            base.getSiblings().add(btn);
            Beezig.api().messagePlayerComponent(base, false);
        }
        return true;
    }
}
