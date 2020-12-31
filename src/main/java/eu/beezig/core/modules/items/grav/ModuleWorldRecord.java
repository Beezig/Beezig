/*
 * Copyright (C) 2017-2021 Beezig Team
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

package eu.beezig.core.modules.items.grav;

import eu.beezig.core.server.modes.GRAV;
import eu.the5zig.mod.modules.GameModeItem;

public class ModuleWorldRecord extends GameModeItem<GRAV> {
    public ModuleWorldRecord() {
        super(GRAV.class);
    }

    @Override
    protected Object getValue(boolean b) {
        if(b) return "1:02 (RoccoDev)";
        GRAV mode = getGameMode();
        if(mode == null || mode.getCurrentMapWr() == null) return null;
        return mode.getCurrentMapWr().getDisplay();
    }

    @Override
    public String getTranslation() {
        return "beezig.module.dr.wr";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        if(dummy) return true;
        return super.shouldRender(false);
    }
}
