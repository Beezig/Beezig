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

package eu.beezig.core.games;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.BeezigMain;
import eu.beezig.core.IHive;
import eu.beezig.core.games.logging.GameLogger;
import eu.beezig.core.hiveapi.APIValues;
import eu.beezig.core.hiveapi.stuff.bed.BEDRank;
import eu.beezig.core.utils.StreakUtils;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.bed.BedMonthlyProfile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BED extends GameMode {

    public static char[] NUMBERS = {' ', '➊', '➋', '➌', '➍', '➎', '➏', '➐', '➑', '➒'};
    public static String activeMap;
    public static Long lastRecordsPoints = null;
    public static String mode = "";
    public static int kills;
    public static int deaths;
    public static int pointsCounter;
    public static int bedsDestroyed;
    public static int teamsLeft;
    public static BedMonthlyProfile monthly;
    public static boolean attemptNew = true;
    public static boolean hasLoaded = false;
    public static boolean inGame;
    public static boolean hasWon;
    public static int winstreak;
    public static int bestStreak;
    public static int apiKills;
    public static int apiDeaths;
    public static int ironGen;

    // Generators (0: None, 1: Level 1, 2: Level 2, 3: Level 3)
    public static int goldGen;
    public static int diamondGen;
    public static int dailyPoints;
    public static double apiKdr;
    public static double gameKdr;
    public static String rank;
    public static BEDRank rankObject;
    public static List<String> votesToParse = new ArrayList<>();
    public static boolean hasVoted = false;
    public static List<String> messagesToSend = new ArrayList<>();
    public static List<String> footerToSend = new ArrayList<>();
    public static String gameId;
    private static GameLogger logger;
    private static PrintWriter dailyPointsWriter;
    private static String dailyPointsName;

    public static void initDailyPointsWriter() throws IOException {
        logger = new GameLogger(BeezigMain.mcFile + "/bedwars/games.csv");
        logger.setHeaders(new String[]{
                "Points",
                "Mode",
                "Map",
                "Kills",
                "Deaths",
                "Beds",
                "Victory?",
                "Timestamp",
                "GameID"
        });

        File f = new File(BeezigMain.mcFile + "/bedwars/dailyPoints/" + dailyPointsName);
        if (!f.exists()) {
            f.createNewFile();
            initPointsWriterWithZero();
            return;
        }

        FileInputStream stream = new FileInputStream(f);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = reader.readLine();
        if (line == null) {
            initPointsWriterWithZero();
            stream.close();
            return;
        } else {
            BED.dailyPoints = Integer.parseInt(line);
        }
        stream.close();
        reader.close();


    }

    private static void initPointsWriterWithZero() throws FileNotFoundException, UnsupportedEncodingException {
        dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/bedwars/dailyPoints/" + dailyPointsName, "UTF-8");
        dailyPointsWriter.println(0);

        dailyPointsWriter.close();


    }

    public static void setDailyPointsFileName(String newName) {
        dailyPointsName = newName;
    }

    private static void saveDailyPoints() {
        try {
            dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/bedwars/dailyPoints/" + dailyPointsName, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dailyPointsWriter.println(dailyPoints);
        dailyPointsWriter.flush();
        dailyPointsWriter.close();
    }

    // (§8▍ §3§lBed§b§lWars§8 ▏ §e§lVote for a map:)

    public static void reset(BED gm) {

        gm.setState(GameState.FINISHED);
        if (inGame && !hasWon && !mode.equals("Double Fun")) {
            boolean wasBest = winstreak >= bestStreak;
            System.out.println("Lost!");
            winstreak = 0;
            StreakUtils.resetWinstreak("bed", wasBest);
        }
        if (inGame && logger != null)
            logger.logGame(pointsCounter + "", mode, activeMap, kills + "", deaths + "", bedsDestroyed + "", hasWon ? "Yes" : "No",
                    System.currentTimeMillis() + "", gameId);
        gameId = null;
        hasWon = false;
        inGame = false;
        BED.mode = "";
        BED.activeMap = null;
        BED.hasVoted = false;
        BED.kills = 0;
        BED.deaths = 0;
        BED.bedsDestroyed = 0;
        BED.pointsCounter = 0;
        BED.teamsLeft = 0;
        BED.votesToParse.clear();
        apiKills = 0;
        apiDeaths = 0;
        apiKdr = 0;
        gameKdr = 0;
        ironGen = 0;
        goldGen = 0;
        diamondGen = 0;
        hasLoaded = false;
        ActiveGame.reset("bed");

        IHive.genericReset();
        if (The5zigAPI.getAPI().getActiveServer() != null)
            The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
        saveDailyPoints();
    }

    public static boolean shouldRender(GameState state) {

        if (state == GameState.GAME)
            return true;
        if (state == GameState.PREGAME)
            return true;
        return state == GameState.STARTING;
    }

    public static void updateTeamsLeft() {
        Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();

        for (Map.Entry<String, Integer> entry : sb.getLines().entrySet()) {

            if (entry.getValue() == 13) {
                BED.teamsLeft = ChatColor.stripColor(entry.getKey()).toCharArray().length;
            }
        }
    }

    public static String updateResources() {
        StringBuilder sb = new StringBuilder();
        int ironIngots = The5zigAPI.getAPI().getItemCount("iron_ingot");
        int goldIngots = The5zigAPI.getAPI().getItemCount("gold_ingot");
        int diamonds = The5zigAPI.getAPI().getItemCount("diamond");
        int emeralds = The5zigAPI.getAPI().getItemCount("emerald");
        if (ironIngots != 0)
            sb.append(ironIngots).append(" Iron / ");
        if (goldIngots != 0)
            sb.append(goldIngots).append(" Gold / ");
        if (diamonds != 0)
            sb.append(diamonds).append(" Diamonds / ");
        if (emeralds != 0)
            sb.append(emeralds).append(" Emeralds / ");

        return sb.toString().trim();
    }

    public static void updateRank() {
        BED.rank = BEDRank.getRank(APIValues.BEDpoints).getName()
                .replaceAll(ChatColor.stripColor(BEDRank.getRank(APIValues.BEDpoints).getName()), "")
                + BED.NUMBERS[BEDRank.getRank(APIValues.BEDpoints).getLevel((int) APIValues.BEDpoints)] + " "
                + BEDRank.getRank((int) APIValues.BEDpoints).getName();
        BED.rankObject = BEDRank.getRank(APIValues.BEDpoints);
    }

    public static void updateKdr() {
        apiKdr = (double) apiKills / (apiDeaths == 0 ? 1 : apiDeaths);
        gameKdr = (double) (kills + apiKills) / (deaths + apiDeaths == 0 ? 1 : apiDeaths + deaths);
    }

    public static void updateMode() {
        // ffs mode is so annoying
        Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
        The5zigAPI.getLogger().info(sb.getTitle());
        if (sb != null && sb.getTitle().contains("BED ")) {
            BED.mode = "Solo";
        }
        if (sb != null && sb.getTitle().contains("BEDD ")) {
            BED.mode = "Duo";
        }
        if (sb != null && sb.getTitle().contains("BEDT ")) {
            BED.mode = "Teams";
        }
        if (sb != null && sb.getTitle().contains("BEDX ")) {
            BED.mode = "Double Fun";
        }
    }

    @Override
    public String getName() {
        return "Bedwars";
    }

}
