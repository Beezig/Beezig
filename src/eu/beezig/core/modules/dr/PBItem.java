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

import eu.beezig.core.games.DR;
import eu.the5zig.mod.modules.GameModeItem;

public class PBItem extends GameModeItem<DR> {

    public PBItem() {
        super(DR.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        if (getGameMode().activeMap != null) {
            return getGameMode().currentMapPB;
        } else {
            return "No Personal Best";
        }
    }

    @Override
    public String getTranslation() {
        return "beezig.module.dr.pb";
    }

    @Override
    public void registerSettings() {
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (DR.shouldRender(getGameMode().getState()) && getGameMode().activeMap != null && getGameMode().role.equals("Runner"));
        } catch (Exception e) {
            return false;
        }
    }

}
