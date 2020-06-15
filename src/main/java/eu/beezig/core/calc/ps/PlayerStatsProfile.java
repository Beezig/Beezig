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

package eu.beezig.core.calc.ps;

import eu.beezig.core.data.HiveTitle;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;

public class PlayerStatsProfile implements Comparable<PlayerStatsProfile> {
    private String displayName;
    private Number stat;
    private HiveTitle title;

    public PlayerStatsProfile(String displayName, Number stat) {
        this.displayName = displayName;
        this.stat = stat;
    }

    public void setTitle(HiveTitle title) {
        this.title = title;
    }

    public Number getStat() {
        return stat;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int compareTo(PlayerStatsProfile o) {
        return Double.compare(stat.doubleValue(), o.stat.doubleValue());
    }

    public String getFormat() {
        if(title == null)
            return String.format("%s §7-%s %s", Message.ratio(stat), Color.accent(), displayName);
        String rankColor = StringUtils.getColor(title.getColoredName());
        return String.format("%s%s §7- %s %s", rankColor, Message.ratio(stat), title.getColoredName(), displayName);
    }
}
