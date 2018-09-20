package tk.roccodev.beezig.modules.sky;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.SKY;

public class TeamItem extends GameModeItem<SKY> {

    public TeamItem() {
        super(SKY.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        try {

            boolean color = (boolean) getProperties().getSetting("color").get();

            return color ? SKY.team : ChatColor.stripColor(SKY.team);

        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getName() {
        return Log.t("beezig.module.sky.team");
    }


    @Override
    public void registerSettings() {
        getProperties().addSetting("color", true);
        super.registerSettings();
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null)
                return false;
            if (SKY.team == null || SKY.team.isEmpty())
                return false;
            return dummy || (SKY.shouldRender(getGameMode().getState()));
        } catch (Exception e) {
            return false;
        }
    }

}
