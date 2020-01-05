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

package eu.beezig.core.modules.dr;

import eu.beezig.core.Log;
import eu.beezig.core.games.DR;
import eu.beezig.core.hiveapi.stuff.dr.DRMap;
import eu.the5zig.mod.modules.GameModeItem;

public class MapItem extends GameModeItem<DR> {

    public MapItem() {
        super(DR.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        DR dr = getGameMode();
        DRMap map = dr.activeMap;
        if (map == null) return "Unknown map";
        String name = map.getDisplayName();
        StringBuilder tr = new StringBuilder();
        tr.append(name);
        if ((boolean) getProperties().getSetting("showcheckpoints").get() && "Runner".equals(dr.role)) {
            int totalc = map.getCheckpoints();
            tr.append(" (").append(dr.checkpoints).append("/").append(totalc).append(" ").append(Log.t("beezig.str.dr.checkpoints")).append(")");
        }
        return tr.toString().trim();
    }

    @Override
    public String getTranslation() {
        return "beezig.module.map";
    }

    @Override
    public void registerSettings() {
        getProperties().addSetting("showcheckpoints", true);
    }

    @Override
    public boolean shouldRender(boolean dummy) {

        try {
            if (getGameMode() == null) return false;
            return dummy || (DR.shouldRender(getGameMode().getState()) && getGameMode().activeMap != null);
        } catch (Exception e) {
            return false;
        }
    }

}
