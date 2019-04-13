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

package eu.beezig.core.utils.rpc;

import eu.beezig.core.BeezigMain;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.event.ChatEvent;
import eu.the5zig.mod.event.EventHandler;

public class DiscordParty {
    private int members = 1, maxMembers;
    private String id;

    public DiscordParty(int maxMembers, String id) {
        this.maxMembers = maxMembers;
        this.id = id;
        The5zigAPI.getAPI().getPluginManager().registerListener(BeezigMain.instance, this);
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public int getMembers() {
        return members;
    }

    public String getId() {
        return id;
    }

    @EventHandler
    public void onMessage(ChatEvent evt) {
        if(evt.getMessage().startsWith("§8▍ §b§lParty§8 ▏ §f§lMembers §7")) {
            String num = evt.getMessage().replace("§8▍ §b§lParty§8 ▏ §f§lMembers §7", "")
                    .substring(1).replace(")", "");
            members = Integer.parseInt(num);
        }
        DiscordUtils.reloadPresence();
    }

    public void unregister() {
        The5zigAPI.getAPI().getPluginManager().unregisterListener(this);
    }
}
