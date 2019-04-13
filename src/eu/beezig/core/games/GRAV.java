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
import eu.beezig.core.IHive;
import eu.beezig.core.hiveapi.stuff.grav.GRAVRank;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GRAV extends GameMode {

    public static HashMap<String, String> mapsPool = new HashMap<>();

    public static List<String> messagesToSend = new ArrayList<>();
    public static List<String> footerToSend = new ArrayList<>();
    public static boolean hasVoted = false;
    public static HashMap<Integer, ArrayList<String>> mapsToParse = new HashMap<>();

    public static long gamePoints;


    public static int fails;
    public static int failsCache;

    public static int apiGamesPlayed, apiVictories;

    public static String rank;
    public static GRAVRank rankObject;

    public static ArrayList<String> maps = new ArrayList<>();
    public static HashMap<String, Double> mapPBs = new HashMap<>();
    public static HashMap<Integer, String> toDisplay = new HashMap<>();
    public static HashMap<Integer, String> toDisplayWithFails = new HashMap<>();
    public static int currentMap = -1;


    public static void reset(GRAV gameMode) {

        gameMode.setState(GameState.FINISHED);

        GRAV.messagesToSend.clear();
        GRAV.footerToSend.clear();
        GRAV.mapsToParse.clear();

        GRAV.hasVoted = false;
        currentMap = -1;
        gamePoints = 0;
        fails = 0;
        failsCache = 0;
        toDisplay.clear();
        toDisplayWithFails.clear();
        maps.clear();
        mapPBs.clear();
        ActiveGame.reset("grav");
        IHive.genericReset();
        if (The5zigAPI.getAPI().getActiveServer() != null)
            The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");
    }

    public static boolean shouldRender(GameState state) {

        if (state == GameState.GAME) return true;
        if (state == GameState.PREGAME) return true;
        return state == GameState.STARTING;
    }


    @Override
    public String getName() {
        return "Gravity";
    }

}
