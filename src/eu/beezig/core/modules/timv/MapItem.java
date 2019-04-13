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

import eu.beezig.core.games.TIMV;
import eu.beezig.core.hiveapi.stuff.timv.TIMVMap;
import eu.the5zig.mod.modules.GameModeItem;

public class MapItem extends GameModeItem<TIMV> {

    public MapItem() {
        super(TIMV.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        TIMVMap map = TIMV.activeMap;
        if (map == null) return "Unknown map";
        String name = map.getName();
        StringBuilder tr = new StringBuilder();
        tr.append(name);
        if ((boolean) getProperties().getSetting("showenderchests").get()) {

            boolean current = (boolean) getProperties().getSetting("showcurrent").get();
            int t = map.getEnderchests();

            tr.append(" (").append(t);
            if (current) {

                if ((TIMV.activeMap.getName().equalsIgnoreCase("Precinct") || TIMV.activeMap.getName().equalsIgnoreCase("Azure Island")) && TIMV.currentEnderchests != 0) {
                    //remove unaccessible echest from tracker
                    tr.append(" | ").append(TIMV.currentEnderchests - 1);
                } else tr.append(" | ").append(TIMV.currentEnderchests);
            }
            tr.append(")");
        }

        return tr.toString().trim();
    }

    @Override
    public void registerSettings() {
        // TODO Auto-generated method stub
        getProperties().addSetting("showenderchests", true);
        getProperties().addSetting("showcurrent", true);
    }

    // LabyMod
    protected String getCategoryKey() {
        return "timv";
    }

    @Override
    public String getTranslation() {
        return "beezig.module.map";
    }

    @Override
    public boolean shouldRender(boolean dummy) {

        try {
            if (getGameMode() == null) return false;
            return dummy || (TIMV.shouldRender(getGameMode().getState()) && TIMV.activeMap != null);
        } catch (Exception e) {
            return false;
        }
    }

}
