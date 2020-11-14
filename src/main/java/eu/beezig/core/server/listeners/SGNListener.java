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

import eu.beezig.core.server.modes.SGN;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.IPatternResult;

public class SGNListener extends AbstractGameListener<SGN> {
    private String lobby;

    @Override
    public Class<SGN> getGameMode() {
        return SGN.class;
    }

    @Override
    public boolean matchLobby(String s) {
        if(s.matches("sgd?")) {
            lobby = s;
            return true;
        }
        return false;
    }

    @Override
    public void onGameModeJoin(SGN gameMode) {
        gameMode.setModeFromLobby(lobby);
    }

    @Override
    public void onMatch(SGN gameMode, String key, IPatternResult match) {
        if("sgn.kill".equals(key)) gameMode.addKills(1);
        else if("sgn.gain".equals(key)) gameMode.addPoints(Integer.parseInt(match.get(0), 10));
        else if("sgn.loss".equals(key)) gameMode.addPoints(-Integer.parseInt(match.get(0), 10));
        else if("sgn.win".equals(key)) gameMode.won();
    }
}
