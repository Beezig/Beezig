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
import eu.beezig.core.api.BeezigForge;
import eu.beezig.core.config.Settings;
import eu.beezig.core.server.modes.SHU;
import eu.beezig.core.server.modes.shu.ShuffleMode;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.IPatternResult;
import eu.the5zig.util.minecraft.ChatColor;

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

    @Override
    public boolean onActionBar(SHU gameMode, String message) {
        if(message == null || gameMode == null) return super.onActionBar(gameMode, message);
        String stripped = ChatColor.stripColor(message);
        if(BeezigForge.isSupported() && "Starting game in 6".equals(stripped) && Settings.SHU_WINNER_CHECK.get().getBoolean()) {
            BeezigForge.get().shuffleOpenWinner();
        }
        if(BeezigForge.isSupported() && "Starting game in 5".equals(stripped) && Settings.SHU_WINNER_CHECK.get().getBoolean()) {
            BeezigForge.get().shuffleCheckWinner(mode -> {
                Message.info(Beezig.api().translate("msg.shu.winner", Color.accent() + mode + Color.primary()));
                if(Settings.SHU_WINNER_LEAVE.get().getBoolean()) {
                    try {
                        if(!gameMode.getAutovoteManager().getFavoriteMaps("shu").contains(StringUtils.normalizeMapName(mode))) {
                            Message.info(Message.translate("msg.shu.winner.leave"));
                            Beezig.api().sendPlayerMessage("/hub");
                            Beezig.api().sendPlayerMessage("/q shu");
                        }
                    } catch (Exception e) {
                        ExceptionHandler.catchException(e);
                        Message.error(Message.translate("error.data_read"));
                    }
                }
            });
        }
        return super.onActionBar(gameMode, message);
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
