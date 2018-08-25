package tk.roccodev.beezig.modules.sky;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.SKY;

public class KillsItem extends GameModeItem<SKY> {

    public KillsItem() {
        super(SKY.class);
    }


    @Override
    protected Object getValue(boolean dummy) {
        try {

            if ((boolean) getProperties().getSetting("showtotal").get())
                return SKY.kills + " (" + Log.df(SKY.kills + SKY.totalKills) + ")";
            return SKY.kills;
        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getName() {
        return Log.t("beezig.module.kills");
    }

    @Override
    public void registerSettings() {
        getProperties().addSetting("showtotal", true);
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {

            return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && ActiveGame.is("sky") && SKY.kills != 0);
        } catch (Exception e) {
            return false;
        }
    }

}
