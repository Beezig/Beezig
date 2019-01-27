package eu.beezig.core.modules.sky;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.Log;
import eu.beezig.core.games.SKY;
import eu.the5zig.mod.modules.GameModeItem;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.sky.SkyMonthlyProfile;

import java.text.DecimalFormat;

public class MonthlyItem extends GameModeItem<SKY> {

    public MonthlyItem() {
        super(SKY.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        if (dummy) return "No Profile";
        if (!SKY.hasLoaded) return "Loading...";

        StringBuilder sb = new StringBuilder();
        sb.append("#").append(SKY.monthly.getPlace()).append(" ▏ ");

        MonthlyField selected = (MonthlyField) getProperties().getSetting("field").get();
        SkyMonthlyProfile profile = SKY.monthly;

        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);

        switch (selected) {
            case POINTS:
                sb.append(Log.df(profile.getPoints())).append(" ").append(Log.t("beezig.module.points"));
                break;
            case KILLS:
                sb.append(Log.df(profile.getKills())).append(" ").append(Log.t("beezig.module.kills"));
                break;
            case DEATHS:
                sb.append(Log.df(profile.getDeaths())).append(" ").append(Log.t("beezig.module.deaths"));
                break;
            case VICTORIES:
                sb.append(Log.df(profile.getVictories())).append(" ").append(Log.t("beezig.str.victories"));
                break;
            case PLAYED:
                sb.append(Log.df(profile.getGamesPlayed())).append(" ").append(Log.t("beezig.str.played"));
                break;
            case KD:
                double kd = profile.getKills() / (double) profile.getDeaths();
                sb.append("K/D: ").append(df.format(kd));
                break;
            case WL:
                double wl = profile.getVictories() / ((double) profile.getGamesPlayed() - (double) profile.getVictories());
                sb.append("W/L: ").append(df.format(wl));
                break;
        }

        return sb.toString().trim();

    }

    @Override
    public String getTranslation() {
        return "beezig.module.monthly";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        if (dummy) return false;
        try {
            if (getGameMode() == null)
                return false;
            if (SKY.monthly == null || !SKY.hasLoaded) return false;
            return ActiveGame.is("gnt") || ActiveGame.is("gntm");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void registerSettings() {
        getProperties().addSetting("field", MonthlyField.POINTS, MonthlyField.class);
    }
}
