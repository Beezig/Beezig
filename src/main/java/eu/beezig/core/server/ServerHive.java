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

package eu.beezig.core.server;

import eu.beezig.core.Beezig;
import eu.beezig.core.server.listeners.TIMVListener;
import eu.the5zig.mod.server.*;

import java.util.Locale;

public class ServerHive extends ServerInstance {
    @Override
    public void registerListeners() {
        GameListenerRegistry registry = getGameListener();
        registry.registerListener(new ListenerHive());
        registry.registerListener(new TIMVListener());
    }

    @Override
    public String getName() {
        return "The Hive";
    }

    @Override
    public String getConfigName() {
        return "hive";
    }

    @Override
    public boolean handleServer(String host, int port) {
        return host.toLowerCase(Locale.ROOT).contains("hivemc.") || host.toLowerCase(Locale.ROOT).endsWith("hive.sexy")
                || host.toLowerCase(Locale.ROOT).contains(".j2o");
    }

    public static boolean isCurrent() {
        return Beezig.api().getActiveServer() instanceof ServerHive;
    }

    private static class ListenerHive extends AbstractGameListener<GameMode> {
        @Override
        public Class<GameMode> getGameMode() {
            return null;
        }

        @Override
        public boolean matchLobby(String s) {
            return false;
        }

        @Override
        public void onMatch(GameMode gameMode, String key, IPatternResult match) {
            if(gameMode != null || key == null) return;
            if(key.startsWith("join.")) getGameListener().switchLobby(key.replace("join.", ""));
        }
    }
}
