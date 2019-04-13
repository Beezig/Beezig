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

package eu.beezig.core.briefing;

import eu.beezig.core.Log;
import eu.beezig.core.briefing.lergin.NewMap;
import eu.beezig.core.briefing.lergin.StaffChangeType;
import eu.beezig.core.briefing.lergin.StaffUpdate;
import eu.beezig.core.settings.Setting;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NewsServer {

    public static void serveNews(ArrayList<News> news, ArrayList<NewMap> maps, ArrayList<StaffUpdate> staff) {
        if (!Setting.BRIEFING.getValue()) {

            System.out.println("Briefing is disabled.");

            return;
        }


        if (Pools.error && news.size() == 0 && !Setting.IGNORE_WARNINGS.getValue()) {
            The5zigAPI.getAPI().messagePlayer(Log.error + "An error has occurred while attempting to load your Briefing. This may be caused by Minecraft using the wrong Java installation. Please follow this guide: https://github.com/RoccoDev/Beezig/wiki/Fixing-the-Issue-with-WR-for-Deathrun-&-Briefing");
            The5zigAPI.getAPI().messagePlayer(Log.info + "To suppress this warning, run §b/settings ignore_warnings true");
            return;
        } else if (Pools.error) {
            The5zigAPI.getAPI().messagePlayer(Log.error + "An error has occurred while attempting to load your Briefing. This may be caused by Minecraft using the wrong Java installation. Please follow this guide: https://github.com/RoccoDev/Beezig/wiki/Fixing-the-Issue-with-WR-for-Deathrun-&-Briefing");
            The5zigAPI.getAPI().messagePlayer(Log.info + "However, we're still able to serve you our latest news!");

        }

        if (news.size() == 0 && maps.size() == 0 && staff.size() == 0)
            return;


        The5zigAPI.getAPI().messagePlayer(
                "§f                     §b§m                  §f §f§lBeezig Briefing§f §b§m                  "
                        + (news.size() == 0 ? "" : "- " + "\n\n§f - " + ChatColor.ITALIC + "Our news:"));
        if (news.size() != 0) {
            for (News n : news) {
                The5zigAPI.getAPI().messagePlayer("\n§e" + ChatColor.UNDERLINE + n.getTitle());
                The5zigAPI.getAPI().messagePlayer("§e" + n.getContent());

            }
        }
        if (maps.size() != 0) {

            The5zigAPI.getAPI().messagePlayer("\n - " + ChatColor.ITALIC + "New maps:");
            StringBuilder sb = new StringBuilder();
            HashMap<String, ArrayList<NewMap>> grouped = new HashMap<>();
            for (NewMap m : maps) {

                if (!grouped.containsKey(m.getGameMode())) {
                    grouped.put(m.getGameMode(), new ArrayList<>(Collections.singletonList(m)));
                } else {
                    ArrayList<NewMap> tmp = grouped.get(m.getGameMode());
                    tmp.add(m);
                    grouped.put(m.getGameMode(), tmp);
                }

            }
            for (Map.Entry<String, ArrayList<NewMap>> e : grouped.entrySet()) {
                sb.append("§a").append(e.getKey()).append(":§e ");
                for (NewMap m : e.getValue()) {
                    sb.append(m.getName()).append(", ");
                }
                sb.trimToSize();
                sb.deleteCharAt(sb.length() - 2);
                sb.deleteCharAt(sb.length() - 1);
                sb.append(".");
                sb.append("\n");
            }

            The5zigAPI.getAPI().messagePlayer("§e" + sb.toString().trim());

        }
        if (staff.size() != 0) {
            The5zigAPI.getAPI().messagePlayer("\n - " + ChatColor.ITALIC + "Staff changes:");
            StringBuilder sb = new StringBuilder();
            HashMap<StaffChangeType, ArrayList<StaffUpdate>> grouped = new HashMap<>();
            for (StaffUpdate s : staff) {

                if (!grouped.containsKey(s.getType())) {

                    grouped.put(s.getType(), new ArrayList<>(Collections.singletonList(s)));

                } else {
                    ArrayList<StaffUpdate> tmp = grouped.get(s.getType());
                    tmp.add(s);
                    grouped.put(s.getType(), tmp);

                }

            }

            for (Map.Entry<StaffChangeType, ArrayList<StaffUpdate>> e : grouped.entrySet()) {
                sb.append(e.getKey().getDisplay()).append(":§e ");
                for (StaffUpdate s : e.getValue()) {
                    sb.append(s.getStaffName()).append(", ");
                }
                sb.trimToSize();
                sb.deleteCharAt(sb.length() - 2);
                sb.deleteCharAt(sb.length() - 1);
                sb.append(".");
                sb.append("\n");
            }

            The5zigAPI.getAPI().messagePlayer("§e" + sb.toString().trim());

        }
        The5zigAPI.getAPI().messagePlayer(
                "\n§f     §b§m            §f §7Map & staff data gently provided by Lergin§f §b§m            ");
        The5zigAPI.getAPI().messagePlayer(
                "§f       §b§m                                  §f §7hive.lergin.de§f §b§m                                   ");
    }

}
