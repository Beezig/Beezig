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
import eu.beezig.core.api.BeezigAPI;
import eu.beezig.core.autovote.AutovoteUtils;
import eu.beezig.core.utils.TabCompletionUtils;
import eu.the5zig.mod.The5zigAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoVoteCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "autovote";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/autovote", "/avote"};
    }

    @Override
    public boolean execute(String[] args) {

        if (!(The5zigAPI.getAPI().getActiveServer() instanceof IHive))
            return false;

        if (BeezigMain.hasExpansion && args.length == 0) {
            BeezigAPI.get().getListener().displayAutovoteGui();
            return true;
        }

        // Format would be /autovote add dr_throwback
        if (args.length == 2) {
            String mode = args[0];
            if (mode.equalsIgnoreCase("add")) {
                String map = args[1];
                String[] data = map.split("_");
                String gamemode = data[0]; // ex: dr
                List<String> mapStr = new ArrayList<>(Arrays.asList(data));
                mapStr.remove(0);

                StringBuilder sb = new StringBuilder();
                String mapString;
                for (String s : mapStr) {
                    sb.append(s).append("_");
                }
                mapString = sb.deleteCharAt(sb.length() - 1).toString().trim();

                List<String> maps = (List<String>) AutovoteUtils.get(gamemode.toLowerCase());
                maps.add(mapString.toUpperCase());
                AutovoteUtils.set(gamemode.toLowerCase(), maps);
                AutovoteUtils.dump();
                The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully added map.");

            } else if (mode.equalsIgnoreCase("list")) {
                String game = args[1];

                The5zigAPI.getAPI().messagePlayer(Log.info + "Maps for mode " + game.toUpperCase());
                for (String s : AutovoteUtils.getMapsForMode(game.toLowerCase())) {
                    The5zigAPI.getAPI().messagePlayer("§7 - §b" + s);
                }

            } else if (mode.equalsIgnoreCase("remove")) {
                String map = args[1];
                String[] data = map.split("_");
                String gamemode = data[0]; // ex: dr

                List<String> mapStr = new ArrayList<>(Arrays.asList(data));
                mapStr.remove(0);
                StringBuilder sb = new StringBuilder();
                String mapString;
                for (String s : mapStr) {
                    sb.append(s).append("_");
                }
                mapString = sb.deleteCharAt(sb.length() - 1).toString().trim();

                List<String> maps = AutovoteUtils.getMapsForMode(gamemode.toLowerCase());
                maps.remove(mapString.trim().toUpperCase());
                AutovoteUtils.set(gamemode.toLowerCase(), maps);
                AutovoteUtils.dump();
                The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully removed map.");

            }

        } else if (args.length == 3 && args[0].equalsIgnoreCase("place")) {
            //e.g, /autovote place timv_azure_island 1
            String map = args[1];
            String[] data = map.split("_");
            String gamemode = data[0]; // ex: dr
            List<String> mapStr = new ArrayList<>(Arrays.asList(data));
            mapStr.remove(0);
            StringBuilder sb = new StringBuilder();
            String mapString;
            for (String s : mapStr) {
                sb.append(s).append("_");
            }
            mapString = sb.deleteCharAt(sb.length() - 1).toString().trim();
            ArrayList<String> maps = new ArrayList<>(AutovoteUtils.getMapsForMode(gamemode.toLowerCase()));
            int index = Integer.parseInt(args[2]);
            String tmp = maps.get(index - 1);
            int tmpIndex = maps.indexOf(mapString.trim().toUpperCase());
            if (tmpIndex == -1) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "Map not found. Perhaps try adding it first?");
                return true;
            }
            System.out.println(tmp + " / " + tmpIndex);
            System.out.println(index);
            maps.set(index - 1, mapString.trim().toUpperCase());
            maps.set(tmpIndex, tmp);

            AutovoteUtils.set(gamemode.toLowerCase(), maps);
            AutovoteUtils.dump();
            The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully moved map.");

        } else {
            The5zigAPI.getAPI()
                    .messagePlayer(Log.info + "Usage:" + "\n"
                            + "§b/autovote add mode_map §7- §3Adds a favorite map to a gamemode" + "\n"
                            + "§b/autovote list mode §7- §3Lists your favorite maps for that gamemode" + "\n"
                            + "§b/autovote remove mode_map §7- §3Removes a favorite map from a gamemode\n" +
                            "§b/autovote place mode_map place§7- §3Puts the given map in [place] place.");
        }

        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(GameProfile sender, String[] args) {
        if (args.length == 1)
            return TabCompletionUtils.matching(args, Arrays.asList("add", "list", "remove", "place"));
        return new ArrayList<>();
    }
}
