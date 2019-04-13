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
import eu.the5zig.mod.modules.GameModeItem;

import java.text.DecimalFormat;

public class KDRChangeItem extends GameModeItem<BED> {

    public KDRChangeItem() {
        super(BED.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        try {

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            df.setMinimumFractionDigits(2);

            return df.format(BED.gameKdr) + " (" + (BED.gameKdr - BED.apiKdr > 0 ? "+" + df.format(BED.gameKdr - BED.apiKdr) : df.format(BED.gameKdr - BED.apiKdr)) + ")";
        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getTranslation() {
        return "beezig.module.bed.kdchange";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (BED.shouldRender(getGameMode().getState()) && (BED.apiKdr - BED.gameKdr != 0));
        } catch (Exception e) {
            return false;
        }
    }

}
