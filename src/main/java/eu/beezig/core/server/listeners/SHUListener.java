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

import eu.beezig.core.server.modes.SHU;
import eu.beezig.core.server.modes.shu.ShuffleMode;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.IPatternResult;

import java.util.Locale;

public class SHUListener extends AbstractGameListener<SHU> {
    @Override
    public Class<SHU> getGameMode() {
        return SHU.class;
    }

    @Override
    public boolean matchLobby(String s) {
        return "shu".equals(s);
    }

    @Override
    public void onMatch(SHU gameMode, String key, IPatternResult match) {
        if(gameMode != null && "shu.game".equals(key) && gameMode.getGame() == null) gameMode.joinGame(match.get(0));
    }

    public abstract static class ShuffleModeListener<T extends ShuffleMode> extends AbstractGameListener<T> {
        @Override
        public void onMatch(T gameMode, String key, IPatternResult match) {
            if("shu.win".equals(key)) gameMode.shuffleWin();
        }

        public static <T extends ShuffleMode> ShuffleModeListener<T> simpleListener(Class<T> cls, String lobbyId) {
            String lobby = lobbyId.toLowerCase(Locale.ROOT);
            return new ShuffleModeListener<T>() {
                @Override
                public Class<T> getGameMode() {
                    return cls;
                }

                @Override
                public boolean matchLobby(String s) {
                    return lobby.equals(s);
                }
            };
        }
    }
}
