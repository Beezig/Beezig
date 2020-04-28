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
import eu.beezig.core.util.Message;

import java.util.Locale;

public enum Settings {
    THOUSANDS_SEPARATOR(true, "LEVER");

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
