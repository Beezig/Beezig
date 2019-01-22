package eu.beezig.core.modules.gnt;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.games.Giant;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.util.minecraft.ChatColor;

public class GoldItem extends GameModeItem<Giant> {

    public GoldItem() {
        super(Giant.class);
    }


    @Override
    protected Object getValue(boolean dummy) {
        try {
            if ((boolean) getProperties().getSetting("showcolor").get())
                return ChatColor.GOLD + "" + Log.df(Giant.gold);
            return Log.df(Giant.gold);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    @Override
    public String getTranslation() { return "beezig.module.gnt.gold";}

    @Override
    public void registerSettings() {
        getProperties().addSetting("showcolor", true);
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {

            return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && (ActiveGame.is("gnt") || ActiveGame.is("gntm")) && Giant.gold != 0);
        } catch (Exception e) {
            return false;
        }
    }

}
