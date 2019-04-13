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

package eu.beezig.core.utils.autogg;

import eu.beezig.core.settings.Setting;
import eu.beezig.core.utils.rpc.DiscordUtils;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.event.ChatEvent;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.TitleEvent;

public class AutoGGListener {

    @EventHandler
    public void onTitle(TitleEvent evt) {
        if (evt.getTitle() != null && Setting.AUTOGG.getValue() && Triggers.enabled && Triggers.shouldGG(evt.getTitle(), 1)) {
            new Thread(() -> {
                try {
                    Thread.sleep(Triggers.delay);
                    if (Triggers.inParty) {
                        The5zigAPI.getAPI().sendPlayerMessage("/p");
                        The5zigAPI.getAPI().sendPlayerMessage(Triggers.ggText);
                        The5zigAPI.getAPI().sendPlayerMessage("/p");
                    } else The5zigAPI.getAPI().sendPlayerMessage(Triggers.ggText);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }).start();
        }
        if (evt.getSubTitle() != null && Setting.AUTOGG.getValue() && Triggers.enabled && Triggers.shouldGG(evt.getSubTitle(), 2)) {
            new Thread(() -> {
                try {
                    Thread.sleep(Triggers.delay);
                    if (Triggers.inParty) {
                        The5zigAPI.getAPI().sendPlayerMessage("/p");
                        The5zigAPI.getAPI().sendPlayerMessage(Triggers.ggText);
                        The5zigAPI.getAPI().sendPlayerMessage("/p");
                    } else The5zigAPI.getAPI().sendPlayerMessage(Triggers.ggText);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }).start();
        }
    }

    @EventHandler
    public void onChat(ChatEvent evt) {
        if (evt.getMessage().equals("§8▍ §b§lParty§8 ▏ §bParticipating in party chat? No.")) {
            Triggers.inParty = false;
        }
        if (evt.getMessage().equals("§f                       §a§lWelcome to your Party!")) {
            Triggers.lastPartyJoined = System.currentTimeMillis();
        }
        if (evt.getMessage().equals("§8▍ §b§lParty§8 ▏ §bParticipating in party chat? Yes.")) {
            Triggers.inParty = true;
        }
        if (evt.getMessage().startsWith("§8▍ §e§lHive§6§lMC§8 ▏§a §bDid this Party violate our Party Rules?")) {
            Triggers.lastPartyJoined = 0;
            Triggers.inParty = false;

            DiscordUtils.noParty();
        }
        if (evt.getMessage().startsWith("§8▍ §b§lParty§8 ▏ §aJoined")) {
            Triggers.lastPartyJoined = System.currentTimeMillis();
        }
        if (Setting.AUTOGG.getValue() && Triggers.enabled && Triggers.shouldGG(evt.getMessage(), 0)) {
            new Thread(() -> {
                try {
                    Thread.sleep(Triggers.delay);
                    if (Triggers.inParty) {
                        The5zigAPI.getAPI().sendPlayerMessage("/p");
                        The5zigAPI.getAPI().sendPlayerMessage(Triggers.ggText);
                        The5zigAPI.getAPI().sendPlayerMessage("/p");
                    } else The5zigAPI.getAPI().sendPlayerMessage(Triggers.ggText);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }).start();
        }
    }

}
