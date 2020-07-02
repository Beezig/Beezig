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
import eu.beezig.core.hiveapi.APIValues;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.util.minecraft.ChatColor;

public class KarmaItem extends GameModeItem<TIMV> {

    public KarmaItem() {
        super(TIMV.class);
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
                    sb.append(Log.df(APIValues.TIMVkarma)).append(" (").append(TIMV.rank).append(getMainFormatting());

                } else {

                    sb.append(Log.df(APIValues.TIMVkarma)).append(" (").append(ChatColor.stripColor(TIMV.rank));
                }

                if ((boolean) getProperties().getSetting("showpointstonextrank").get()) {
                    if (TIMV.rankObject == null) return Log.df(APIValues.TIMVkarma);
                    sb.append((boolean) getProperties().getSetting("showcolor").get() ? " / " + TIMV.rankObject.getKarmaToNextRank((int) APIValues.TIMVkarma) : " / " + ChatColor.stripColor(TIMV.rankObject.getKarmaToNextRank((int) APIValues.TIMVkarma)));

                }
                sb.append(

                        (boolean) getProperties().getSetting("showcolor").get() ?

                                getMainFormatting() + ")" :
                                ")");
                return sb.toString().trim();
            }
            return Log.df(APIValues.TIMVkarma);
        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    // LabyMod
    protected String getCategoryKey() {
        return "timv";
    }

    @Override
    public String getTranslation() {
        return "beezig.module.timv.karma";
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
            return dummy || TIMV.shouldRender(getGameMode().getState());
        } catch (Exception e) {
            return false;
        }
    }

}
