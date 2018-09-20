package tk.roccodev.beezig.listener;

import eu.the5zig.mod.server.AbstractGameListener;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.games.Arcade;

public class ArcadeListener extends AbstractGameListener<Arcade> {

    private String game = null;

    @Override
    public Class<Arcade> getGameMode() {
        return Arcade.class;
    }

    @Override
    public boolean matchLobby(String s) {
        if(!s.startsWith("ARCADE_")) return false;
        game = s.split("_")[1];
        return true;
    }

    @Override
    public void onGameModeJoin(Arcade gameMode) {
        IHive.genericJoin();
        System.out.println("Joined Arcade game: " + game);
        gameMode.setMode(game);


    }

    @Override
    public void onServerConnect(Arcade gameMode) {
        gameMode.reset();
    }
}
