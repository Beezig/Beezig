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

package eu.beezig.core.util;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class CollectionUtils {
    public static <T> int indexOf(List<T> collection, Predicate<T> predicate) {
        return IntStream.range(0, collection.size())
                .filter(i -> predicate.test(collection.get(i)))
        .findAny().orElse(-1);
    }
}
