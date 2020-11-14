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

package eu.beezig.core.net.profile;

import eu.beezig.core.Beezig;
import eu.beezig.core.net.profile.role.DefaultUserRoles;
import eu.beezig.core.net.profile.role.RoleContainer;
import eu.beezig.core.net.profile.role.RoleService;
import eu.beezig.core.util.ExceptionHandler;

import java.time.Instant;

public class OwnProfile {
    static final RoleContainer defaultRole = new RoleContainer(DefaultUserRoles.USER);
    private int id;
    private RoleContainer role;
    private final Instant firstLogin;
    private Region region;
    private boolean dailyScores;

    public OwnProfile(int id, byte role, long firstLogin, String regionId, String regionName) {
        this.id = id;
        this.role = defaultRole;
        RoleService.getRole(role, Beezig.user().getId()).thenAcceptAsync(r -> this.role = r).exceptionally(e -> {
            ExceptionHandler.catchException(e, "Profile load");
            return null;
        });
        this.firstLogin = Instant.ofEpochMilli(firstLogin);
        if(regionId != null) {
            this.region = new Region(regionId, regionName);
        }
    }

    public int getId() {
        return id;
    }

    public RoleContainer getRoleContainer() {
        return role;
    }

    public Instant getFirstLogin() {
        return firstLogin;
    }

    public Region getRegion() {
        return region;
    }

    public void setHasDailyScores(boolean dailyScores) {
        this.dailyScores = dailyScores;
    }

    /**
     * Returns whether the user's daily scores are available online
     */
    public boolean hasDailyScores() {
        return dailyScores;
    }
}
