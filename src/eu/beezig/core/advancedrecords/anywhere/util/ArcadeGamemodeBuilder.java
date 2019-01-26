package eu.beezig.core.advancedrecords.anywhere.util;

import eu.beezig.core.advancedrecords.anywhere.statistic.PercentRatioStatistic;
import eu.beezig.core.advancedrecords.anywhere.statistic.RecordsStatistic;

public class ArcadeGamemodeBuilder extends GamemodeBuilder {

    public ArcadeGamemodeBuilder(String gamemode, String pointsApiKey, String victoriesApiKey, String playedApiKey) {
        super(gamemode);

        addStatistic(new RecordsStatistic("Points", pointsApiKey));
        if (victoriesApiKey != null)
            addStatistic(new RecordsStatistic("Victories", victoriesApiKey));
        if (playedApiKey != null)
            addStatistic(new RecordsStatistic("Games Played", playedApiKey));
        if (victoriesApiKey != null)
            addStatistic(new PercentRatioStatistic("Win Rate", victoriesApiKey, playedApiKey));

    }

}
