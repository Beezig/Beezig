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

public class KarmaCounterItem extends GameModeItem<TIMV> {

    public KarmaCounterItem() {
        super(TIMV.class);
    }

    @Override
    protected Object getValue(boolean dummy) {


        StringBuilder sb = new StringBuilder();
        sb.append(TIMV.karmaCounter).append(" ").append(Log.t("beezig.module.timv.karma"));
        if ((boolean) getProperties().getSetting("showrolepoints").get()) {

            if (TIMV.dPoints != 0) {
                sb.append(" / ").append(TIMV.dPoints).append(" ").append(Log.t("beezig.str.timv.dpoints"));
            }
            if (TIMV.iPoints != 0) {
                sb.append(" / ").append(TIMV.iPoints).append(" ").append(Log.t("beezig.str.timv.ipoints"));
            }
            if (TIMV.tPoints != 0) {
                sb.append(" / ").append(TIMV.tPoints).append(" ").append(Log.t("beezig.str.timv.tpoints"));
            }


        }
        return sb.toString().trim();


    }

    @Override
    public void registerSettings() {
        // TODO Auto-generated method stub
        getProperties().addSetting("showrolepoints", true);


    }

    // LabyMod
    protected String getCategoryKey() {
        return "timv";
    }


    @Override
    public String getTranslation() {
        return "beezig.module.game";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (TIMV.shouldRender(getGameMode().getState()) && TIMV.karmaCounter != 0);
        } catch (Exception e) {
            return false;
        }
    }

}
