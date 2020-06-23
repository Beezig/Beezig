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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import eu.beezig.core.Beezig;
import eu.beezig.core.net.packets.PacketOnlineUsers;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ProfilesCache {
    private Cache<UUID, UserProfile> profilesCache;
    private Queue<FutureTask> tasks = new ArrayDeque<>();
    private AtomicInteger lastRequestID;

    public ProfilesCache() {
        profilesCache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(15, TimeUnit.MINUTES).build();
        lastRequestID = new AtomicInteger(0);
    }

    public void putAll(int requestId, List<UserProfile> profiles) {
        if(profiles.size() == 0) wakeTasks(requestId, null);
        for(UserProfile profile : profiles) {
            profilesCache.put(profile.getUuid(), profile);
            wakeTasks(requestId, profile);
        }
    }

    private void wakeTasks(int requestId, UserProfile profile) {
        for(FutureTask task : tasks) {
            if(task.requestId == requestId) task.consumer.accept(profile);
        }
    }

    public CompletableFuture<UserProfile> getProfile(UUID uuid) {
        CompletableFuture<UserProfile> future = new CompletableFuture<>();
        UserProfile temp = profilesCache.getIfPresent(uuid);
        if(temp != null) future.complete(temp);
        else {
            int id = lastRequestID.getAndIncrement();
            tasks.add(new FutureTask(id, uuid, future::complete));
            Beezig.get().getAsyncExecutor().execute(() -> Beezig.get().getNetworkManager().getHandler()
                    .sendPacket(new PacketOnlineUsers(id, uuid)));
        }
        return future;
    }

    private static class FutureTask {
        private int requestId;
        private UUID uuid;
        private Consumer<UserProfile> consumer;

        FutureTask(int requestId, UUID uuid, Consumer<UserProfile> consumer) {
            this.requestId = requestId;
            this.uuid = uuid;
            this.consumer = consumer;
        }
    }
}
