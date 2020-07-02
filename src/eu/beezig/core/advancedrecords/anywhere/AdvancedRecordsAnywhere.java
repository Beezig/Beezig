/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
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

package eu.beezig.core.advancedrecords.anywhere;

import eu.beezig.core.Log;
import eu.beezig.core.advancedrecords.anywhere.statistic.PercentRatioStatistic;
import eu.beezig.core.advancedrecords.anywhere.statistic.RatioRecordsStatistic;
import eu.beezig.core.advancedrecords.anywhere.statistic.RecordsStatistic;
import eu.beezig.core.advancedrecords.anywhere.statistic.TimeStatistic;
import eu.beezig.core.advancedrecords.anywhere.util.ArcadeGamemodeBuilder;
import eu.beezig.core.advancedrecords.anywhere.util.GamemodeBuilder;
import eu.beezig.core.settings.Setting;
import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;
import pw.roccodev.beezig.hiveapi.wrapper.player.GameStats;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;

import java.util.ArrayList;
import java.util.List;

public class AdvancedRecordsAnywhere {

    // A bunch of string constants
    private static final String PTS = "Points";
    private static final String GM = "Games Played";
    private static final String V = "Victories";
    private static final String K = "Kills";
    private static final String D = "Deaths";
    private static final String KD = "K/D";
    private static final String WR = "Win Rate";
    private static List<GamemodeStatistics> gamemodes = new ArrayList<>();

    public static GamemodeStatistics getGamemode(String query) {
        for (GamemodeStatistics gamemode : gamemodes) {
            if (query.matches("(?i:" + gamemode.getGamemode() + ")")) return gamemode;
        }
        return null;
    }

