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
