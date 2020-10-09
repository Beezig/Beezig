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

package eu.beezig.core.config;

import eu.beezig.core.Beezig;
import eu.beezig.core.advrec.AdvancedRecords;
import eu.beezig.core.config.i18n.LanguageConfiguration;
import eu.beezig.core.notification.MessageIgnoreLevel;
import eu.beezig.core.util.text.Message;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.Locale;

public enum Settings {
    // Appearance
    LANGUAGE(LanguageConfiguration.NATIVE, Category.APPEARANCE),
    COLOR_PRIMARY(ChatColor.DARK_AQUA, Category.APPEARANCE),
    COLOR_ACCENT(ChatColor.AQUA, Category.APPEARANCE),
    THOUSANDS_SEPARATOR(true, Category.APPEARANCE),

    // Autovote
    AUTOVOTE(true, Category.AUTOVOTE),
    AUTOVOTE_RANDOM(true, Category.AUTOVOTE),
    AUTOVOTE_SHUFFLE(true, Category.AUTOVOTE),

    // Advanced Records
    ADVANCED_RECORDS(true, Category.ADVANCED_RECORDS),
    ADVREC_MODE(AdvancedRecords.Mode.NORMAL, Category.ADVANCED_RECORDS),
    ADVREC_RANK(true, Category.ADVANCED_RECORDS),
    ADVREC_TONEXT(true, Category.ADVANCED_RECORDS),
    ADVREC_KD(true, Category.ADVANCED_RECORDS),
    ADVREC_WINRATE(true, Category.ADVANCED_RECORDS),
    ADVREC_PPG(true, Category.ADVANCED_RECORDS),
    ADVREC_KPG(true, Category.ADVANCED_RECORDS),

    // Anti Sniper
    SNIPE_TYPO(true, Category.ANTI_SNIPER),
    SNIPE_PMS(true, Category.ANTI_SNIPER),
    SNIPE_PARTY(true, Category.ANTI_SNIPER),

    // Trouble in Mineville
    TIMV_ADVREC_KRR(true, Category.TROUBLE_IN_MINEVILLE),
    TIMV_ADVREC_TRATIO(true, Category.TROUBLE_IN_MINEVILLE),
    TIMV_ADVREC_RECORD(true, Category.TROUBLE_IN_MINEVILLE),
    TIMV_ADVREC_KPV(true, Category.TROUBLE_IN_MINEVILLE),
    TIMV_TESTMESSAGES(true, Category.TROUBLE_IN_MINEVILLE),

    // DrawIt
    DRAW_AUTOGUESS(false, Category.DRAW_IT),

    // Gravity
    GRAV_MAPNAMES(false, Category.GRAVITY),
    GRAV_CONFIRM_DISCONNECT(true, Category.GRAVITY),
    GRAV_NEXT_MAP(false, Category.GRAVITY),

    // Misc
    MSG_PING(true, Category.MISC),
    MSG_DND_MODE(MessageIgnoreLevel.IGNORE_ALERT, Category.MISC),
    MSG_DND_ALERT("I'm currently on Do Not Disturb mode. I'll get back to you later!", Category.MISC),
    BROADCAST_ACTIONS(true, Category.MISC),
    CHAT_LINKS(true, Category.MISC),
    AUTOGG(false, Category.MISC),
    AUTOGG_MESSAGE("gg", Category.MISC),
    AUTOGG_DELAY(0L, Category.MISC),
    AUTOGL(false, Category.MISC),
    AUTOGL_MESSAGE("gl & hf", Category.MISC),
    AUTOGL_DELAY(0L, Category.MISC),
    AUTONEWGAME(false, Category.MISC),
    AUTONEWGAME_DELAY(500L, Category.MISC),
    AUTONEWGAME_IN_PARTIES(false, Category.MISC),
    RECORD_DND(false, Category.MISC),
    CHAT_PARTY(true, Category.MISC),
    CHAT_FRIENDS(true, Category.MISC),
    REPORTS_NOTIFY(true, Category.MISC),
    TABLIST_BADGES(true, Category.MISC),
    UPDATE_CHECK(true, Category.MISC);

    private final Object defaultValue;
    private final Class settingType;
    private final Category category;

    Settings(Object defaultValue, Category category) {
        this.defaultValue = defaultValue;
        this.settingType = defaultValue.getClass();
        this.category = category;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public Class getSettingType() {
        return settingType;
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

    public Category getCategory() {
        return category;
    }

    public enum Category {
        APPEARANCE,
        AUTOVOTE,
        ADVANCED_RECORDS,
        ANTI_SNIPER,
        TROUBLE_IN_MINEVILLE,
        DRAW_IT,
        GRAVITY,
        MISC;

        public String getName () {
            return Message.translate(String.format("settings_category.%s.name", name().toLowerCase()));
        }
    }
}
