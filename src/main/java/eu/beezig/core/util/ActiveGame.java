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
import eu.beezig.core.server.HiveMode;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.ServerInstance;

public class ActiveGame {
    public static HiveMode get() {
        ServerInstance server = Beezig.api().getActiveServer();
        if(server == null) return null;
        GameMode mode = server.getGameListener().getCurrentGameMode();
        if(!(mode instanceof HiveMode)) return null;
        return (HiveMode) mode;
    }

    public static String getID() {
        HiveMode current = get();
        if(current == null) return null;
        return current.getIdentifier();
    }
}
