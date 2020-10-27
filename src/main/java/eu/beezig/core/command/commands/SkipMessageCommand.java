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

import com.mojang.authlib.GameProfile;
import eu.beezig.core.Beezig;
import eu.beezig.core.automessage.AutoMessageManager;
import eu.beezig.core.command.Command;
import eu.beezig.core.util.text.Message;

import java.util.ArrayList;
import java.util.List;

public class SkipMessageCommand implements Command {

    private final static String usage = "/skipmessage [module]";

    @Override
    public String getName() {
        return "skipmessage";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "/skipmessage", "/sm", "/skipautomessage" };
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length > 1) {
            sendUsage(usage);
            return true;
        }

        String module = "";
        if (args.length > 0)
            module = args[0].toLowerCase();
        switch (module) {
            case "autogg":
            case "gg":
                if (Beezig.get().getData().getAutoGGManager().skip())
                    Message.info(Beezig.api().translate("skipmessage.module_skipping_enabled", Beezig.api().translate("setting.autogg.name")));
                else
                    Message.info(Beezig.api().translate("skipmessage.module_skipping_disabled", Beezig.api().translate("setting.autogg.name")));
                break;
            case "autogl":
            case "gl":
                if (Beezig.get().getData().getAutoGLManager().skip())
                    Message.info(Beezig.api().translate("skipmessage.module_skipping_enabled", Beezig.api().translate("setting.autogl.name")));
                else
                    Message.info(Beezig.api().translate("skipmessage.module_skipping_disabled", Beezig.api().translate("setting.autogl.name")));
                break;
            case "autonewgame":
            case "newgame":
            case "ng":
                if (Beezig.get().getData().getAutoNewGameManager().skip())
                    Message.info(Beezig.api().translate("skipmessage.module_skipping_enabled", Beezig.api().translate("setting.autonewgame.name")));
                else
                    Message.info(Beezig.api().translate("skipmessage.module_skipping_disabled", Beezig.api().translate("setting.autonewgame.name")));
                break;
            case "":
                if (AutoMessageManager.skipAll())
                    Message.info(Beezig.api().translate("skipmessage.skipping_enabled"));
                else
                    Message.info(Beezig.api().translate("skipmessage.skipping_disabled"));
                break;
            default:
                Message.info(Beezig.api().translate("skipmessage.module_not_found"));
                sendUsage(usage);
        }

        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(GameProfile sender, String[] args) {
        List<String> ret = new ArrayList<>();
        ret.add("gg");
        ret.add("gl");
        ret.add("newgame");
        return ret;
    }
}
