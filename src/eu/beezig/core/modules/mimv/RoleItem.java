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

package eu.beezig.core.modules.mimv;

import eu.beezig.core.games.MIMV;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.util.minecraft.ChatColor;

public class RoleItem extends GameModeItem<MIMV> {

    public RoleItem() {
        super(MIMV.class);
    }

    @Override
    protected Object getValue(boolean dummy) {


        return (boolean) getProperties().getSetting("showcolor").get() ? MIMV.role : ChatColor.stripColor(MIMV.role);


    }

    @Override
    public void registerSettings() {
        // TODO Auto-generated method stub
        getProperties().addSetting("showcolor", true);


    }


    @Override
    public String getTranslation() {
        return "beezig.module.role";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (MIMV.shouldRender(getGameMode().getState()) && MIMV.role != null && !MIMV.role.isEmpty());
        } catch (Exception e) {
            return false;
        }
    }

}
