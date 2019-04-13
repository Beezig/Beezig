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

package eu.beezig.core.modules.timv;

import eu.beezig.core.Log;
import eu.beezig.core.games.TIMV;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;

public class DBodiesItem extends GameModeItem<TIMV> {

    public DBodiesItem() {
        super(TIMV.class);
    }

    @Override
    protected Object getValue(boolean dummy) {

        return TIMV.detectivesDiscovered + "/" + TIMV.detectivesBefore + " " + Log.t("beezig.str.timv.detectives");

    }

    @Override
    public String getTranslation() {
        return "beezig.module.timv.dbodies";
    }


    // LabyMod
    protected String getCategoryKey() {
        return "timv";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (getGameMode().getState() == GameState.GAME);
        } catch (Exception e) {
            return false;
        }
    }

}
