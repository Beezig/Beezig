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

import eu.beezig.core.logging.ws.WinstreakService;
import eu.beezig.core.modules.Modules;
import eu.beezig.core.server.HiveMode;
import eu.beezig.core.server.IWinstreak;
import eu.beezig.core.util.text.Message;
import eu.the5zig.mod.modules.GameModeItem;

public class ModuleWinstreak extends GameModeItem<HiveMode> {
    public ModuleWinstreak() {
        super(HiveMode.class);
    }

    @Override
    public void registerSettings() {
        getProperties().addSetting("best", true);
    }

    @Override
    protected Object getValue(boolean dummy) {
        if(dummy) return "10 (10)";
        boolean best = (boolean) getProperties().getSetting("best").get();
        WinstreakService service = getGameMode().getWinstreakService();
        return best
            ? String.format("%s (%s)", Message.formatNumber(service.getCurrent()), Message.formatNumber(service.getBest()))
            : Message.formatNumber(service.getCurrent());
    }

    @Override
    public String getTranslation() {
        return "beezig.module.streak";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        return dummy || (Modules.render() && getGameMode() instanceof IWinstreak && getGameMode().getWinstreakService() != null);
    }
}
