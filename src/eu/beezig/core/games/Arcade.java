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
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;

public class Arcade extends GameMode {

    private static Arcade inst;

    public String game;
    public String gameDisplay;
    public String map;

    public Arcade() {
        inst = this;
    }

    public static void reset() {
        inst.resetInternally();
    }

    public static Modes[] getModes() {
        return Modes.values();
    }

    @Override
    public String getName() {
        return "Arcade/" + game;
    }

    public void setMode(String raw) {
        game = raw;
        gameDisplay = Modes.valueOf(raw).getDisplay();
    }

    public void resetInternally() {
        this.setState(GameState.FINISHED);
        ActiveGame.reset("ARCADE_" + game);
        IHive.genericReset();
        game = "";
        gameDisplay = null;
        map = null;
        if (The5zigAPI.getAPI().getActiveServer() != null)
            The5zigAPI.getAPI().getActiveServer().getGameListener().switchLobby("");

    }

    private enum Modes {

        SHU("Shuffle"),
        CR("Cranked"),
        MM("MusicMasters"),
        OITC("One in the Chamber"),
        RR("RestaurantRush"),
        SLAP("Slaparoo"),
        HERO("SG:Heroes"),
        SPL("Sploop"),
        HB("TheHerobrine"),
        DRAW("DrawIt"),
        SP("Splegg"),
        BD("BatteryDash"),
        EF("ElectricFloor");


        private String display;

        Modes(String display) {
            this.display = display;
        }

        public String getDisplay() {
            return display;
        }
    }
}
