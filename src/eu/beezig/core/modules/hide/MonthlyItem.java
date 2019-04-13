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

package eu.beezig.core.modules.hide;

import eu.beezig.core.Log;
import eu.beezig.core.games.HIDE;
import eu.the5zig.mod.modules.GameModeItem;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.hide.HideMonthlyProfile;

import java.text.DecimalFormat;

public class MonthlyItem extends GameModeItem<HIDE> {

    public MonthlyItem() {
        super(HIDE.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        if (dummy) return "No Profile";
        if (!HIDE.hasLoaded) return "Loading...";

        StringBuilder sb = new StringBuilder();
        sb.append("#").append(HIDE.monthly.getPlace()).append(" ▏ ");

        MonthlyField selected = (MonthlyField) getProperties().getSetting("field").get();
        HideMonthlyProfile profile = HIDE.monthly;

        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);

        switch (selected) {
            case POINTS:
                sb.append(Log.df(profile.getPoints())).append(" ").append(Log.t("beezig.module.points"));
                break;
            case KILLS:
                sb.append(Log.df(profile.getHiderKills() + profile.getSeekerKills())).append(" ").append(Log.t("beezig.module.kills"));
                break;
            case VICTORIES:
                sb.append(Log.df(profile.getVictories())).append(" ").append(Log.t("beezig.str.victories"));
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
            if (HIDE.monthly == null || !HIDE.hasLoaded) return false;
            return HIDE.shouldRender(getGameMode().getState());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void registerSettings() {
        getProperties().addSetting("field", MonthlyField.POINTS, MonthlyField.class);
    }
}
