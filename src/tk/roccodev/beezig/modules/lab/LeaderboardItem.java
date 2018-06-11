package tk.roccodev.beezig.modules.lab;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.render.RenderLocation;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.GRAV;
import tk.roccodev.beezig.games.LAB;

import java.util.Map;

public class LeaderboardItem extends GameModeItem<LAB> {

    public LeaderboardItem() {
        super(LAB.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        try {
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "No Map";
        }
    }

    @Override
    public String getName() {
        return Log.t("beezig.module.lab.leaderboard");
    }

    @Override
    public void render(int x, int y, RenderLocation renderLocation, boolean dummy) {
        int lineCount = 0;


        for (Map.Entry<String, Integer> e : LAB.leaderboard.entrySet()) {
            if(e.getKey().equals(The5zigAPI.getAPI().getGameProfile().getName())) {
                The5zigAPI.getAPI().getRenderHelper().drawString("§a" + e.getKey() + " | " + e.getValue(), x, y + lineCount * 10);
            }
            else {
                The5zigAPI.getAPI().getRenderHelper().drawString(e.getKey() + " | " + e.getValue(), x, y + lineCount * 10);
            }
            lineCount++;
        }

    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && ActiveGame.is("lab")
                    && LAB.leaderboard.size() != 0);
        } catch (Exception e) {
            return false;
        }
    }

}
