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

package eu.beezig.core.modules.items;

import eu.beezig.core.modules.Modules;
import eu.beezig.core.server.HiveMode;
import eu.beezig.core.server.modes.TIMV;
import eu.beezig.core.util.text.Message;
import eu.the5zig.mod.modules.GameModeItem;

public class ModuleDaily extends GameModeItem<HiveMode> {
    public ModuleDaily() {
        super(HiveMode.class);
    }

    @Override
    public void registerSettings() {
        getProperties().addSetting("place", true);
    }

    @Override
    protected Object getValue(boolean b) {
        if(b) return "123 Points";
        HiveMode mode = getGameMode();
        boolean place = (boolean) getProperties().getSetting("place").get() && mode.getDailyService().getPlace() != -1;
        StringBuilder builder = new StringBuilder();
        builder.append(Message.formatNumber(getGameMode().getDailyService().getPoints())).append(" ")
            .append(getGameMode() instanceof TIMV ? "Karma" : Message.translate("modules.item.hive_points"));
        if(place) {
            builder.append(" (#").append(Message.formatNumber(mode.getDailyService().getPlace())).append(")");
        }
        return builder.toString();
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        return dummy || (Modules.render() && getGameMode() != null && getGameMode().getDailyService() != null);
    }

    @Override
    public String getTranslation() {
        return "beezig.module.daily";
    }
}
