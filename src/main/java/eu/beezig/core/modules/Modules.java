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

package eu.beezig.core.modules;

import eu.beezig.core.Beezig;
import eu.beezig.core.modules.items.ModuleTokens;
import eu.the5zig.mod.ModAPI;

public class Modules {

    private static final String HIVE = "serverhivemc";

    public static void register(Beezig plugin, ModAPI api) {
        api.registerModuleItem(plugin, "tokens", ModuleTokens.class, HIVE);
    }
}
