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
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.modes.BED;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.IPatternResult;
import eu.the5zig.util.minecraft.ChatColor;

public class BEDListener extends AbstractGameListener<BED> {
    private String lobby;

    @Override
    public Class<BED> getGameMode() {
        return BED.class;
    }

    @Override
    public boolean matchLobby(String s) {
        if(s.matches("bed[dtx]?")) {
            lobby = s;
            return true;
        }
        return false;
    }

    @Override
    public void onGameModeJoin(BED gameMode) {
        gameMode.setModeFromLobby(lobby);
    }

    @Override
    public void onMatch(BED gameMode, String key, IPatternResult match) {
        if("bed.points".equals(key)) {
            int points = Integer.parseInt(match.get(0), 10);
            gameMode.addPoints(points);
            if(match.size() > 1) {
                if(match.get(1).startsWith("Destroying")) {
                    gameMode.setBedsDestroyed(gameMode.getBedsDestroyed() + 1);
                }
            }
        }
        else if(key.startsWith("bed.kill") && match.get(0).equals(((ServerHive) Beezig.api().getActiveServer()).getNick())) {
            gameMode.addKills(1);
            gameMode.addPoints(key.equals("bed.kill.final") ? 10 : 5);
        }
        else if("bed.win".equals(key)) gameMode.won();
        else if("bed.summoner".equals(key)) gameMode.upgradeSummoner(match.get(0));
    }

    @Override
    public void onTitle(BED gameMode, String rawTitle, String rawSubTitle) {
        if(rawTitle == null) return;
        if(rawSubTitle == null) return;
        String title = ChatColor.stripColor(rawTitle);
        String sub = ChatColor.stripColor(rawSubTitle);
        if("➋".equals(title) && "Respawning in 2 seconds".equals(sub)) gameMode.addDeaths(1);
    }
}
