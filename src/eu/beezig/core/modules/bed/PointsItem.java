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
import eu.beezig.core.hiveapi.APIValues;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.util.minecraft.ChatColor;

public class PointsItem extends GameModeItem<BED> {

    public PointsItem() {
        super(BED.class);
    }

    private String getMainFormatting() {
        return "§r";
    }

    @Override
    protected Object getValue(boolean dummy) {
        try {
            if ((boolean) getProperties().getSetting("showrank").get()) {
                StringBuilder sb = new StringBuilder();
                if ((boolean) getProperties().getSetting("showcolor").get()) {
                    sb.append(Log.df(APIValues.BEDpoints)).append(" (").append(BED.rank).append(getMainFormatting());

                } else {

                    sb.append(Log.df(APIValues.BEDpoints)).append(" (").append(ChatColor.stripColor(BED.rank));
                }

                if ((boolean) getProperties().getSetting("showpointstonextrank").get()) {
                    if (BED.rankObject == null) return Log.df(APIValues.BEDpoints);
                    sb.append((boolean) getProperties().getSetting("showcolor").get() ? " / " + BED.rankObject.getPointsToNextRank((int) APIValues.BEDpoints) : " / " + ChatColor.stripColor(BED.rankObject.getPointsToNextRank((int) APIValues.BEDpoints)));

                }
                sb.append(

                        (boolean) getProperties().getSetting("showcolor").get() ?

                                getMainFormatting() + ")" :
                                ")");
                return sb.toString().trim();
            }
            return Log.df(APIValues.BEDpoints);
        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getTranslation() {
        return "beezig.module.points";
    }

    @Override
    public void registerSettings() {
        getProperties().addSetting("showrank", false);
        getProperties().addSetting("showcolor", true);
        getProperties().addSetting("showpointstonextrank", false);
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (BED.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
