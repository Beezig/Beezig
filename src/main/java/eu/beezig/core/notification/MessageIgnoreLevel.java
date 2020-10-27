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

package eu.beezig.core.notification;

/**
 * Specifies the behavior that the notification manager should have when parsing private messages when the
 * 'Do not disturb' mode is on.
 */
public enum MessageIgnoreLevel {
    /**
     * Ignore the message completely.
     */
    IGNORE,

    /**
     * Ignore the message completely, and alert the sender of your absence.
     */
    IGNORE_ALERT,

    /**
     * Show the message in a separate window.
     */
    SEPARATE
}
