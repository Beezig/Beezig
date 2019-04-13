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

package eu.beezig.core.advancedrecords.anywhere.util;

import eu.beezig.core.advancedrecords.anywhere.GamemodeStatistics;
import eu.beezig.core.advancedrecords.anywhere.statistic.RecordsStatistic;

import java.util.ArrayList;
import java.util.List;

public class GamemodeBuilder {

    private String gamemode;
    private List<RecordsStatistic> statistics = new ArrayList<>();

    public GamemodeBuilder(String gamemode) {
        this.gamemode = gamemode;
    }

    public GamemodeBuilder addStatistic(RecordsStatistic stat) {
        statistics.add(stat);
        return this;
    }

    public GamemodeStatistics build() {
        return new GamemodeStatistics(gamemode, statistics);
    }


}
