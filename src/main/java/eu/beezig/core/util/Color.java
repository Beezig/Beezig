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

package eu.beezig.core.util;

import eu.beezig.core.config.Settings;

public class Color {
    private static String primary, accent;

    public static String primary() {
        return primary;
    }
    public static String accent() {
        return accent;
    }

    public static void refreshCache() {
        primary = Settings.COLOR_PRIMARY.get().getValue().toString();
        accent = Settings.COLOR_ACCENT.get().getValue().toString();
    }

    public enum RankColor {
        REGULAR("§9"),
        GOLD("§6"),
        DIAMOND("§b"),
        EMERALD("§a"),
        ULTIMATE("§d"),
        VIP("§5"),
        YOUTUBER("§5"),
        STREAMER("§5"),
        CONTRIBUTOR("§5"),
        NECTAR("§3"),
        RESERVED_STAFF("§2"),
        HELPER("§2"),
        MODERATOR("§c"),
        SRMODERATOR("§3"),
        STAFFMANAGER("§e"),
        COMMUNITYMANAGER("§e"),
        DEVELOPER("§e"),
        OWNER("§e");

        private final String prefix;

        RankColor(String prefix) {
            this.prefix = prefix;
        }

        public static RankColor safeGet(String id) {
            try {
                return RankColor.valueOf(id);
            } catch (IllegalArgumentException ignored) {
                return REGULAR;
            }
        }

        @Override
        public String toString() {
            return prefix;
        }
    }
}
