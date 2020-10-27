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

package eu.beezig.core.util;

import eu.beezig.core.Beezig;
import eu.beezig.core.Constants;
import eu.the5zig.mod.ModAPI;

public class SystemInfo {
    public static String getSystemInfo() {
        ModAPI api = Beezig.api();
        Runtime rt = Runtime.getRuntime();

        String platformInfo = String.format("V: %s, Mod: %s, MC %s, DBG: %b\n", Constants.VERSION, api.getModVersion(), api.getMinecraftVersion(), Beezig.DEBUG);
        String playerInfo = String.format("U: %s (%s)\n", Beezig.user().getName(), Beezig.user().getId().toString());
        String systemInfo = String.format("OS: %s, J: %s\n", System.getProperty("os.name"), System.getProperty("java.version"));
        String resourceInfo = String.format("Mem (free): %d/%dM\n", rt.freeMemory() / (1024 * 1024), rt.totalMemory() / (1024 * 1024));

        String gameMode = api.getActiveServer() == null ? null :
                (api.getActiveServer().getGameListener().getCurrentGameMode() == null ? null : api.getActiveServer().getGameListener().getCurrentGameMode().getName());
        String lobby = api.getActiveServer() == null ? null : api.getActiveServer().getGameListener().getCurrentLobby();
        String serverInfo = String.format("Srv: %s, GM: %s, L: %s\n", api.getActiveServer(), gameMode, lobby);
        String netMgrInfo = String.format("Net: %b UsrCache: %d\n", Beezig.net().isConnected().get(), Beezig.net().getProfilesCache().getSize());

        return "```\n" +
                platformInfo +
                playerInfo +
                systemInfo +
                resourceInfo +
                serverInfo +
                netMgrInfo +
                "```";
    }
}
