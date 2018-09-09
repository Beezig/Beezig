package tk.roccodev.beezig.modules.grav;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.render.RenderLocation;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.GRAV;

public class StagesItem extends GameModeItem<GRAV> {

    public StagesItem() {
        super(GRAV.class);
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
        return Log.t("beezig.module.grav.stages");
    }

    @Override
    public void render(int x, int y, RenderLocation renderLocation, boolean dummy) {
        int lineCount = 0;
        The5zigAPI.getAPI().getRenderHelper().drawString(getPrefix(), x, y);
        lineCount++;

        for (String v : GRAV.toDisplayWithFails.values()) {
            The5zigAPI.getAPI().getRenderHelper().drawString(v.replace("{f}", "0"), x, y + lineCount * 10);
            lineCount++;
        }

    }

    
    
    @Override
	public int getHeight(boolean dummy) {
		// TODO Auto-generated method stub
		return 10 + GRAV.toDisplayWithFails.size() * 10;
	}

	@Override
    public boolean shouldRender(boolean dummy) {
        try {
            return dummy || (The5zigAPI.getAPI().getActiveServer() instanceof IHive && ActiveGame.is("grav")
                    && GRAV.toDisplay.size() != 0);
        } catch (Exception e) {
            return false;
        }
    }

}
