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
import eu.beezig.hiveapi.wrapper.mojang.UsernameToUuid;
import eu.beezig.hiveapi.wrapper.utils.download.Downloader;
import eu.beezig.hiveapi.wrapper.utils.json.JObject;
import org.apache.commons.lang3.text.WordUtils;
import org.json.simple.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class MonthlyCalculator {
    private final String mode, player;
    private final int start, end;

    public MonthlyCalculator(String mode, String player, int start, int end) {
        this.mode = mode;
        this.player = player;
        this.start = start;
        this.end = end;
    }

    public void run() {
        if(player != null) {
            getProfile().thenAcceptAsync(this::displayProfile).exceptionally(e -> {
                Message.error(Message.translate("error.leaderboard"));
                ExceptionHandler.catchException(e);
                return null;
            });
        }
        else {
            calculateLeaderboard().thenAcceptAsync(this::displayLeaderboard).exceptionally(e -> {
                Message.error(Message.translate("error.leaderboard"));
                ExceptionHandler.catchException(e);
                return null;
            });
        }
    }

    private CompletableFuture<Set<MonthlyPlace>> calculateLeaderboard() {
        return getLeaderboard().thenApplyAsync(json -> {
            Set<MonthlyPlace> result = new TreeSet<>();
            List<CompletableFuture<Void>> names = new ArrayList<>(json.getInput().size());
            for(Object o : json.getInput().entrySet()) {
                Map.Entry<String, JSONObject> entry = (Map.Entry<String, JSONObject>) o;
                MonthlyPlace place = new MonthlyPlace();
                place.place = (long) entry.getValue().get("place");
                place.points = (long) entry.getValue().get(entry.getValue().containsKey("points") ? "points" : "karma");
                place.username = entry.getValue().get(entry.getValue().containsKey("username") ? "username" : "name").toString();
                names.add(UUIDUtils.getNameWithOptionalRank(entry.getKey(), place.username, null).thenAcceptAsync(name -> place.username = name));
                result.add(place);
            }
            CompletableFuture.allOf(names.toArray(new CompletableFuture[0])).join();
            return result;
        });
    }

    private void displayLeaderboard(Set<MonthlyPlace> lb) {
        Beezig.api().messagePlayer(StringUtils.linedCenterText(Color.primary(), Color.primary() + Beezig.api().translate("msg.leaderboard",
            Color.accent() + mode.toUpperCase(Locale.ROOT) + Color.primary())));
        for(MonthlyPlace place : lb) {
            Beezig.api().messagePlayer(Color.primary() + " #" + Color.accent() + place.place
                + Color.primary() + "§7 ▏ " + Color.accent() + Message.formatNumber(place.points) + "§7 - "
                + place.username);
        }
        Beezig.api().messagePlayer(StringUtils.linedCenterText(Color.primary(), Color.primary() + "#" + Color.accent() + (start == end ? start : start + "-" + end)));
    }

    private void displayProfile(JObject json) {
        JSONObject input = json.getInput();
        String displayName = input.remove("__display__").toString();
        input.remove("UUID");
        input.remove("username");
        input.remove("name");
        long place = (long) input.remove("place");
        long points = (long) input.remove(input.containsKey("points") ? "points" : "karma");
        Beezig.api().messagePlayer(StringUtils.linedCenterText(Color.primary(), Color.primary() + Beezig.api().translate("msg.monthly.profile",
            displayName + Color.primary())));
        sendProfileLine("Points", Message.formatNumber(points));
        for(Object o : input.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) o;
            String value = entry.getValue() instanceof Long ? Message.formatNumber((long) entry.getValue()) : entry.getValue().toString();
            String key = WordUtils.capitalize(entry.getKey().replace("_", " ").toLowerCase(Locale.ROOT));
            sendProfileLine(key, value);
        }
        if(input.containsKey("kills") && input.containsKey("deaths")) {
            double kd = (long) input.get("kills") / (double) (long) input.get("deaths");
            sendProfileLine("K/D", Message.ratio(kd));
        }
        if(input.containsKey("victories") && input.containsKey("played")) {
            double wl = 100D * (long) input.get("victories") / (double) (long) input.get("played");
            sendProfileLine("Win Rate", Message.ratio(wl) + "%");
        }
        Beezig.api().messagePlayer(StringUtils.linedCenterText(Color.primary(), Color.primary() + "#" + Color.accent() + place));
    }

    private void sendProfileLine(String key, String value) {
        Beezig.api().messagePlayer("§o " + Color.primary() + key + ": " + Color.accent() + value);
    }

    private CompletableFuture<JObject> getLeaderboard() {
        URL url;
        try {
            url = new URL("https://api.rocco.dev/" + mode + "/monthlies/leaderboard?from=" + (start - 1) + "&to=" + end);
        } catch (MalformedURLException e) {
            ExceptionHandler.catchException(e);
            return null;
        }
        return Downloader.getJsonObject(url);
    }

    private CompletableFuture<JObject> getProfile() {
        return UsernameToUuid.getUUID(player).thenApplyAsync(uuid -> {
            URL url;
            try {
                url = new URL("https://api.rocco.dev/" + mode + "/monthlies/profile/" + uuid);
            } catch (MalformedURLException e) {
                ExceptionHandler.catchException(e);
                return null;
            }
            CompletableFuture<JObject> json = Downloader.getJsonObject(url);
            CompletableFuture<String> name = UUIDUtils.getNameWithOptionalRank(uuid, player, null);
            return CompletableFuture.allOf(json, name).thenApplyAsync(v -> {
                JObject j = json.join();
                j.getInput().put("__display__", name.join());
                return j;
            }).join();
        });
    }

    private static class MonthlyPlace implements Comparable<MonthlyPlace> {
        long place;
        long points;
        String username;

        @Override
        public int compareTo(MonthlyPlace monthlyPlace) {
            return (int) Math.signum(place - monthlyPlace.place);
        }
    }
}
