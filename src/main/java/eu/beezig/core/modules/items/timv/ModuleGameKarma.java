/*
 * Copyright (C) 2017-2020 Beezig Team
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

package eu.beezig.core.modules.items.timv;

import eu.beezig.core.Beezig;
import eu.beezig.core.server.modes.TIMV;
import eu.the5zig.mod.modules.GameModeItem;

public class ModuleGameKarma extends GameModeItem<TIMV> {
    public ModuleGameKarma() {
        super(TIMV.class);
    }

    @Override
    public void registerSettings() {
        getProperties().addSetting("potential", true);
    }

    @Override
    protected Object getValue(boolean b) {
        boolean showPotentialKarma = (boolean) getProperties().getSetting("potential").get();
        if(showPotentialKarma) {
            if(!Beezig.api().isInWorld()) return null;
            return String.format("%d/%d Karma", b ? 0 : getGameMode().getPoints(),
                    b ? 210 : getGameMode().getPoints() + getGameMode().getMaxKarma(Beezig.api().getServerPlayers().size()));
        }
        return (b ? 210 : getGameMode().getPoints()) + " Karma";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        if(dummy) return true;
        return super.shouldRender(false) && Beezig.api().isInWorld() && getGameMode().getRole() != null;
    }

    @Override
    public String getTranslation() {
        return "beezig.module.game";
    }
}
