package eu.beezig.core.calc.lb;

import eu.beezig.core.Beezig;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;
import eu.beezig.hiveapi.wrapper.game.Game;
import eu.beezig.hiveapi.wrapper.game.leaderboard.GameLeaderboard;
import eu.beezig.hiveapi.wrapper.game.leaderboard.LeaderboardPlace;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class LeaderboardCalculator {
    private String mode;
    private int start, end;

    public LeaderboardCalculator(String mode, int start, int end) {
        this.mode = mode;
        this.start = start;
        this.end = end;
    }

    public void run() {
        calculate().thenAcceptAsync(this::display).exceptionally(e -> {
            Message.error(Message.translate("error.leaderboard"));
            e.printStackTrace();
            return null;
        });
    }

    private CompletableFuture<List<LeaderboardPlace>> calculate() {
        return new Game(mode, null).getLeaderboard(start - 1, end).thenApplyAsync(GameLeaderboard::getPlayers);
    }

    private void display(List<LeaderboardPlace> lb) {
        Beezig.api().messagePlayer(StringUtils.linedCenterText(Color.primary(), Color.primary() + Beezig.api().translate("msg.leaderboard",
            Color.accent() + mode.toUpperCase(Locale.ROOT) + Color.primary())));
        for(LeaderboardPlace place : lb) {
            String key = place.containsKey("points") ? "points" : (place.containsKey("total_points") ? "total_points" :
                (place.containsKey("karma") ? "karma" : "victories"));
            Beezig.api().messagePlayer(Color.primary() + " #" + Color.accent() + place.getHumanPlace()
                + Color.primary() + "§7 ▏ " + Color.accent() + Message.formatNumber((long) place.get(key)) + "§7 - "
                + Color.accent() + place.get("username"));
        }
        Beezig.api().messagePlayer(StringUtils.linedCenterText(Color.primary(), Color.primary() + "#" + Color.accent() + (start == end ? start : start + "-" + end)));
    }
}
