/*
 * Copyright (C) 2017-2021 Beezig Team
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
import eu.beezig.core.config.Settings;
import eu.beezig.core.net.profile.ProfileBadgeListener;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.modes.HIDE;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.task.WorldTask;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HIDEListener extends AbstractGameListener<HIDE> {
    private static final Pattern LIST_REGEX = Pattern.compile("§8▍ (.+) ▏ §3Online participants \\(.+\\): (.+)");
    private static final Pattern POINTS_REGEX = Pattern.compile("(\\d+) Points");

    @Override
    public Class<HIDE> getGameMode() {
        return HIDE.class;
    }

    @Override
    public boolean matchLobby(String s) {
        return "hide".equals(s);
    }

    @Override
    public void onMatch(HIDE gameMode, String key, IPatternResult match) {
        if("hide.kill".equals(key)) {
            gameMode.addKills(1);
            ServerHive server = ServerHive.current();
            server.addTokens(Integer.parseInt(match.get(1), 10));
        }
        else if("hide.win".equals(key)) {
            gameMode.won();
            gameMode.addPoints(Integer.parseInt(match.get(0), 10));
        }
    }

    @Override
    public void onTick(HIDE gameMode) {
        Scoreboard sb = Beezig.api().getSideScoreboard();
        if(sb == null) return;
        for(Map.Entry<String, Integer> entry : sb.getLines().entrySet()) {
            if(entry.getValue() == 5 || entry.getValue() == 4) {
                Matcher m = POINTS_REGEX.matcher(ChatColor.stripColor(entry.getKey()));
                if(m.matches()) {
                    gameMode.tryUpdatePoints(m.group(1));
                }
            }
        }
    }

    @Override
    public boolean onServerChat(HIDE gameMode, String message) {
        if(gameMode.getState() == GameState.GAME && Settings.HIDE_HIDERS.get().getBoolean()) {
            Matcher m = LIST_REGEX.matcher(message);
            boolean color = Settings.HIVE_RANK.get().getBoolean();
            if (m.matches()) {
                String prefix = m.group(1);
                List<String> players = ProfileBadgeListener.COMMA.splitToList(m.group(2));
                List<String> hiders = new ArrayList<>();
                List<String> current = Beezig.api().getServerPlayers().stream().map(profile -> profile.getGameProfile().getName()).collect(Collectors.toList());
                for (String s : players) {
                    String player = s.replace(".", "").trim();
                    if (!current.contains(ChatColor.stripColor(player))) hiders.add(color ? player : ChatColor.stripColor(player));
                }
                if (!hiders.isEmpty()) {
                    String send = "§8▍ " + prefix + Color.accent() + " (Beezig)§8 ▏ " + Color.primary() + Beezig.api().translate("msg.hide.hiders", Color.accent() + hiders.size() + Color.primary(),
                        Color.accent() + String.join(Color.primary() + ", " + Color.accent(), hiders) + ".");
                    WorldTask.submit(() -> Beezig.api().messagePlayer(send));
                }
            }
        }
        return super.onServerChat(gameMode, message);
    }
}
