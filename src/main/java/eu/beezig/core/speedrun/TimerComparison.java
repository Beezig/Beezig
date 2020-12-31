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

package eu.beezig.core.speedrun;

import eu.beezig.core.util.text.Message;

import java.util.Locale;

public enum TimerComparison {
    PERSONAL_BEST(0),
    LATEST_RUN(7),
    BEST_SEGMENTS(1),
    WORST_SEGMENTS(5),
    MEDIAN_SEGMENTS(4),
    AVERAGE_SEGMENTS(3),
    BALANCED_PB(6),
    NONE(8);

    private final int enumKey;

    public int getEnumKey() {
        return enumKey;
    }

    TimerComparison(int enumKey) {
        this.enumKey = enumKey;
    }

    public String translate() {
        return Message.translate("speedrun.cmp." + name().toLowerCase(Locale.ROOT));
    }
}
