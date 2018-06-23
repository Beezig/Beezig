package tk.roccodev.beezig.hiveapi.wrapper.modes;

import eu.the5zig.mod.server.GameMode;
import tk.roccodev.beezig.games.BP;
import tk.roccodev.beezig.hiveapi.wrapper.APIGameMode;

import java.util.Date;

public class ApiBP extends APIGameMode {

    public ApiBP(String playerName, String... UUID) {
        super(playerName, UUID);
    }

    @Override
    public Class<? extends GameMode> getGameMode() {
        // TODO Auto-generated method stub
        return BP.class;
    }

    @Override
    public String getShortcode() {
        // TODO Auto-generated method stub
        return "BP";
    }

    @Override
    public Date lastPlayed() {
        // TODO Auto-generated method stub
        return new Date((long) object("cached"));
    }


}
