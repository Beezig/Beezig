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

package eu.beezig.core.modules.items;

import eu.beezig.core.modules.Modules;
import eu.beezig.core.server.HiveMode;
import eu.beezig.core.server.modes.TIMV;
import eu.beezig.core.util.ModuleUtils;
import eu.beezig.core.util.text.Message;
import eu.the5zig.mod.modules.GameModeItem;

public class ModulePoints extends GameModeItem<HiveMode> {
    public ModulePoints() {
        super(HiveMode.class);
    }

    @Override
    public void registerSettings() {
        getProperties().addSetting("rank", true);
        getProperties().addSetting("nextrank", false);
    }

    @Override
    protected Object getValue(boolean b) {
        if(b) return getDummyDisplay();
        return Message.formatNumber(getGameMode().getGlobal().getPoints()) + getTitleDisplay();
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        return dummy || Modules.render() && (getGameMode() != null && getGameMode().getGlobal().getPoints() != null);
    }

    @Override
    public String getTranslation() {
        return Modules.render() && getGameMode() instanceof TIMV ? "beezig.module.timv.karma" : "modules.item.hive_points";
    }

    private String getDummyDisplay() {
        boolean showRank = (boolean) getProperties().getSetting("rank").get();
        boolean nextRank = (boolean) getProperties().getSetting("nextrank").get();
        StringBuilder sb = new StringBuilder();
        sb.append(Message.formatNumber(123456));
        if(!showRank) return sb.toString();
        sb.append(" (Rank");
        if(nextRank) {
            sb.append(" / 100 to Next Rank");
        }
        sb.append(')');
        return sb.toString();
    }

    private String getTitleDisplay() {
        HiveMode mode = getGameMode();
        if(mode == null || mode.getGlobal().getTitle() == null) return "";
        boolean showRank = (boolean) getProperties().getSetting("rank").get();
        if(!showRank) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(" (").append(mode.getGlobal().getTitle().getRight().getColoredName());
        boolean nextRank = (boolean) getProperties().getSetting("nextrank").get();
        if(nextRank && mode.getTitleService() != null && mode.getTitleService().isValid()) {
            String fmt = ModuleUtils.getTextFormatting(this);
            sb.append(fmt).append(" / ").append(mode.getTitleService().getToNext(mode.getGlobal().getTitle().getLeft(), mode.getGlobal().getPoints(),
                    fmt));
        }
        sb.append(')');
        return sb.toString();
    }
}
