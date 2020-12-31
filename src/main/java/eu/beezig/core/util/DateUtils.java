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

import java.time.Instant;
import java.util.Date;

public class DateUtils {
    @SuppressWarnings("JdkObsolete")
    public static Instant toInstant(Date date) {
        return date.toInstant();
    }

    @SuppressWarnings("JdkObsolete")
    public static int instantCompare(Date date1, Date date2) {
        return date1.toInstant().compareTo(date2.toInstant());
    }

    @SuppressWarnings("JdkObsolete")
    public static Date toDate(Instant instant) {
        return Date.from(instant);
    }
}
