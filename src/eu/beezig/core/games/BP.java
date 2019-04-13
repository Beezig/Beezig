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
import eu.beezig.core.hiveapi.stuff.bp.BPRank;
import eu.beezig.core.utils.ws.Connector;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import pw.roccodev.beezig.hiveapi.wrapper.monthly.bp.BpMonthlyProfile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BP extends GameMode {

    public static List<String> messagesToSend = new ArrayList<>();
    public static List<String> footerToSend = new ArrayList<>();
    public static String song;
    public static String artist;
    public static int gamePts;
    public static int dailyPoints;
    public static String rank;
    public static BPRank rankObject;
    public static List<String> votesToParse = new ArrayList<>();
    private static GameLogger logger;
    private static PrintWriter dailyPointsWriter;
    private static String dailyPointsName;

    public static BpMonthlyProfile monthly;
    public static boolean attemptNew = true;
    public static boolean hasLoaded = false;

    public static void initDailyPointsWriter() throws IOException {

        logger = new GameLogger(BeezigMain.mcFile + "/bp/games.csv");
        logger.setHeaders(new String[]{
                "Points",
                "Song",
                "Timestamp"
        });

        File f = new File(BeezigMain.mcFile + "/bp/dailyPoints/" + dailyPointsName);
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
            BP.dailyPoints = Integer.parseInt(line);
        }
        stream.close();
        reader.close();


    }

    private static void initPointsWriterWithZero() throws FileNotFoundException, UnsupportedEncodingException {
        dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/bp/dailyPoints/" + dailyPointsName, "UTF-8");
        dailyPointsWriter.println(0);

        dailyPointsWriter.close();


    }

    public static void setDailyPointsFileName(String newName) {
        dailyPointsName = newName;
    }

    private static void saveDailyPoints() {
        try {
            dailyPointsWriter = new PrintWriter(BeezigMain.mcFile + "/bp/dailyPoints/" + dailyPointsName, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dailyPointsWriter.println(dailyPoints);
        dailyPointsWriter.flush();
        dailyPointsWriter.close();
    }

    public static void reset(BP gameMode) {

        if (gameMode.getState() == GameState.GAME && logger != null)
            logger.logGame(gamePts + "", song, System.currentTimeMillis() + "");
        gameMode.setState(GameState.FINISHED);
        song = null;
        artist = null;

        gamePts = 0;
        hasLoaded = false;
        votesToParse.clear();
        ActiveGame.reset("bp");
        IHive.genericReset();
        if (The5zigAPI.getAPI().getActiveServer() != null)
            The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
        saveDailyPoints();
        Connector.leaveLobbyBP();
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
        // TODO Auto-generated method stub
        return "BlockParty";
    }

}
