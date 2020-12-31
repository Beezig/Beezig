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

package eu.beezig.core.util;

import java.util.Arrays;

public class ArrayUtils {
    public static <T> T[] getPage(T[] src, int pageNo, int pageSize) {
        int from = pageNo * pageSize;
        if(from >= src.length) return null;
        int to = from + pageSize;
        if(to > src.length) to = src.length;
        return Arrays.copyOfRange(src, from, to);
    }
}
