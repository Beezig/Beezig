/*
 * Copyright (C) 2019 Beezig Team
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

import eu.beezig.core.server.modes.TIMV;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.IPatternResult;

public class TIMVListener extends AbstractGameListener<TIMV> {
    @Override
    public Class<TIMV> getGameMode() {
        return TIMV.class;
    }

    @Override
    public boolean matchLobby(String s) {
        return "timv".equals(s);
    }

    @Override
    public void onGameModeJoin(TIMV gameMode) {
        gameMode.initMapData();
    }

    @Override
    public void onMatch(TIMV gameMode, String key, IPatternResult match) {
        if("timv.points_gain".equals(key)) gameMode.addPoints(Integer.parseInt(match.get(0), 10));
        else if("timv.points_loss".equals(key)) gameMode.addPoints(-Integer.parseInt(match.get(0), 10));
        else if("timv.start".equals(key)) gameMode.setRole(match.get(0));
        else if("timv.discovery".equals(key)) {
            String role = match.get(0);
            if("a Traitor".equals(role)) gameMode.setTraitorsDiscovered(gameMode.getTraitorsDiscovered() + 1);
            else if("a Detective".equals(role)) gameMode.setDetectivesDiscovered(gameMode.getDetectivesDiscovered() + 1);
        }
        else if("timv.tdeath".equals(key)) gameMode.setDeadTraitors(gameMode.getDeadTraitors() + 1);
    }
}
