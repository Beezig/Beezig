package eu.beezig.core.modules.timv;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.Log;
import eu.beezig.core.games.TIMV;
import eu.the5zig.mod.modules.GameModeItem;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.gnt.GntMonthlyProfile;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.timv.TimvMonthlyProfile;

import java.text.DecimalFormat;

public class MonthlyItem extends GameModeItem<TIMV> {

    public MonthlyItem() {
        super(TIMV.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        if (dummy) return "No Profile";
        if (!TIMV.hasLoaded) return "Loading...";

        StringBuilder sb = new StringBuilder();
        sb.append("#").append(TIMV.monthly.getPlace()).append(" ▏ ");

        MonthlyField selected = (MonthlyField) getProperties().getSetting("field").get();
        TimvMonthlyProfile profile = TIMV.monthly;

        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);

        switch (selected) {
            case POINTS:
                sb.append(Log.df(profile.getPoints())).append(" ").append(Log.t("beezig.module.timv.karma"));
                break;
            case TPOINTS:
                sb.append(Log.df(profile.getTraitorPoints())).append(" ").append(Log.t("beezig.str.timv.tpoints"));
                break;
            case IPOINTS:
                sb.append(Log.df(profile.getInnocentPoints())).append(" ").append(Log.t("beezig.str.timv.ipoints"));
                break;
            case DPOINTS:
                sb.append(Log.df(profile.getDetectivePoints())).append(" ").append(Log.t("beezig.str.timv.dpoints"));
                break;
            case RPOINTS:
                sb.append(Log.df(profile.getRolePoints())).append(" ").append(Log.t("beezig.str.timv.rpoints"));
                break;
            case KR:
                double kr = profile.getPoints() / (double) profile.getRolePoints();
                sb.append("K/R: ").append(df.format(kr));
                break;
            case TSHARE:
                double tr = profile.getTraitorPoints() / (double) profile.getRolePoints() * 100D;
                sb.append("T%: ").append(df.format(tr)).append("%");
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
            if (TIMV.monthly == null || !TIMV.hasLoaded) return false;
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
