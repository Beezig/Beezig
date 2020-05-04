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

package eu.beezig.core.calc.ps;

import eu.beezig.core.Beezig;
import eu.beezig.core.util.Message;
import eu.beezig.core.util.UUIDUtils;
import eu.beezig.hiveapi.wrapper.player.GameStats;
import eu.the5zig.mod.util.NetworkPlayerInfo;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerStatsCalculator {
    public static void calculate(String modeName, String stat, List<NetworkPlayerInfo> playersIn) {
        PlayerStatsMode mode = PlayerStats.modes.get(modeName.toLowerCase(Locale.ROOT));
        if(mode == null) {
            Message.error(Message.translate("error.ps.mode_not_found"));
            return;
        }
        String apiStat = mode.getApiKey(stat);
        if(apiStat == null) {
            Message.error(Message.translate("error.ps.stat_not_found"));
            return;
        }
        List<NetworkPlayerInfo> players = playersIn == null ? Beezig.api().getServerPlayers() : playersIn;
        // Map all UUIDs to display names, required to format the results later
        HashMap<String, String> displayNames = players.stream()
            .collect(Collectors.toMap(p -> UUIDUtils.strip(p.getGameProfile().getId()), NetworkPlayerInfo::getDisplayName, (x, y) -> y, HashMap::new));
        // Discard exceptions (profile not found etc.)
        CompletableFuture<? extends GameStats>[] stats = players.stream().map(p -> mode.get(p.getGameProfile().getId()).exceptionally(e -> null)).toArray(CompletableFuture[]::new);
        // Wait for the tasks to complete, then sort the results.
        // PlayerStatsProfile implements Comparable for itself, so a call to .sorted() will give us
        // the profiles sorted in ascending order.
        Message.info(Message.translate("msg.loading"));
        CompletableFuture.allOf(stats)
            .thenAcceptAsync(v -> {
                DoubleSummaryStatistics summary = Stream.of(stats)
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .map(s -> mode.getProfile(displayNames, s, apiStat, stat))
                    .sorted()
                    .mapToDouble(p -> {
                        Message.info(String.format("%s §7-§b %s", p.getDisplayName(), Message.ratio(p.getStat())));
                        return p.getStat().doubleValue();
                    }).summaryStatistics();
                Message.info(Beezig.api().translate("msg.ps.done", modeName,
                        "§b" + Message.ratio(summary.getSum()) + "§3", "§b" + Message.ratio(summary.getAverage()) + "§3"));
            }).exceptionally(e -> {
                        Message.error(Message.translate("error.ps.generic"));
                        e.printStackTrace();
                        return null;
        });
    }
}
