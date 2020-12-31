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

package eu.beezig.core.config.i18n;

import eu.beezig.core.config.EnumSetting;

import java.util.Locale;

public class LanguageSetting extends EnumSetting {
    private String enumName;
    private Locale locale;

    public LanguageSetting(String enumName, Locale locale) {
        this.enumName = enumName;
        this.locale = locale;
    }

    public Locale getLocaleId() {
        return locale;
    }

    @Override
    public String name() {
        return enumName;
    }

    public static LanguageSetting valueOf(String name) {
        return LanguageConfiguration.languages.get(name);
    }

    public static LanguageSetting[] values() {
        return LanguageConfiguration.languages.values().toArray(new LanguageSetting[0]);
    }
}
