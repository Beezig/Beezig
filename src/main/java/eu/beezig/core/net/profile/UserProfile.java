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

import eu.beezig.core.net.profile.role.RoleContainer;
import eu.beezig.core.net.profile.role.RoleService;

import java.util.UUID;

public class UserProfile {
    private UUID uuid;
    private RoleContainer role;

    public UserProfile(UUID uuid, int role) {
        this.uuid = uuid;
        this.role = OwnProfile.defaultRole;
        RoleService.getRole(role, uuid).thenAcceptAsync(r -> this.role = r);
    }

    public UUID getUuid() {
        return uuid;
    }

    public RoleContainer getRoleContainer() {
        return role;
    }
}
