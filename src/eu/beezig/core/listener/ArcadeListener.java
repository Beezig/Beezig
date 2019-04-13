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

package eu.beezig.core.listener;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.beezig.core.games.Arcade;
import eu.beezig.core.hiveapi.APIValues;
import eu.beezig.core.listener.arcade.ArcadeSubListener;
import eu.beezig.core.listener.arcade.subs.ElectricFloorSubListener;
import eu.beezig.core.listener.arcade.subs.SpleggSubListener;
import eu.beezig.core.utils.rpc.DiscordUtils;
import eu.beezig.core.utils.tutorial.SendTutorial;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;

import java.util.ArrayList;
import java.util.List;

public class ArcadeListener extends AbstractGameListener<Arcade> {

    private String game = null;
    private List<ArcadeSubListener> subs = new ArrayList<>();

    public ArcadeListener() {

        subs.add(new SpleggSubListener());
        subs.add(new ElectricFloorSubListener());
    }

    @Override
    public Class<Arcade> getGameMode() {
        return Arcade.class;
    }

    @Override
    public boolean matchLobby(String s) {
        if (!s.startsWith("ARCADE_")) return false;
        game = s.split("_")[1];
        return true;
    }

    @Override
    public void onGameModeJoin(Arcade gameMode) {
        if(game.equals("BP")) {
            getGameListener().switchLobby("BP");
            The5zigAPI.getLogger().info("Connected to BP! - Hive");
            DiscordUtils.updatePresence("Dancing in BlockParty", "Startup", "game_bp");
            return;
        }
        IHive.genericJoin();
        System.out.println("Joined Arcade game: " + game);
        gameMode.setMode(game);
        ActiveGame.set("ARCADE_" + game);
        SendTutorial.send("arcade_join", game);

        new Thread(() -> {
            try {
                Thread.sleep(100L);
                Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
                if (sb != null) {
                    APIValues.ArcadePoints = sb.getLines().get(game.equals("SLAP") ? "§bSlap Points" : (game.equals("SHU") ? "§bTotal Points" : "§bPoints"));
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


    }

    @Override
    public boolean onServerChat(Arcade gameMode, String message) {
        subs.forEach(arcadeSubListener -> arcadeSubListener.onServerChat(gameMode, message));
        return false;
    }

    @Override
    public void onServerConnect(Arcade gameMode) {
        gameMode.resetInternally();
    }
}
