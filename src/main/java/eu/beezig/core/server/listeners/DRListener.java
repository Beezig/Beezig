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
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.modes.DR;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DRListener extends AbstractGameListener<DR> {
    private static final Pattern POINTS_REGEX = Pattern.compile("Points: (\\d+)");
    private static final Pattern KILLS_REGEX = Pattern.compile("Kills: (\\d+)");

    @Override
    public Class<DR> getGameMode() {
        return DR.class;
    }

    @Override
    public boolean matchLobby(String s) {
        return "dr".equals(s);
    }

    @Override
    public void onGameModeJoin(DR gameMode) {
        gameMode.initMapData();
    }

    @Override
    public void onMatch(DR gameMode, String key, IPatternResult match) {
        if("dr.death".equals(key)) gameMode.addDeaths(1);
        else if("dr.checkpoint".equals(key)) {
            gameMode.addCheckpoint();
            ServerHive server = ServerHive.current();
            server.addTokens(Integer.parseInt(match.get(0), 10));
        }
        else if("dr.finish".equals(key)) {
            String nick = ServerHive.current().getNick();
            if(match.get(0).equals(nick)) {
                gameMode.setFinishTime(match.get(1));
                gameMode.calcTime();
            }
        }
        else if("dr.role".equals(key)) {
            gameMode.setState(GameState.GAME);
            gameMode.setRole(match.get(0).toLowerCase(Locale.ROOT));
        }
    }

    @Override
    public void onTitle(DR gameMode, String title, String subTitle) {
        if(subTitle == null) return;
        String stripped = ChatColor.stripColor(subTitle);
        if("The game has BEGUN!".equals(stripped)) gameMode.tryStart();
    }

    @Override
    public void onTick(DR gameMode) {
        if(gameMode.getCurrentRun() != null) gameMode.getCurrentRun().tick();
        Scoreboard sb = Beezig.api().getSideScoreboard();
        if(sb == null) return;
        for(Map.Entry<String, Integer> entry : sb.getLines().entrySet()) {
            if(entry.getValue() <= 6 && entry.getValue() >= 4) {
                String s = ChatColor.stripColor(entry.getKey());
                Matcher m = POINTS_REGEX.matcher(s);
                if(m.matches()) {
                    gameMode.tryUpdatePoints(m.group(1));
                }
                Matcher m2 = KILLS_REGEX.matcher(s);
                if(m2.matches()) {
                    gameMode.tryUpdateKills(m2.group(1));
                }
            }
        }
    }
}
