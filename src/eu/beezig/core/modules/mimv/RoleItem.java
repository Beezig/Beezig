package eu.beezig.core.modules.mimv;

import eu.beezig.core.games.MIMV;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.util.minecraft.ChatColor;

public class RoleItem extends GameModeItem<MIMV> {

    public RoleItem() {
        super(MIMV.class);
    }

    @Override
    protected Object getValue(boolean dummy) {


        return (boolean) getProperties().getSetting("showcolor").get() ? MIMV.role : ChatColor.stripColor(MIMV.role);


    }

    @Override
    public void registerSettings() {
        // TODO Auto-generated method stub
        getProperties().addSetting("showcolor", true);


    }


    @Override
    public String getTranslation() {
        return "beezig.module.role";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || (MIMV.shouldRender(getGameMode().getState()) && MIMV.role != null && !MIMV.role.isEmpty());
        } catch (Exception e) {
            return false;
        }
    }

}
