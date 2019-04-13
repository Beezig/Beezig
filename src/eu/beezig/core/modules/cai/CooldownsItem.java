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

package eu.beezig.core.modules.cai;

import eu.beezig.core.games.CAI;
import eu.beezig.core.modules.utils.RenderUtils;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.render.RenderLocation;

import java.text.DecimalFormat;

public class CooldownsItem extends GameModeItem<CAI> {

    private DecimalFormat df = new DecimalFormat("00");

    public CooldownsItem() {
        super(CAI.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        return "";
    }

    @Override
    public void render(int x, int y, RenderLocation renderLocation, boolean dummy) {
        RenderUtils.enableBlend();
        if (CAI.speedCooldown != 0) {

            y += 12;

            long secs = CAI.speedCooldown / 20;

            String display = secs / 60 + ":" + df.format((secs % 60));
            display("Speed - " + display, x, y, 0);
        }
        if (CAI.invisCooldown != 0) {

            y += 12;

            long secs = CAI.invisCooldown / 20;

            String display = secs / 60 + ":" + df.format((secs % 60));
            display("Invisibility - " + display, x, y, 8);
        }
        if (CAI.leaderItem0 != 0) {

            y += 12;

            long secs = CAI.leaderItem0 / 20;

            String display = secs / 60 + ":" + df.format((secs % 60));
            display("Blind Carrier - " + display, x, y, 13);
        }
        if (CAI.leaderItem1 != 0) {

            y += 12;

            long secs = CAI.leaderItem1 / 20;

            String display = secs / 60 + ":" + df.format((secs % 60));
            display("Emergency Flare - " + display, x, y, 7);
        }
        if (CAI.leaderItem2 != 0) {

            y += 12;

            long secs = CAI.leaderItem2 / 20;

            String display = secs / 60 + ":" + df.format((secs % 60));
            display("Attempt Escape - " + display, x, y, 10);
        }


    }

    @Override
    public String getName() {
        return ""; // Ignored
    }


    @Override
    public int getHeight(boolean dummy) {

        return (CAI.speedCooldown != 0 ? 12 : 0)
                + (CAI.invisCooldown != 0 ? 12 : 0)
                + (CAI.leaderItem0 != 0 ? 12 : 0)
                + (CAI.leaderItem1 != 0 ? 12 : 0)
                + (CAI.leaderItem2 != 0 ? 12 : 0);

    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            return dummy || (CAI.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

    private void display(String display, int x, int y, int potion) {
        RenderUtils.color(1f, 1f, 1f, 1f);
        RenderUtils.bindTexture(RenderUtils.INVENTORY_BACKGROUND);
        float scale = 0.7f;
        RenderUtils.pushMatrix();
        RenderUtils.scale(scale, scale, scale);
        RenderUtils.translate(x / scale, (y - 3) / scale, 0f);
        RenderUtils.renderPotionIcon(potion);
        RenderUtils.popMatrix();
        The5zigAPI.getAPI().getRenderHelper().drawString(display, x + 16, y);
    }

}
