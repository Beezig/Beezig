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
import eu.beezig.core.games.TIMV;
import eu.beezig.core.hiveapi.APIValues;
import eu.beezig.core.utils.rpc.DiscordUtils;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HiveListener extends AbstractGameListener<GameMode> {


    @Override
    public Class<GameMode> getGameMode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean matchLobby(String lobby) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onGameModeJoin(GameMode gameMode) {


    }


    @Override
    public boolean onServerChat(GameMode gameMode, String message) {
        if (message.contains("§eGold Medal Awarded!")) {
            APIValues.medals++;
        } else if (ChatColor.stripColor(message).contains("▍ Tokens ▏ You earned")) {

            String[] data = ChatColor.stripColor(message).replaceAll("▍ Tokens ▏ You earned", "").split("tokens");
            int tokens = Integer.parseInt(data[0].trim());

            APIValues.tokens += tokens;

        } else if (message.contains("§b EXTRA tokens this round!")) {
            //§8▍ §3§lBed§b§lWars§8 ▏ §bThanks to the §dultimate§b member §dGryffin§b you gained §a25§b EXTRA tokens this round!
            //idk if thats how it works
            APIValues.tokens += Integer.parseInt(ChatColor.stripColor(message.split("EXTRA")[0].split("you gained ")[1].trim()));
        }
        return false;
    }

    @Override
    public void onMatch(GameMode gameMode, String key, IPatternResult match) {
        if(gameMode != null) return;
        if (key == null)
            return;
        if (key.equals(TIMV.joinMessage)) {
            getGameListener().switchLobby("TIMV");

            The5zigAPI.getLogger().info("Connected to TIMV! -Hive");
            DiscordUtils.updatePresence("Investigating in Trouble in Mineville", "In Lobby", "game_timv");
        }
        switch (key) {
//            TODO ActiveMap for fallback strings all gamemodes
            case "timv.welcome":
            case "timv.fallback":
                getGameListener().switchLobby("TIMV");

                The5zigAPI.getLogger().info("Connected to TIMV! -Hive");
                DiscordUtils.updatePresence("Investigating in Trouble in Mineville", "In Lobby", "game_timv");
                if (key.equals("timv.fallback")) {

                    //The winning map is Nightclub.
                    String afterMsg = match.get(0).split("The winning map is")[1];
                    The5zigAPI.getLogger().info(afterMsg);
                    String map = "";
                    Pattern pattern = Pattern.compile(Pattern.quote(" ") + "(.*?)" + Pattern.quote("."));
                    Matcher matcher = pattern.matcher(afterMsg);
                    while (matcher.find()) {
                        map = matcher.group(1);
                    }
                    The5zigAPI.getLogger().info(map);
                    TIMV.mapStr = map;
                    DiscordUtils.updatePresence("Investigating in Trouble in Mineville", "Playing on " + map, "game_timv");

                    TIMV.activeMap = TIMV.mapsPool.get(map.toLowerCase());
                }
                break;
            case "dr.welcome":
            case "dr.fallback":
                getGameListener().switchLobby("DR");

                The5zigAPI.getLogger().info("Connected to DR! -Hive");
                DiscordUtils.updatePresence("Parkouring in DeathRun", "In Lobby", "game_dr");
                break;
            case "bed.welcome":
            case "bed.spectator":
            case "bed.fallback":
                getGameListener().switchLobby(
                        "bed.fallback".equals(key)
                                ? "BED_" + ChatColor.stripColor(match.get(0)).replace("The winning map is ", "").replace(".", "")
                                : "BED");

                The5zigAPI.getLogger().info("Connected to BED/BEDT! -Hive");
                DiscordUtils.updatePresence("Housekeeping in BedWars", "In Lobby", "game_bedwars");
                break;
            case "hide.welcome":
            case "hide.fallback":
                getGameListener().switchLobby("HIDE");
                The5zigAPI.getLogger().info("Connected to HIDE! -Hive");
                DiscordUtils.updatePresence("Playing Hide & Seek", "In Lobby", "game_hide");
                break;
            case "sky.welcome":
            case "sky.fallback":
                getGameListener().switchLobby("SKY");
                The5zigAPI.getLogger().info("Connected to SKY! - Hive");
                DiscordUtils.updatePresence("Fighting in SkyWars", "In Lobby", "game_skywars");

                break;
            case "grav.welcome":
                getGameListener().switchLobby("GRAV");
                The5zigAPI.getLogger().info("Connected to GRAV! - Hive");
                DiscordUtils.updatePresence("Freefalling in Gravity", "In Lobby", "game_grav");

                break;
            case "bp.welcome":
                getGameListener().switchLobby("BP");
                The5zigAPI.getLogger().info("Connected to BP! - Hive");
                DiscordUtils.updatePresence("Dancing in BlockParty", "Startup", "game_bp");

                break;
            case "sgn.welcome":
            case "sgn.fallback":
                getGameListener().switchLobby("SGN");
                The5zigAPI.getLogger().info("Connected to SGN! - Hive");
                DiscordUtils.updatePresence("Battling in SG2", "In Lobby", "game_sgn");
                break;
            case "sgn2.welcome":
            case "sgn2.fallback":
                getGameListener().switchLobby("SGN2");
                The5zigAPI.getLogger().info("Connected to SGN2! - Hive");
                DiscordUtils.updatePresence("Battling in SG2", "In Lobby", "game_sgn");
                break;
        }


        if ("somearcades.welcome".equals(key)) {
            new Thread(() -> {
                try {
                    Thread.sleep(500L);
                    if (!ActiveGame.current().isEmpty() || (gameMode != null && gameMode.getState() != GameState.FINISHED)) {
                        return;
                    }
                    if (The5zigAPI.getAPI().getSideScoreboard() == null) return;
                    if (The5zigAPI.getAPI().getSideScoreboard().getTitle() == null) return;
                    String title = ChatColor.stripColor(The5zigAPI.getAPI().getSideScoreboard().getTitle()).trim();
                    if (title.startsWith("Your") && title.endsWith("Stats")) {
                        String game = title.split(" ")[1];
                        getGameListener().switchLobby("ARCADE_" + game);

                        DiscordUtils.updatePresence("Playing an Arcade Game", "In Lobby (" + game + ")", "game_arcade");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        if (key.startsWith("arcade.")) {
            String[] path = key.split("\\.");
            String game = path[1].toUpperCase();
            getGameListener().switchLobby("ARCADE_" + game);

            DiscordUtils.updatePresence("Playing an Arcade Game", "In Lobby (" + game + ")", "game_arcade");

        }


    }


}
