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

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.beezig.core.games.HIDE;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;

public class MapItem extends GameModeItem<HIDE> {

    public MapItem() {
        super(HIDE.class);
    }


    @Override
    protected Object getValue(boolean dummy) {
        try {

            boolean mostKills = (boolean) getProperties().getSetting("showrecord").get();


            return HIDE.activeMap + (mostKills ? " (" + HIDE.mostKills + ")" : "");
        } catch (Exception e) {
            e.printStackTrace();
            return "No Map";
        }
    }

    @Override
    public String getTranslation() {
        return "beezig.module.map";
    }

    @Override
    public void registerSettings() {
        getProperties().addSetting("showrecord", true);
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && ActiveGame.is("hide") && HIDE.activeMap != null);
        } catch (Exception e) {
            return false;
        }
    }

}
