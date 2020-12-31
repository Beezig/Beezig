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
import eu.beezig.core.util.text.Message;
import eu.the5zig.mod.modules.GameModeItem;

public class ModuleKDRChange extends GameModeItem<HiveMode> {
    private KDRData currentData;

    public ModuleKDRChange() {
        super(HiveMode.class);
    }

    @Override
    protected Object getValue(boolean b) {
        if(b) return "3.14 (+0.01)";
        char prefix = currentData.kdrChange > 0 ? '+' : '-';
        return String.format("%s (%s)", Message.ratio(currentData.newKdr), prefix + Message.ratio(Math.abs(currentData.kdrChange)));
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        return dummy || (Modules.render() && (currentData = calculateKdr()) != null && currentData.kdrChange != 0);
    }

    @Override
    public String getTranslation() {
        return "modules.item.hive_kdr";
    }

    private KDRData calculateKdr() {
        if(getGameMode() == null) return null;
        HiveMode.GlobalStats cached = getGameMode().getCachedGlobal();
        if(cached.getKills() == null || cached.getDeaths() == null) return null;
        HiveMode.GlobalStats current = getGameMode().getGlobal();
        if(current.getKills() == null || current.getDeaths() == null) return null;
        KDRData data = new KDRData();
        double currentDeaths = current.getDeaths() == 0 ? 1 : current.getDeaths();
        double cachedDeaths = cached.getDeaths() == 0 ? 1 : cached.getDeaths();
        double newKdr = current.getKills() / currentDeaths;
        double oldKdr = cached.getKills() / cachedDeaths;
        double change = newKdr - oldKdr;
        data.newKdr = newKdr;
        data.kdrChange = change;
        return data;
    }

    private static class KDRData {
        double newKdr;
        double kdrChange;
    }
}
