package eu.beezig.core.modules.bp;

import eu.beezig.core.games.BP;
import eu.the5zig.mod.modules.GameModeItem;

public class SongItem extends GameModeItem<BP> {

    public SongItem() {
        super(BP.class);
    }

    @Override
    protected Object getValue(boolean dummy) {
        if ((boolean) getProperties().getSetting("showartist").get()) return BP.song + " (" + BP.artist + ")";
        else return BP.song;

    }

    @Override
    public String getTranslation() {
        return "beezig.module.bp.song";
    }

    @Override
    public void registerSettings() {
        // TODO Auto-generated method stub
        getProperties().addSetting("showartist", true);
    }

    @Override
    public boolean shouldRender(boolean dummy) {

        try {
            if (getGameMode() == null)
                return false;
            return dummy || (BP.shouldRender(getGameMode().getState()) && BP.song != null && !BP.song.isEmpty());
        } catch (Exception e) {
            return false;
        }
    }

}
