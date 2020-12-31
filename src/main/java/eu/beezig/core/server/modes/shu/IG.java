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

package eu.beezig.core.server.modes.shu;

import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.listeners.SHUListener;
import eu.the5zig.mod.server.IPatternResult;

public class IG extends ShuffleMode {
    @Override
    public void shuffleWin() {
        addPoints(200);
    }

    @Override
    public String getIdentifier() {
        return "ig";
    }

    @Override
    public String getName() {
        return "Instagib";
    }

    @Override
    public void addKills(int kills) {
        super.addKills(kills);
        addPoints(kills * 3);
    }

    public static class IgListener extends SHUListener.ShuffleModeListener<IG> {
        @Override
        public Class<IG> getGameMode() {
            return IG.class;
        }

        @Override
        public boolean matchLobby(String s) {
            return "ig".equals(s);
        }

        @Override
        public void onMatch(IG gameMode, String key, IPatternResult match) {
            super.onMatch(gameMode, key, match);
            if("ig.kill".equals(key)) gameMode.addKills(1);
            else if("ig.streak".equals(key) && match.get(0).equals(ServerHive.current().getNick()))
                gameMode.addPoints(2 + Integer.parseInt(match.get(1), 10));
        }
    }
}
