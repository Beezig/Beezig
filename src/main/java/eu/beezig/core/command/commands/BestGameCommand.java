package eu.beezig.core.command.commands;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import eu.beezig.core.Beezig;
import eu.beezig.core.command.Command;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.UUIDUtils;
import eu.beezig.core.util.text.Message;
import eu.beezig.hiveapi.wrapper.exception.ProfileNotFoundException;
import eu.beezig.hiveapi.wrapper.game.Game;
import eu.beezig.hiveapi.wrapper.game.leaderboard.LeaderboardPlace;
import eu.beezig.hiveapi.wrapper.mojang.UsernameToUuid;
import eu.beezig.hiveapi.wrapper.utils.download.Downloader;
import eu.beezig.hiveapi.wrapper.utils.download.UrlBuilder;
import eu.beezig.hiveapi.wrapper.utils.json.JObject;
import eu.the5zig.mod.util.component.MessageComponent;
import eu.the5zig.mod.util.component.style.MessageAction;
import org.apache.commons.lang3.text.WordUtils;
import org.json.simple.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * /bestgame - compares someone's stats to the average of the Top 20 for every mode.
 * The results are cached so that subsequent requests are quicker.
 */
public class BestGameCommand implements Command {
    private AsyncCache<String, Double> lbCache = Caffeine.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).buildAsync();

    @Override
    public String getName() {
        return "bestgame";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/bestgame", "/maingame"};
    }

    @Override
    public boolean execute(String[] args) {
        if(!ServerHive.isCurrent()) return false;
        boolean showAll = args.length > 1;
        String target = args.length == 0 ? UUIDUtils.strip(Beezig.user().getId()) : args[0];
        Message.info(Message.translate("msg.bestgame.loading"));
        getGames().thenApplyAsync(games -> {
            Map<GameInfo, CompletableFuture<Double>> averages = new HashMap<>();
            for(GameInfo game : games) averages.put(game, getAverage(game));
            CompletableFuture.allOf(averages.values().toArray(new CompletableFuture[0])).join();
            List<CompletableFuture<BestGameInfo>> results = new ArrayList<>();
            for(GameInfo game : games) results.add(calculateForPlayer(target, game, averages.get(game).join()));
            CompletableFuture.allOf(results.toArray(new CompletableFuture[0])).join();
            TreeSet<BestGameInfo> result = new TreeSet<>();
            for(CompletableFuture<BestGameInfo> future : results) result.add(future.join());
            return result;
        }).thenAcceptAsync(games -> {
            if(games == null) throw new ProfileNotFoundException();
            if(showAll) {
                for(BestGameInfo info : games) {
                    Message.info(Color.primary() + info.game.name + ": " + getPercentageDisplay(info));
                }
            }
            BestGameInfo leastPlayed = games.first();
            BestGameInfo mostPlayed = games.last();
            Message.bar();
            Message.info(Beezig.api().translate("msg.bestgame.least", Color.accent() + leastPlayed.game.name + Color.primary() + ": " + getPercentageDisplay(leastPlayed)));
            Message.info(Beezig.api().translate("msg.bestgame.most", Color.accent() + mostPlayed.game.name + Color.primary() + ": " + getPercentageDisplay(mostPlayed)));
            MessageComponent component = new MessageComponent(Message.infoPrefix() + Message.translate("msg.bestgame.hint"));
            component.getStyle().setOnHover(new MessageAction(MessageAction.Action.SHOW_TEXT, new MessageComponent(Color.accent() + WordUtils.wrap(Message.translate("msg.bestgame.hint.desc"), 50, "\n" + Color.accent(), false))));
            Beezig.api().messagePlayerComponent(component, false);
            Message.bar();
        }).exceptionally(e -> {
            ExceptionHandler.catchException(e);
            return null;
        });
        return true;
    }

    private String getPercentageDisplay(BestGameInfo game) {
        if(!game.hasPlayed) return "§8" + Message.translate("msg.bestgame.never");
        if(game.percent > 1) return String.format("§a+%s%%", Message.ratio((game.percent - 1) * 100));
        else if(game.percent < 1) return String.format("§c-%s%%", Message.ratio(100 * (1 - game.percent)));
        else return "§7=";
    }

    private CompletableFuture<Double> getAverage(GameInfo game) {
        return lbCache.get(game.id, (k, exec) -> new Game(k, null).getLeaderboard(0, 20).thenApplyAsync(lb -> {
            LeaderboardPlace first = lb.getPlayers().get(0);
            String place;
            if(game.shouldUseVictories()) place = "victories";
            else place = first.containsKey("points") ? "points" : (first.containsKey("total_points") ? "total_points" :
                (first.containsKey("karma") ? "karma" : "victories"));
            return lb.getPlayers().stream().filter(p -> p.get(place) != null).mapToInt(p -> (int)(long) p.get(place)).average().orElse(0);
        }));
    }

    private CompletableFuture<List<GameInfo>> getGames() {
        try {
            return Downloader.getJsonObject(new URL("https://api.hivemc.com/v1/game")).thenApplyAsync(json -> {
                List<GameInfo> result = new ArrayList<>();
                for(Object o : json.getInput().entrySet()) {
                    Map.Entry<String, Object> entry = (Map.Entry<String, Object>) o;
                    result.add(new GameInfo(entry.getKey(), entry.getValue().toString()));
                }
                return result;
            });
        } catch (MalformedURLException ignored) {
            // Unreachable
        }
        return null;
    }

    private CompletableFuture<BestGameInfo> calculateForPlayer(String uuid, GameInfo game, double average) {
        return getStats(uuid, game.id).thenApplyAsync(profile -> {
            JSONObject input = profile.getInput();
            String place;
            if(game.shouldUseVictories()) place = "victories";
            else place = input.containsKey("points") ? "points" : (input.containsKey("total_points") ? "total_points" :
                (input.containsKey("karma") ? "karma" : "victories"));
            return new BestGameInfo(game, profile.getInt(place) / average);
        }).exceptionally(e -> {
            // Profile not found, etc.
            BestGameInfo never = new BestGameInfo(game, 0);
            never.hasPlayed = false;
            return never;
        });
    }

    private CompletableFuture<JObject> getStats(String username, String shortcode) {
        if(username.length() < 16) {
            return UsernameToUuid.getUUID(username).thenApplyAsync(uuid -> JObject.get(new UrlBuilder().hive().player(uuid, shortcode).build()).join());
        }
        return JObject.get(new UrlBuilder().hive().player(username, shortcode).build());
    }

    private static class GameInfo {
        private final String id, name;

        private GameInfo(String id, String name) {
            this.id = id;
            this.name = name;
        }

        private boolean shouldUseVictories() {
            return "OITC".equalsIgnoreCase(id);
        }
    }

    private static class BestGameInfo implements Comparable<BestGameInfo> {
        private final GameInfo game;
        private final double percent;
        private boolean hasPlayed = true;

        BestGameInfo(GameInfo game, double percent) {
            this.game = game;
            this.percent = percent;
        }

        @Override
        public int compareTo(BestGameInfo bestGameInfo) {
            if(hasPlayed && bestGameInfo.hasPlayed) return (int) Math.signum(percent - bestGameInfo.percent);
            else if(hasPlayed) return 1;
            else if(bestGameInfo.hasPlayed) return -1;
            return (int) Math.signum(percent - bestGameInfo.percent);
        }
    }
}
