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

package eu.beezig.core.utils;

import java.util.ArrayList;
import java.util.List;

public class TabCompletionUtils {


    public static List<String> matching(String[] args, List<String> toMatch) {
        String arg = args[args.length - 1];
        List<String> tr = new ArrayList<>();
        if (!arg.isEmpty()) {
            for (String s : toMatch) {
                if (s.regionMatches(true, 0, arg, 0, arg.length()))
                    tr.add(s);
            }
        } else {
            tr.addAll(toMatch);
        }
        return tr;
    }


}