    public static void register() {

        gamemodes.add(new GamemodeBuilder("BED(.*)") // BED, BEDS, BEDT etc.
                .addStatistic(new RecordsStatistic(PTS, "total_points"))
                .addStatistic(new RecordsStatistic(V, "victories"))
                .addStatistic(new RecordsStatistic(GM, "games_played"))
                .addStatistic(new RecordsStatistic(K, "kills"))
                .addStatistic(new RecordsStatistic(D, "deaths"))
                .addStatistic(new RecordsStatistic("Beds Destroyed", "beds_destroyed"))
                .addStatistic(new RecordsStatistic("Teams Eliminated", "teams_eliminated"))
                .addStatistic(new RatioRecordsStatistic(KD, "kills", "deaths", Setting.SHOW_RECORDS_KDR))
                .addStatistic(new PercentRatioStatistic(WR, "victories", "games_played", Setting.SHOW_RECORDS_WINRATE))
                .build());

        gamemodes.add(new GamemodeBuilder("SKY")
                .addStatistic(new RecordsStatistic(PTS, "total_points"))
                .addStatistic(new RecordsStatistic(V, "victories"))
                .addStatistic(new RecordsStatistic(GM, "gamesplayed"))
                .addStatistic(new RecordsStatistic(K, "kills"))
                .addStatistic(new RecordsStatistic(D, "deaths"))
                .addStatistic(new RatioRecordsStatistic(KD, "kills", "deaths", Setting.SHOW_RECORDS_KDR))
                .addStatistic(new PercentRatioStatistic(WR, "victories", "gamesplayed", Setting.SHOW_RECORDS_WINRATE))
                .build());

        gamemodes.add(new GamemodeBuilder("TIMV")
                .addStatistic(new RecordsStatistic("Karma", "total_points"))
                .addStatistic(new RecordsStatistic("Role Points", "role_points"))
                .addStatistic(new RecordsStatistic("Innocent Points", "i_points"))
                .addStatistic(new RecordsStatistic("Traitor Points", "t_points"))
                .addStatistic(new RecordsStatistic("Detective Points", "d_points"))
                .addStatistic(new RecordsStatistic("Karma Record", "most_points", Setting.TIMV_SHOW_MOSTPOINTS))
                .addStatistic(new RatioRecordsStatistic("Karma/Role Points", "total_points", "role_points", Setting.TIMV_SHOW_KRR))
                .addStatistic(new PercentRatioStatistic("Traitor Share", "t_points", "role_points", Setting.TIMV_SHOW_TRAITORRATIO))
                .build());

        gamemodes.add(new GamemodeBuilder("HIDE")
                .addStatistic(new RecordsStatistic(PTS, "total_points"))
                .addStatistic(new RecordsStatistic(V, "victories"))
                .addStatistic(new RecordsStatistic(GM, "gamesplayed"))
                .addStatistic(new RecordsStatistic("Kills as Hider", "hiderkills"))
                .addStatistic(new RecordsStatistic("Kills as Seeker", "seekerkills"))
                .addStatistic(new RecordsStatistic(D, "deaths"))
                .addStatistic(new TimeStatistic("Time Alive", "timealive"))
                .addStatistic(new PercentRatioStatistic(WR, "victories", "gamesplayed", Setting.SHOW_RECORDS_WINRATE))
                .build());


        gamemodes.add(new GamemodeBuilder("CAI")
                .addStatistic(new RecordsStatistic(PTS, "total_points"))
                .addStatistic(new RecordsStatistic(V, "victories"))
                .addStatistic(new RecordsStatistic(GM, "gamesplayed"))
                .addStatistic(new RecordsStatistic("Captures", "captures"))
                .addStatistic(new RecordsStatistic("Captured", "captured"))
                .addStatistic(new RecordsStatistic("Catches", "catches"))
                .addStatistic(new RecordsStatistic("Caught", "caught"))
                .addStatistic(new PercentRatioStatistic(WR, "victories", "gamesplayed", Setting.SHOW_RECORDS_WINRATE))
                .build());

        gamemodes.add(new GamemodeBuilder("GNT(.*)")
                .addStatistic(new RecordsStatistic(PTS, "total_points"))
                .addStatistic(new RecordsStatistic(V, "victories"))
                .addStatistic(new RecordsStatistic(GM, "games_played"))
                .addStatistic(new RecordsStatistic(K, "kills"))
                .addStatistic(new RecordsStatistic(D, "deaths"))
                .addStatistic(new RecordsStatistic("Beasts Slain", "beasts_slain"))
                .addStatistic(new RatioRecordsStatistic(KD, "kills", "deaths", Setting.SHOW_RECORDS_KDR))
                .addStatistic(new PercentRatioStatistic(WR, "victories", "games_played", Setting.SHOW_RECORDS_WINRATE))
                .build());

        gamemodes.add(new GamemodeBuilder("DR")
                .addStatistic(new RecordsStatistic(PTS, "total_points"))
                .addStatistic(new RecordsStatistic(V, "victories"))
                .addStatistic(new RecordsStatistic(GM, "games_played"))
                .addStatistic(new RecordsStatistic(K, "kills"))
                .addStatistic(new RecordsStatistic(D, "deaths"))
                .addStatistic(new RecordsStatistic("Checkpoints", "totalcheckpoints"))
                .addStatistic(new PercentRatioStatistic(WR, "victories", "games_played", Setting.SHOW_RECORDS_WINRATE))
                .build());

        gamemodes.add(new GamemodeBuilder("MIMV")
                .addStatistic(new RecordsStatistic("Karma", "total_points"))
                .addStatistic(new RecordsStatistic(V, "victories"))
                .addStatistic(new RecordsStatistic(GM, "games_played"))
                .addStatistic(new RecordsStatistic(K, "kills"))
                .addStatistic(new RecordsStatistic(D, "deaths"))
                .addStatistic(new RatioRecordsStatistic(KD, "kills", "deaths", Setting.SHOW_RECORDS_KDR))
                .addStatistic(new PercentRatioStatistic(WR, "victories", "games_played", Setting.SHOW_RECORDS_WINRATE))
                .build());

        gamemodes.add(new GamemodeBuilder("SGN")
                .addStatistic(new RecordsStatistic(PTS, "total_points"))
                .addStatistic(new RecordsStatistic(V, "victories"))
                .addStatistic(new RecordsStatistic(GM, "games_played"))
                .addStatistic(new RecordsStatistic(K, "kills"))
                .addStatistic(new RecordsStatistic(D, "deaths"))
                .addStatistic(new RecordsStatistic("Crates Opened", "crates_opened"))
                .addStatistic(new RecordsStatistic("Points Record", "most_points"))
                .addStatistic(new RatioRecordsStatistic(KD, "kills", "deaths", Setting.SHOW_RECORDS_KDR))
                .addStatistic(new PercentRatioStatistic(WR, "victories", "games_played", Setting.SHOW_RECORDS_WINRATE))
                .build());

        gamemodes.add(new GamemodeBuilder("GRAV")
                .addStatistic(new RecordsStatistic(PTS, "points"))
                .addStatistic(new RecordsStatistic(V, "victories"))
                .addStatistic(new RecordsStatistic(GM, "gamesplayed"))
                .addStatistic(new PercentRatioStatistic(WR, "victories", "gamesplayed", Setting.SHOW_RECORDS_WINRATE))
                .build());

        gamemodes.add(new GamemodeBuilder("LAB")
                .addStatistic(new RecordsStatistic("Atoms", "total_points"))
                .addStatistic(new RecordsStatistic(V, "victories"))
                .addStatistic(new RecordsStatistic(GM, "gamesplayed"))
                .addStatistic(new PercentRatioStatistic(WR, "victories", "gamesplayed", Setting.SHOW_RECORDS_WINRATE))
                .build());

        gamemodes.add(new GamemodeBuilder("BP")
                .addStatistic(new RecordsStatistic(PTS, "total_points"))
                .addStatistic(new RecordsStatistic(V, "victories"))
                .addStatistic(new RecordsStatistic("Placings", "total_placing"))
                .addStatistic(new RecordsStatistic(GM, "games_played"))
                .addStatistic(new PercentRatioStatistic(WR, "victories", "games_played", Setting.SHOW_RECORDS_WINRATE))
                .build());


        gamemodes.add(new ArcadeGamemodeBuilder("CR", "total_points", "victories", "gamesplayed")
                .build());
        gamemodes.add(new ArcadeGamemodeBuilder("MM", "points", "victories", "gamesplayed")
                .build());
        gamemodes.add(new ArcadeGamemodeBuilder("OITC", "total_points", "victories", "gamesplayed")
                .build());
        gamemodes.add(new ArcadeGamemodeBuilder("RR", "points", "victories", "gamesplayed")
                .build());
        gamemodes.add(new ArcadeGamemodeBuilder("SLAP", "points", "victories", "gamesplayed")
                .build());
        gamemodes.add(new ArcadeGamemodeBuilder("HERO", "total_points", "victories", "games_played")
                .build());
        gamemodes.add(new ArcadeGamemodeBuilder("SPL", "total_points", "victories", "games_played")
                .build());
        gamemodes.add(new ArcadeGamemodeBuilder("HB", "points", null, null)
                .build());
        gamemodes.add(new ArcadeGamemodeBuilder("DRAW", "total_points", "victories", "gamesplayed")
                .build());
        gamemodes.add(new ArcadeGamemodeBuilder("SP", "points", "victories", "gamesplayed")
                .build());
        gamemodes.add(new ArcadeGamemodeBuilder("BD", "total_points", null, "games_played")
                .build());
        gamemodes.add(new ArcadeGamemodeBuilder("EF", "points", "victories", "gamesplayed")
                .build());


    }

    public static void run(String player, String mode) {
        GamemodeStatistics gm = getGamemode(mode);
        if (gm == null) {
            The5zigAPI.getAPI().messagePlayer(Log.error + "Game mode not found or not supported.");
            return;
        }
        The5zigAPI.getAPI().messagePlayer(Log.info + "Running Advanced Records...");
        new Thread(() -> {

            GameStats apiObj = new GameStats(player, mode.toUpperCase());
            try {
                apiObj.getSource().fetch();
            } catch (Exception e) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "Player not found.");
                return;
            }
            JSONObject jObj = apiObj.getSource().getInput();
            HivePlayer parent = apiObj.getPlayer();
            The5zigAPI.getAPI().messagePlayer("§f          §6§m                  §f " + parent.getUsername() + "'s Stats §6§m                  ");

            for (RecordsStatistic stat : gm.getStatistics()) {
                if (stat.getSetting() == null || stat.getSetting().getValue())
                    The5zigAPI.getAPI().messagePlayer(" §a§3" + stat.getKey() + ":§b " + stat.getValue(jObj));
            }

            The5zigAPI.getAPI().messagePlayer("§f                      §6§m                  §6§m                  ");


        }).start();

    }


}
