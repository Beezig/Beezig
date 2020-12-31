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

import eu.beezig.core.server.listeners.SHUListener;
import eu.the5zig.mod.server.IPatternResult;

public class SLAP extends ShuffleMode {
    @Override
    public void shuffleWin() {
        addPoints(200);
    }

    @Override
    public String getIdentifier() {
        return "slap";
    }

    @Override
    public String getName() {
        return "Slaparoo";
    }

    public static class SlapListener extends SHUListener.ShuffleModeListener<SLAP> {
        @Override
        public Class<SLAP> getGameMode() {
            return SLAP.class;
        }

        @Override
        public boolean matchLobby(String s) {
            return "slap".equals(s);
        }

        @Override
        public void onMatch(SLAP gameMode, String key, IPatternResult match) {
            super.onMatch(gameMode, key, match);
            if("slap.kill".equals(key)) gameMode.addPoints(10);
        }
    }
}
