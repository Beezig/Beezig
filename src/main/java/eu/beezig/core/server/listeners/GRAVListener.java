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

package eu.beezig.core.server.listeners;

import eu.beezig.core.Beezig;
import eu.beezig.core.autovote.AutovoteMap;
import eu.beezig.core.config.Settings;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.modes.GRAV;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;
import eu.the5zig.mod.event.ChatEvent;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;
import eu.the5zig.mod.util.component.MessageComponent;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GRAVListener extends AbstractGameListener<GRAV> {
    private static final Pattern MAP_REGEX = Pattern.compile("([^,&]+)");
    private static final Pattern MESSAGE_REGEX = Pattern.compile("▍ Gravity ▏ (\\d+)\\. .+ \\[(\\d+) Votes]");

    @Override
    public Class<GRAV> getGameMode() {
        return GRAV.class;
    }

    @Override
    public boolean matchLobby(String s) {
        return "grav".equals(s);
    }

    @Override
    public void onMatch(GRAV gameMode, String key, IPatternResult match) {
        if("grav.completion".equals(key)) gameMode.addCompletion(Integer.parseInt(match.get(0), 10));
        else if("grav.finish_other".equals(key)) gameMode.addCompletion(5);
        else if("grav.map".equals(key) || "grav.finish".equals(key)) {
            gameMode.setState(GameState.GAME);
            gameMode.stageDone();
        }
        if("grav.finish".equals(key)) gameMode.setWon();
    }

    @EventHandler
    public void onServerChat(ChatEvent event) {
        if(!(event.getChatComponent() instanceof MessageComponent)) return;
        if(!ServerHive.isCurrent()) return;
        GameMode mode;
        if(!Settings.AUTOVOTE.get().getBoolean() || !((mode = Beezig.api().getActiveServer().getGameListener().getCurrentGameMode()) instanceof GRAV)) return;
        GRAV grav = (GRAV) mode;
        if(grav.hasAutovoteRun()) return;
        String msg = ChatColor.stripColor(event.getMessage());
        Matcher master = MESSAGE_REGEX.matcher(msg);
        if(master.matches()) {
            int index = Integer.parseInt(master.group(1), 10);
            int votes = Integer.parseInt(master.group(2), 10);
            MessageComponent comp = (MessageComponent) event.getChatComponent();
            if(comp.getSiblings().size() > 0) {
                MessageComponent first = comp.getSiblings().get(0);
                if(first.getStyle().getOnHover() != null) {
                    String mapsMsg = ChatColor.stripColor(first.getStyle().getOnHover().getComponent().getText());
                    Matcher m = MAP_REGEX.matcher(mapsMsg);
                    while(m.find()) {
                        String name = StringUtils.normalizeMapName(m.group(0));
                        grav.getMaps().put(name, new AutovoteMap(name, index, votes));
                    }
                }
            }
            if(index == 5) {
                try {
                    grav.runAutovote();
                } catch (Exception e) {
                    Message.error(Message.translate("error.data_read"));
                    e.printStackTrace();
                }
            }
        }
    }
}
