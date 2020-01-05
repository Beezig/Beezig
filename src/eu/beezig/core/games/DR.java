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
import eu.beezig.core.games.logging.IDailyPoints;
import eu.beezig.core.hiveapi.stuff.dr.DRMap;
import eu.beezig.core.hiveapi.stuff.dr.DRRank;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.dr.DrMonthlyProfile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DR extends GameMode implements IDailyPoints {

    public static HashMap<String, DRMap> mapsPool;

    public DRMap activeMap;
    public String currentMapPB;
    public String currentMapWR;
    public String currentMapWRHolder;
    public String role;
    public String mapTime;
    public int checkpoints;
    public int deaths;
    public int kills;
    public int lastPts;
    public String gameId;
    public int dailyPoints;
    public String rank;
    public DRRank rankObject;
    public List<String> votesToParse = new ArrayList<>();
    public boolean hasVoted;
    public List<String> messagesToSend = new ArrayList<>();
    public List<String> footerToSend = new ArrayList<>();
    private GameLogger logger;

    public DrMonthlyProfile monthly;
    public boolean attemptNew = true;
    public boolean hasLoaded;

    public void reset() {
        this.setState(GameState.FINISHED);
        ActiveGame.reset("dr");
        if (role != null && activeMap != null && logger != null)
            logger.logGame(role, activeMap.getDisplayName(), kills, deaths, gameId, System.currentTimeMillis(), mapTime);
        IHive.genericReset();
        if (The5zigAPI.getAPI().getActiveServer() != null)
            The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
        try {
            this.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean shouldRender(GameState state) {

        if (state == GameState.GAME)
            return true;
        if (state == GameState.PREGAME)
            return true;
        return state == GameState.STARTING;
    }

    @Override
    public String getName() {
        return "DeathRun";
    }

    @Override
    public void initWriter() throws IOException {
        logger = new GameLogger(BeezigMain.mcFile + "/dr/games.csv");
        logger.setHeaders(new String[]{
                "Role",
                "Map",
                "Kills",
                "Deaths",
                "GameID",
                "Timestamp",
                "Time"
        });
        File f = new File(BeezigMain.mcFile + "/dr/dailyPoints/" + BeezigMain.dailyFileName);
        if (!f.exists()) {
            f.createNewFile();
            zeroed();
            return;
        }
        try(FileInputStream stream = new FileInputStream(f)) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line = reader.readLine();
                if (line == null) {
                    zeroed();
                } else {
                    this.dailyPoints = Integer.parseInt(line);
                }
            }
        }
    }

    @Override
    public void zeroed() throws IOException {
        try (PrintWriter dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/dr/dailyPoints/" + BeezigMain.dailyFileName,
                "UTF-8")) {
            dailyPointsWriter.println(0);
        }
    }

    @Override
    public void save() throws IOException {
        try (PrintWriter dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/dr/dailyPoints/" + BeezigMain.dailyFileName,
                "UTF-8")) {
            dailyPointsWriter.println(dailyPoints);
        }
    }
}
