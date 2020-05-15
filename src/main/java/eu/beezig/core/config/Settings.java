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

package eu.beezig.core.config;

import eu.beezig.core.Beezig;
import eu.beezig.core.advrec.AdvancedRecords;
import eu.beezig.core.util.text.Message;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.Locale;

public enum Settings {
    // Colors
    COLOR_PRIMARY(ChatColor.DARK_AQUA, "WOOL"),
    COLOR_ACCENT(ChatColor.AQUA, "WOOL"),

    // Misc
    THOUSANDS_SEPARATOR(true, "LEVER"),

    // Autovote
    AUTOVOTE(true, "LEVER"),
    AUTOVOTE_RANDOM(true, "LEVER"),

    // Advanced Records
    ADVANCED_RECORDS(true, "LEVER"),
    ADVREC_MODE(AdvancedRecords.Mode.NORMAL, "LEVER"),
    ADVREC_RANK(true, "LEVER"),
    ADVREC_TONEXT(true, "LEVER"),
    ADVREC_KD(true, "LEVER"),
    ADVREC_WINRATE(true, "LEVER"),

    // Trouble in Mineville
    TIMV_ADVREC_VICTORIES(true, "LEVER"),
    TIMV_ADVREC_KRR(true, "LEVER"),
    TIMV_ADVREC_TRATIO(true, "LEVER"),
    TIMV_ADVREC_RECORD(true, "LEVER"),
    TIMV_ADVREC_KPV(true, "LEVER");

    private final Object defaultValue;
    private final Class settingType;
    private final String labyIcon;

    Settings(Object defaultValue, String labyIcon) {
        this.defaultValue = defaultValue;
        this.settingType = defaultValue.getClass();
        this.labyIcon = labyIcon;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public Class getSettingType() {
        return settingType;
    }

    public String getLabyIcon() {
        return labyIcon;
    }

    public String getName() {
        return Message.translate("setting." + name().toLowerCase(Locale.ROOT) + ".name");
    }

    public String getDescription() {
        return Message.translate("setting." + name().toLowerCase(Locale.ROOT) + ".desc");
    }

    public Setting get() {
        return Beezig.cfg().get(this);
    }

}
