package tk.roccodev.beezig.advancedrecords.anywhere;

import tk.roccodev.beezig.advancedrecords.anywhere.statistic.RecordsStatistic;

import java.util.List;

public class GamemodeStatistics {

    private String gamemode;
    private List<RecordsStatistic> statistics;

    public GamemodeStatistics(String gamemode, List<RecordsStatistic> statistics) {
        this.gamemode = gamemode;
        this.statistics = statistics;
    }

    public String getGamemode() {
        return gamemode;
    }

    public List<RecordsStatistic> getStatistics() {
        return statistics;
    }

}
