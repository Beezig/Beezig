package eu.beezig.core.calc.lb;

import eu.beezig.core.Beezig;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;
import eu.beezig.hiveapi.wrapper.mojang.UsernameToUuid;
import eu.beezig.hiveapi.wrapper.utils.download.Downloader;
import eu.beezig.hiveapi.wrapper.utils.json.JObject;
import org.apache.commons.lang3.text.WordUtils;
import org.json.simple.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MonthlyCalculator {
    private String mode, player;
    private int start, end;

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
                e.printStackTrace();
                return null;
            });
        }
        else {
            calculateLeaderboard().thenAcceptAsync(this::displayLeaderboard).exceptionally(e -> {
                Message.error(Message.translate("error.leaderboard"));
                e.printStackTrace();
                return null;
            });
        }
    }

    private CompletableFuture<List<MonthlyPlace>> calculateLeaderboard() {
        return getLeaderboard().thenApplyAsync(json -> {
            List<MonthlyPlace> places = new ArrayList<>();
            for(Object o : json.getInput().entrySet()) {
                Map.Entry<String, JSONObject> entry = (Map.Entry<String, JSONObject>) o;
                MonthlyPlace place = new MonthlyPlace();
                place.place = (long) entry.getValue().get("place");
                place.points = (long) entry.getValue().get(entry.getValue().containsKey("points") ? "points" : "karma");
                place.username = entry.getValue().get(entry.getValue().containsKey("username") ? "username" : "name").toString();
                places.add(place);
            }
            return places;
        });
    }

    private void displayLeaderboard(List<MonthlyPlace> lb) {
        Beezig.api().messagePlayer(StringUtils.linedCenterText(Color.primary(), Color.primary() + Beezig.api().translate("msg.leaderboard",
            Color.accent() + mode.toUpperCase(Locale.ROOT) + Color.primary())));
        for(MonthlyPlace place : lb) {
            Beezig.api().messagePlayer(Color.primary() + " #" + Color.accent() + place.place
                + Color.primary() + "§7 ▏ " + Color.accent() + Message.formatNumber(place.points) + "§7 - "
                + Color.accent() + place.username);
        }
        Beezig.api().messagePlayer(StringUtils.linedCenterText(Color.primary(), Color.primary() + "#" + Color.accent() + (start == end ? start : start + "-" + end)));
    }

    private void displayProfile(JObject json) {
        JSONObject input = json.getInput();
        input.remove("UUID");
        input.remove("username");
        input.remove("name");
        long place = (long) input.get("place");
        input.remove("place");
        long points = (long) input.get(input.containsKey("points") ? "points" : "karma");
        input.remove("points");
        input.remove("karma");
        Beezig.api().messagePlayer(StringUtils.linedCenterText(Color.primary(), Color.primary() + Beezig.api().translate("msg.monthly.profile",
            Color.accent() + player + Color.primary())));
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
            e.printStackTrace();
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
                e.printStackTrace();
                return null;
            }
            return Downloader.getJsonObject(url).join();
        });
    }

    private static class MonthlyPlace {
        long place;
        long points;
        String username;
    }
}
