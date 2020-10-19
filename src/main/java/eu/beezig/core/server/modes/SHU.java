package eu.beezig.core.server.modes;

import eu.beezig.core.Beezig;
import eu.beezig.core.api.BeezigForge;
import eu.beezig.core.config.Settings;
import eu.beezig.core.server.HiveMode;
import eu.beezig.core.util.text.Message;

import java.util.List;

public class SHU extends HiveMode {
    private String game;

    @Override
    public String getIdentifier() {
        return "shu";
    }

    @Override
    public String getName() {
        return "Arcade Shuffle";
    }

    @Override
    protected void onModeJoin() {
        super.onModeJoin();
        if(Settings.AUTOVOTE_SHUFFLE.get().getBoolean() && BeezigForge.isSupported()) {
            try {
                List<String> maps = getAutovoteManager().getFavoriteMaps("shu");
                if(!maps.isEmpty() && BeezigForge.isSupported()) BeezigForge.get().autovoteShuffle(maps);
            } catch (Exception e) {
                Message.error(Message.translate("error.data_read"));
                Beezig.logger.error(e);
            }
        }
        if(BeezigForge.isSupported()) BeezigForge.get().setCurrentGame(null);
    }

    @Override
    protected boolean supportsTemporaryPoints() {
        return false;
    }

    public void setGame(String game) {
        this.game = game.trim();
        String id = null;
        switch (this.game) {
            case "Splegg":
                id = "SP";
                break;
            case "BatteryDash":
                id = "BD";
                break;
            case "ElectricFloor":
                id = "EF";
                break;
            case "Cranked":
                id = "CR";
                break;
            case "Sploop":
                id = "SPL";
                break;
            case "One in the Chamber":
                id = "OITC";
                break;
            case "Slaparoo":
                id = "SLAP";
                break;
            case "Music Masters":
                id = "MM";
                break;
            case "Restaurant Rush":
                id = "RR";
                break;
        }
        if(BeezigForge.isSupported()) BeezigForge.get().setCurrentGame(id);
    }

    public String getGame() {
        return game;
    }
}
