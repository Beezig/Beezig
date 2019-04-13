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
import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.hiveapi.wrapper.NetworkRank;
import eu.the5zig.mod.The5zigAPI;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;

import java.util.ArrayList;
import java.util.List;

public class MessageOverlayCommand implements Command {

    public static String toggledName = "";

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "msg";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/msg", "/tell"};
    }


    @Override
    public boolean execute(String[] args) {

        if (args.length == 0 && !toggledName.isEmpty()) {
            toggledName = "";
            The5zigAPI.getAPI().messagePlayer(Log.info + "Now sending messages to normal chat.");
            return true;
        }
        if (args.length != 1 || !(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;


        MessageOverlayCommand.toggledName = args[0];
        new Thread(() -> {
            HivePlayer api = new HivePlayer(toggledName);
            The5zigAPI.getAPI().messagePlayer(Log.info + "Now sending messages to " +
                    NetworkRank.fromDisplay(api.getRank().getHumanName()).getColor() + api.getUsername() + "§3.");
        }).start();


        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(GameProfile sender, String[] args) {
        return new ArrayList<>();
    }
}
