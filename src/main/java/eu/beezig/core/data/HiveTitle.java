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

package eu.beezig.core.data;

import com.google.gson.annotations.SerializedName;
import eu.the5zig.util.minecraft.ChatColor;

public class HiveTitle {
    @SerializedName("required_points")
    private int requiredPoints;
    @SerializedName("human_name")
    private String humanName;
    @SerializedName("plain_name")
    private String plainName;

    private transient String extraData;

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }

    public String getColoredName() {
        return ChatColor.translateAlternateColorCodes('&', getHumanName());
    }

    public String getPlainName() {
        return plainName;
    }

    public void setPlainName(String plainName) {
        this.plainName = plainName;
    }

    public String getHumanName() {
        if(extraData == null) return humanName;
        return humanName + extraData;
    }

    public void setHumanName(String humanName) {
        this.humanName = humanName;
    }

    public void setRequiredPoints(int requiredPoints) {
        this.requiredPoints = requiredPoints;
    }
}
