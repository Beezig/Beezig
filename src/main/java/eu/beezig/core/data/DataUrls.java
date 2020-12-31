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

package eu.beezig.core.data;

class DataUrls {
    private static final String GITLAB_BASE = "https://gitlab.com/api/v4/projects/21868639/repository/";
    static final String LATEST_COMMIT = GITLAB_BASE + "branches/master";
    static final String DOWNLOAD = GITLAB_BASE + "archive.zip";
    static final String TITLES_FORMAT = "https://api.hivemc.com/v1/game/%s/titles";
}
