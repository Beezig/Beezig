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

package eu.beezig.core.command;

import com.mojang.authlib.GameProfile;
import eu.beezig.core.BeezigMain;
import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.utils.TabCompletionUtils;
import eu.beezig.core.utils.acr.ChatReason;
import eu.the5zig.mod.The5zigAPI;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChatReportCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "cr";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/chatreport", "/reportchat"};
    }


    @Override
    public boolean execute(String[] args) {

        if (!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
        BeezigMain.crInteractive = false;
        BeezigMain.lrRS = "";
        BeezigMain.checkForNewLR = false;
        BeezigMain.lrID = "";
        if (args.length >= 2) {
            BeezigMain.lrPL = args[0];

            String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replace(" ", "");
            if (ChatReason.is(reason.toUpperCase())) {
                BeezigMain.crInteractive = true;
                BeezigMain.lrRS = reason;
                The5zigAPI.getAPI().sendPlayerMessage("/chatreport " + args[0]);
                return true;
            } else {
                The5zigAPI.getAPI().messagePlayer(Log.error + "Invalid reason. Available reasons: " + String.join(", ", Stream.of(ChatReason.values()).map(ChatReason::toString).collect(Collectors.toList())));
            }
        }
        return false;


    }

    @Override
    public List<String> addTabCompletionOptions(GameProfile sender, String[] args) {
        if (args.length == 2)
            return TabCompletionUtils.matching(args, Arrays.stream(ChatReason.values()).map(ChatReason::toString).collect(Collectors.toList()));
        return null;
    }
}
