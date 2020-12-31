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

package eu.beezig.core.calc.lb;

import eu.beezig.core.Beezig;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.UUIDUtils;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;
import eu.beezig.hiveapi.wrapper.game.Game;
import eu.beezig.hiveapi.wrapper.game.leaderboard.GameLeaderboard;
import eu.beezig.hiveapi.wrapper.game.leaderboard.LeaderboardPlace;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LeaderboardCalculator {
    private String mode;
    private int start, end;

    public LeaderboardCalculator(String mode, int start, int end) {
        this.mode = mode;
        this.start = start;
        this.end = end;
    }

    public void run() {
        calculate().thenComposeAsync(list -> {
            Map<Object, CompletableFuture<String>> names =
                list.stream().collect(Collectors.toMap(profile -> profile.get("UUID"),
                    profile -> UUIDUtils.getNameWithOptionalRank(profile.get("UUID").toString(),
                        profile.get("username").toString(), null), (x, y) -> y, HashMap::new));
            return CompletableFuture.allOf(names.values().toArray(new CompletableFuture[0]))
                .thenApplyAsync(v -> list.stream().map(profile -> new FormattedPlace(names.get(profile.get("UUID")).join(), profile)));
        }).thenAcceptAsync(this::display).exceptionally(e -> {
            Message.error(Message.translate("error.leaderboard"));
            ExceptionHandler.catchException(e);
            return null;
        });
    }

    private CompletableFuture<List<LeaderboardPlace>> calculate() {
        return new Game(mode, null).getLeaderboard(start - 1, end).thenApplyAsync(GameLeaderboard::getPlayers);
    }

    private void display(Stream<FormattedPlace> lb) {
        Beezig.api().messagePlayer(StringUtils.linedCenterText(Color.primary(), Color.primary() + Beezig.api().translate("msg.leaderboard",
            Color.accent() + mode.toUpperCase(Locale.ROOT) + Color.primary())));
        lb.forEachOrdered(formattedPlace -> {
            LeaderboardPlace place = formattedPlace.place;
            String key = place.containsKey("points") ? "points" : (place.containsKey("total_points") ? "total_points" :
                (place.containsKey("karma") ? "karma" : "victories"));
            Beezig.api().messagePlayer(Color.primary() + " #" + Color.accent() + place.getHumanPlace()
                + Color.primary() + "§7 ▏ " + Color.accent() + Message.formatNumber((long) place.get(key)) + "§7 - "
                + formattedPlace.displayName);
        });
        Beezig.api().messagePlayer(StringUtils.linedCenterText(Color.primary(), Color.primary() + "#" + Color.accent() + (start == end ? start : start + "-" + end)));
    }

    private static class FormattedPlace {
        String displayName;
        LeaderboardPlace place;

        public FormattedPlace(String displayName, LeaderboardPlace place) {
            this.displayName = displayName;
            this.place = place;
        }
    }
}
