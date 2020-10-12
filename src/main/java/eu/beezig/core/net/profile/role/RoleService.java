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

package eu.beezig.core.net.profile.role;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import eu.beezig.core.Beezig;
import eu.beezig.core.net.profile.override.UserOverride;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.hiveapi.wrapper.utils.download.Downloader;
import eu.beezig.hiveapi.wrapper.utils.json.JObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RoleService {
    private static AsyncCache<Integer, UserRole> roleCache = Caffeine.newBuilder().maximumSize(100).buildAsync();

    public static CompletableFuture<RoleContainer> getRole(int id, UUID uuid) {
        CompletableFuture<UserOverride> override = CompletableFuture.completedFuture(null);
        // If 31st bit is set, we need to download a custom user override.
        if((id & (1 << 31)) == (1 << 31)) {
            id = id ^ (1 << 31);
            override = getOverride(uuid);
        }
        CompletableFuture<UserRole> role = getUserRole(id);
        CompletableFuture<UserOverride> finalOverride = override;
        return CompletableFuture.allOf(override, role).thenApplyAsync(v -> {
            RoleContainer cnt = new RoleContainer(role.join());
            cnt.setOverride(finalOverride.join());
            return cnt;
        });
    }

    public static CompletableFuture<UserRole> getUserRole(int id) {
        UserRole role = DefaultUserRoles.fromIndex(id);
        if(role != null) return CompletableFuture.completedFuture(role);
        return roleCache.get(id, RoleService::getRemoteRole);
    }

    private static UserRole getRemoteRole(int id) {
        JObject json = null;
        try {
            json = Downloader.getJsonObject(new URL("https://web.beezig.eu/v1/server/roles/" + id)).join();
        } catch (MalformedURLException e) {
            ExceptionHandler.catchException(e);
        }
        return Beezig.gson.fromJson(json.getInput().toJSONString(), RemoteUserRole.class);
    }

    private static CompletableFuture<UserOverride> getOverride(UUID id) {
        try {
            return Downloader.getJsonObject(new URL("https://static.beezig.eu/useroverrides/" + id.toString() + ".json"))
                .thenApplyAsync(j -> Beezig.gson.fromJson(j.getInput().toJSONString(), UserOverride.class));
        } catch (MalformedURLException e) {
            ExceptionHandler.catchException(e);
            return null;
        }
    }
}
