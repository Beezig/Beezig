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

package eu.beezig.core.modules.bed;

import eu.beezig.core.Log;
import eu.beezig.core.games.BED;
import eu.the5zig.mod.modules.GameModeItem;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.bed.BedMonthlyProfile;

import java.text.DecimalFormat;

public class MonthlyItem extends GameModeItem<BED> {

    public MonthlyItem() {
        super(BED.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        if (dummy) return "No Profile";
        if (!BED.hasLoaded) return "Loading...";

        StringBuilder sb = new StringBuilder();
        sb.append("#").append(BED.monthly.getPlace()).append(" ▏ ");

        MonthlyField selected = (MonthlyField) getProperties().getSetting("field").get();
        BedMonthlyProfile profile = BED.monthly;

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
            case BEDS:
                sb.append(Log.df(profile.getBedsDestroyed())).append(" ").append(Log.t("beezig.str.bed.beds"));
                break;
            case TEAMS:
                sb.append(Log.df(profile.getTeamsEliminated())).append(" ").append(Log.t("beezig.str.bed.teams"));
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
            if (BED.monthly == null || !BED.hasLoaded) return false;
            return BED.shouldRender(getGameMode().getState());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void registerSettings() {
        getProperties().addSetting("field", MonthlyField.POINTS, MonthlyField.class);
    }
}
