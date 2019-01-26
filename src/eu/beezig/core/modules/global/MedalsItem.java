package eu.beezig.core.modules.global;

import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.hiveapi.APIValues;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameMode;

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
    public String getTranslation() {
        return "beezig.module.global.medals";
    }


    @Override
    public boolean shouldRender(boolean dummy) {
        try {

            return dummy || The5zigAPI.getAPI().getActiveServer() instanceof IHive;
        } catch (Exception e) {
            return false;
        }
    }

}
