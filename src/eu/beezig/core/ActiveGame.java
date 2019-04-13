/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
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

package eu.beezig.core;

import eu.beezig.core.api.BeezigAPI;

public class ActiveGame {

    private static String current = "";

    public static String current() {
        return current;
    }

    public static void set(String s) {
        current = s;
        if (BeezigMain.hasExpansion) BeezigAPI.get().getListener().setActiveGame(s);
    }

    public static boolean is(String game) {
        return current.toUpperCase().equals(game.toUpperCase());
    }

    public static void reset(String game) {
        if (is(game)) set("");
    }

}
