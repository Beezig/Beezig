package tk.roccodev.beezig.listener;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.server.AbstractGameListener;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.games.Arcade;
import tk.roccodev.beezig.hiveapi.APIValues;
import tk.roccodev.beezig.listener.arcade.ArcadeSubListener;
import tk.roccodev.beezig.listener.arcade.subs.ElectricFloorSubListener;
import tk.roccodev.beezig.listener.arcade.subs.SpleggSubListener;
import tk.roccodev.beezig.utils.tutorial.SendTutorial;

import java.util.ArrayList;
import java.util.List;

public class ArcadeListener extends AbstractGameListener<Arcade> {

    private String game = null;
    private List<ArcadeSubListener> subs = new ArrayList<>();

    public ArcadeListener() {

        subs.add(new SpleggSubListener());
        subs.add(new ElectricFloorSubListener());
    }

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
        ActiveGame.set("ARCADE_" + game);
        SendTutorial.send("arcade_join", game);

        new Thread(() -> {
            try {
                Thread.sleep(100L);
                Scoreboard sb = The5zigAPI.getAPI().getSideScoreboard();
                if(sb != null) {
                    APIValues.ArcadePoints = sb.getLines().get(game.equals("SLAP") ? "§bSlap Points" : "§bPoints");
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


    }

    @Override
    public boolean onServerChat(Arcade gameMode, String message) {
        subs.forEach(arcadeSubListener -> arcadeSubListener.onServerChat(gameMode, message));
        return false;
    }

    @Override
    public void onServerConnect(Arcade gameMode) {
        gameMode.resetInternally();
    }
}
