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
        .build();

    public static final List<String> defaultModules = Lists.newArrayList("game_info", "segments",
        "detailed_timer", "previous_segment", "possible_time_save", "sum_of_best");
}
