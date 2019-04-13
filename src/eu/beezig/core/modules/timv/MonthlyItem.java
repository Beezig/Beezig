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

package eu.beezig.core.modules.timv;

import eu.beezig.core.Log;
import eu.beezig.core.games.TIMV;
import eu.the5zig.mod.modules.GameModeItem;
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
            return TIMV.shouldRender(getGameMode().getState());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void registerSettings() {
        getProperties().addSetting("field", MonthlyField.POINTS, MonthlyField.class);
    }
}
