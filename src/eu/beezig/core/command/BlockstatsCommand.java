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

package eu.beezig.core.command;

import eu.beezig.core.Log;
import eu.beezig.core.hiveapi.wrapper.APIUtils;
import eu.beezig.core.hiveapi.wrapper.NetworkRank;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;
import pw.roccodev.beezig.hiveapi.wrapper.player.games.HideStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BlockstatsCommand implements Command {

    @Override
    public String getName() {
        return "blockstats";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/blockstats", "/bs"};
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length == 0) {
            The5zigAPI.getAPI().messagePlayer(Log.info + "Blockstats Usage:");
            The5zigAPI.getAPI().messagePlayer(ChatColor.AQUA + "/bs list (player) §7- §3Returns the levels of all blocks by the player");
        }
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("list") && args.length < 3) {
                new Thread(() -> {
                    String ign;
                    if (args.length == 1) {
                        ign = The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "");
                    } else {
                        ign = args[1];
                    }
                    The5zigAPI.getLogger().info("Init API");
                    HideStats api = new HideStats(ign);
                    Map<String, Long> blockExp = api.getBlockLevels();
                    Map<String, Long> rawBlockEx = api.getBlockExperience();

                    HivePlayer parent = api.getPlayer();


                    List<Object> blocks = new ArrayList<>(Arrays.asList(blockExp.keySet().toArray()));
                    List<Long> levels = new ArrayList<>();
                    List<Long> rawExp = new ArrayList<>();
                    for (Object block : blocks) {
                        levels.add(Long.valueOf(blockExp.get(block).toString()));
                        rawExp.add(Long.valueOf(rawBlockEx.get(block).toString()));
                    }

                    APIUtils.concurrentSort(rawExp, blocks, levels, rawExp);

                    The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");
                    The5zigAPI.getAPI().messagePlayer(Log.info + "§bBlockstats of §b" +
                            NetworkRank.fromDisplay(parent.getRank().getHumanName()).getColor() + parent.getUsername() + "§3:");

                    for (int i = blocks.size() - 1; i != 0; i--) {

                        String blockName = APIUtils.capitalize(blocks.get(i).toString().replaceAll("_", " "));
                        if (blockName.equals("Wood")) {
                            blockName = "Oak Planks";
                        }
                        if (blockName.equals("Wood:1")) {
                            blockName = "Wood";
                        }
                        if (blockName.equals("Wood:3")) {
                            blockName = "Jungle Planks";
                        }
                        if (blockName.equals("Stone:6")) {
                            blockName = "Polished Andesite";
                        }
                        if (blockName.equals("Prismarine:2")) {
                            blockName = "Dark Prismarine";
                        }

                        The5zigAPI.getAPI().messagePlayer(Log.info + "§3" + blockName + ": " + APIUtils.getLevelColorHIDE(levels.get(i).intValue()) + "" + levels.get(i) + APIUtils.getNextPecentHIDE(rawExp.get(i).intValue(), levels.get(i).intValue()));
                    }
                    The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    \n");

                }).start();
            }
        }

        return true;
    }
}
