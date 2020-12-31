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

package eu.beezig.core.logging.ws;

import com.google.gson.annotations.SerializedName;

public class WinstreakService {
    @SerializedName("streak")
    private int current;
    @SerializedName("bestStreak")
    private int best;
    private Long lastReset, bestReset;

    /**
     * This is true if the player has won
     */
    private transient boolean poisoned;

    public void increment() {
        poisoned = true;
        current++;
        if(best < current) best = current;
    }

    public void reset() {
        if(poisoned) return;
        lastReset = System.currentTimeMillis();
        if(best <= current) bestReset = lastReset;
        current = 0;
    }

    public int getCurrent() {
        return current;
    }

    public Long getLastReset() {
        return lastReset;
    }

    public Long getBestReset() {
        return bestReset;
    }

    public int getBest() {
        return best;
    }
}
