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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import eu.beezig.core.speedrun.render.TimerModule;
import eu.beezig.core.speedrun.render.modules.*;

import java.util.List;
import java.util.Map;

public class SpeedrunModules {
    public static Map<String, Class<? extends TimerModule>> registry = ImmutableMap.<String, Class<? extends TimerModule>>builder()
        .put("detailed_timer", SpeedrunDetailedTimer.class)
        .put("game_info", SpeedrunGameInfo.class)
        .put("possible_time_save", SpeedrunPossibleTimeSave.class)
        .put("previous_segment", SpeedrunPrevSegment.class)
        .put("sum_of_best", SpeedrunSumOfBest.class)
        .put("segments", SpeedrunSegmentView.class)
        .put("comparison", SpeedrunComparison.class)
        .build();

    public static final List<String> defaultModules = Lists.newArrayList("game_info", "segments",
        "detailed_timer", "previous_segment", "possible_time_save", "sum_of_best");
}
