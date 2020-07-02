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

package eu.beezig.core.modules.bed;

import eu.beezig.core.games.BED;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.render.RenderLocation;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;

public class ResourcesItem extends GameModeItem<BED> {

    private int size = 0;

    public ResourcesItem() {
        super(BED.class);
    }

    private String getMainFormatting() {
        return "§r";
    }

    @Override
    protected Object getValue(boolean dummy) {
        if (!The5zigAPI.getAPI().isInWorld()) return "Not in world";
        if (getProperties().getSetting("mode").get() == ResourcesMode.INLINE) {
            try {
                StringBuilder sb = new StringBuilder();
                boolean colors = (boolean) getProperties().getSetting("showcolors").get();

                int ironIngots = The5zigAPI.getAPI().getItemCount("minecraft:iron_ingot");
                int goldIngots = The5zigAPI.getAPI().getItemCount("minecraft:gold_ingot");
                int diamonds = The5zigAPI.getAPI().getItemCount("minecraft:diamond");
                int emeralds = The5zigAPI.getAPI().getItemCount("minecraft:emerald");


                if (ironIngots != 0)
                    sb.append(colors ? "§7" + ironIngots : ironIngots).append(colors ? " §7Iron " + getMainFormatting() + "/ " : " Iron / ");
                if (goldIngots != 0)
                    sb.append(colors ? "§6" + goldIngots : goldIngots).append(colors ? " §6Gold " + getMainFormatting() + "/ " : " Gold / ");
                if (diamonds != 0)
                    sb.append(colors ? "§b" + diamonds : diamonds).append(colors ? " §bDiamonds " + getMainFormatting() + "/ " : " Diamonds / ");
                if (emeralds != 0)
                    sb.append(colors ? "§a" + emeralds : emeralds).append(colors ? " §aEmeralds " + getMainFormatting() + "/ " : " Emeralds / ");
                if (sb.length() > 2) sb.deleteCharAt(sb.length() - 2);


                return sb.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        } else if (getProperties().getSetting("mode").get() == ResourcesMode.INLINE_SHORTENED) {
            try {
                StringBuilder sb = new StringBuilder();

                boolean colors = (boolean) getProperties().getSetting("showcolors").get();
                int ironIngots = The5zigAPI.getAPI().getItemCount("minecraft:iron_ingot");
                int goldIngots = The5zigAPI.getAPI().getItemCount("minecraft:gold_ingot");
                int diamonds = The5zigAPI.getAPI().getItemCount("minecraft:diamond");
                int emeralds = The5zigAPI.getAPI().getItemCount("minecraft:emerald");


                if (ironIngots != 0)
                    sb.append(colors ? "§7" + ironIngots : ironIngots).append(colors ? " §7I " + getMainFormatting() + "/ " : " I / ");
                if (goldIngots != 0)
                    sb.append(colors ? "§6" + goldIngots : goldIngots).append(colors ? " §6G " + getMainFormatting() + "/ " : " G / ");
                if (diamonds != 0)
                    sb.append(colors ? "§b" + diamonds : diamonds).append(colors ? " §bD " + getMainFormatting() + "/ " : " D / ");
                if (emeralds != 0)
                    sb.append(colors ? "§a" + emeralds : emeralds).append(colors ? " §aE " + getMainFormatting() + "/ " : " E / ");
                if (sb.length() > 2) sb.deleteCharAt(sb.length() - 2);


                return sb.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        } else {

            return "";
        }

    }

    @Override
    public int getHeight(boolean dummy) {
        ResourcesMode mode = (ResourcesMode) getProperties().getSetting("mode").get();
        switch (mode) {

            case INLINE:
            case INLINE_SHORTENED:
                return super.getHeight(dummy);
            case EXTENDED:
                return size + 10;
        }

        return super.getHeight(dummy);
    }


    @Override
    public void render(int x, int y, RenderLocation renderLocation, boolean dummy) {
        if (getProperties().getSetting("mode").get() != ResourcesMode.EXTENDED) {
            super.render(x, y, renderLocation, dummy);
            return;
        }
        if (!The5zigAPI.getAPI().isInWorld()) return;
        int lineCount = 0;
        The5zigAPI.getAPI().getRenderHelper().drawString(getPrefix(), x, y);
        lineCount++;

        boolean colors = (boolean) getProperties().getSetting("showcolors").get();

        int ironIngots = The5zigAPI.getAPI().getItemCount("minecraft:iron_ingot");
        int goldIngots = The5zigAPI.getAPI().getItemCount("minecraft:gold_ingot");
        int diamonds = The5zigAPI.getAPI().getItemCount("minecraft:diamond");
        int emeralds = The5zigAPI.getAPI().getItemCount("minecraft:emerald");

        int calcSize = (ironIngots != 0 ? 10 : 0) + (goldIngots != 0 ? 10 : 0) + (diamonds != 0 ? 10 : 0) + (emeralds != 0 ? 10 : 0);
        if (size != calcSize) size = calcSize;

        if (ironIngots != 0) {
            if (colors) {
                The5zigAPI.getAPI().getRenderHelper().drawString(ironIngots + " Iron", x, y + lineCount * 10, ChatColor.GRAY.getColor(), true);
            } else {
                The5zigAPI.getAPI().getRenderHelper().drawString(ironIngots + " Iron", x, y + lineCount * 10, true);
            }
            lineCount++;
        }
        if (goldIngots != 0) {
            if (colors) {
                The5zigAPI.getAPI().getRenderHelper().drawString(goldIngots + " Gold", x, y + lineCount * 10, ChatColor.GOLD.getColor(), true);
            } else {
                The5zigAPI.getAPI().getRenderHelper().drawString(goldIngots + " Gold", x, y + lineCount * 10, true);
            }
            lineCount++;
        }
        if (diamonds != 0) {
            if (colors) {
                The5zigAPI.getAPI().getRenderHelper().drawString(diamonds + " Diamonds", x, y + lineCount * 10, ChatColor.AQUA.getColor(), true);
            } else {
                The5zigAPI.getAPI().getRenderHelper().drawString(diamonds + " Diamonds", x, y + lineCount * 10, true);
            }
            lineCount++;
        }
        if (emeralds != 0) {
            if (colors) {
                The5zigAPI.getAPI().getRenderHelper().drawString(emeralds + " Emeralds", x, y + lineCount * 10, ChatColor.GREEN.getColor(), true);
            } else {
                The5zigAPI.getAPI().getRenderHelper().drawString(emeralds + " Emeralds", x, y + lineCount * 10, true);
            }
        }

    }

    @Override
    public String getTranslation() {
        return "beezig.module.bed.resources";
    }


    @Override
    public void registerSettings() {

        getProperties().addSetting("mode", ResourcesMode.INLINE, ResourcesMode.class);
        getProperties().addSetting("showcolors", true);

    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || getGameMode().getState() == GameState.GAME;
        } catch (Exception e) {
            return false;
        }
    }

}
