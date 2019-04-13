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

package eu.beezig.core.notes;

import eu.the5zig.mod.The5zigAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesManager {

    public static List<String> notes = new ArrayList<>();

    public static boolean HR1cm5z = false;


    public static void tramontoccataStelle() {

        String[] ℛ = {
                //Non-ASCII characters in an indentifier smh
                "Va, pensiero",
                "Nessun dorma",
                "Vincerò",
                "O mio babbino caro",
                "Veni veni",
                "Cantate Dominum canticum novum",
                "Laus eius in ecclesia sanctorum",
                "Emmanuel captivum solve Israel",
                "Qui gemit in exsilio, privatus Dei Filio",
                "La donna è mobile",
                "Libiaaamo libiaaamo nei lieti caaalici"
        };

        Random random = new Random();
        int start = 0;
        int end = ℛ.length - 1;

        int index = random.nextInt(end - start) + start;

        The5zigAPI.getAPI().sendPlayerMessage("/msg Toccata " + ℛ[index]);
    }


}
