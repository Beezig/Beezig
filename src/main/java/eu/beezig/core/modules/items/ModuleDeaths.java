/*
 * Copyright (C) 2019 Beezig Team
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

package eu.beezig.core.modules.items;

import eu.beezig.core.modules.Modules;
import eu.beezig.core.server.HiveMode;
import eu.beezig.core.util.Message;
import eu.the5zig.mod.modules.GameModeItem;

public class ModuleDeaths extends GameModeItem<HiveMode> {
    public ModuleDeaths() {
        super(HiveMode.class);
    }

    @Override
    public void registerSettings() {
        getProperties().addSetting("show_global", true);
    }

    @Override
    protected Object getValue(boolean b) {
        StringBuilder builder = new StringBuilder();
        builder.append(Message.formatNumber(b ? 1234 : getGameMode().getDeaths()));
        boolean showGlobal = (boolean) getProperties().getSetting("show_global").get();
        if(showGlobal) {
            if(b) builder.append(" (").append(Message.formatNumber(12345)).append(")");
            else if(getGameMode().getGlobal().getDeaths() != null) builder.append(" (").append(Message.formatNumber(getGameMode().getGlobal().getDeaths())).append(")");
        }
        return builder.toString();
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        return dummy || Modules.render() && (getGameMode() != null && getGameMode().getDeaths() != 0);
    }

    @Override
    public String getTranslation() {
        return "modules.item.hive_deaths";
    }
}
