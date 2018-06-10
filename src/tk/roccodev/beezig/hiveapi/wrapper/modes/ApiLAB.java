package tk.roccodev.beezig.hiveapi.wrapper.modes;

import eu.the5zig.mod.server.GameMode;
import tk.roccodev.beezig.games.BP;
import tk.roccodev.beezig.games.LAB;
import tk.roccodev.beezig.hiveapi.wrapper.APIGameMode;

import java.util.Date;

public class ApiLAB extends APIGameMode {

    public ApiLAB(String playerName, String... UUID) {
        super(playerName, UUID);
    }

    @Override
    public Class<? extends GameMode> getGameMode() {
        // TODO Auto-generated method stub
        return LAB.class;
    }

    @Override
    public String getShortcode() {
        // TODO Auto-generated method stub
        return "LAB";
    }

    @Override
    public Date lastPlayed() {
        // TODO Auto-generated method stub
        return new Date((long) object("lastlogin"));
    }

    @Override
    public long getGamesPlayed() {
        return (long) object("gamesplayed");
    }
}
