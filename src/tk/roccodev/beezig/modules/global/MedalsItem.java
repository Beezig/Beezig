package tk.roccodev.beezig.modules.global;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameMode;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.hiveapi.APIValues;

public class MedalsItem extends GameModeItem<GameMode> {

    public MedalsItem() {
        super(GameMode.class);
    }


    @Override
    protected Object getValue(boolean dummy) {
        try {

            return Log.df(APIValues.medals);
        } catch (Exception e) {
            e.printStackTrace();
            return "Server error";
        }
    }

    @Override
    public String getTranslation() { return "beezig.module.global.medals";}


    @Override
    public boolean shouldRender(boolean dummy) {
        try {

            return dummy || The5zigAPI.getAPI().getActiveServer() instanceof IHive;
        } catch (Exception e) {
            return false;
        }
    }

}
