package tk.roccodev.beezig.games;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;

public class Arcade extends GameMode {

    private static Arcade inst;

    public String game;
    public String gameDisplay;
    public String map;

    public Arcade() {
        inst = this;
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

    public static void reset() {
        inst.resetInternally();
    }

    public static Modes[] getModes() {
        return Modes.values();
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
