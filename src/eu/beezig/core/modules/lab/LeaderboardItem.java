package eu.beezig.core.modules.lab;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.beezig.core.games.LAB;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.render.RenderLocation;

import java.util.Map;

public class LeaderboardItem extends GameModeItem<LAB> {

    public LeaderboardItem() {
        super(LAB.class);
    }

    private String getMainFormatting() {
        if (this.getProperties().getFormatting() != null) {
            if (this.getProperties().getFormatting().getMainColor() != null && this.getProperties().getFormatting().getMainFormatting() == null) {
                return The5zigAPI.getAPI().getFormatting().getMainFormatting().replace((The5zigAPI.getAPI().getFormatting().getMainFormatting()).charAt(1), this.getProperties().getFormatting().getMainColor().toString().charAt(1));
                //Replaces Char at index 1 (ColorTag) of the Main formatting with the custom one.
            }
            if (this.getProperties().getFormatting().getMainColor() == null && this.getProperties().getFormatting().getMainFormatting() != null) {
                return The5zigAPI.getAPI().getFormatting().getMainFormatting().replace((The5zigAPI.getAPI().getFormatting().getMainFormatting()).charAt(3), this.getProperties().getFormatting().getMainFormatting().toString().charAt(3));
                //Replaces Char at index 3 (FormattingTag) of the Main formatting with the custom one.
            }
            if (this.getProperties().getFormatting().getMainColor() != null && this.getProperties().getFormatting().getMainFormatting() != null) {
                return this.getProperties().getFormatting().getMainColor() + "" + this.getProperties().getFormatting().getMainFormatting();
            }
        }
        return The5zigAPI.getAPI().getFormatting().getMainFormatting();
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
    public String getTranslation() { return "beezig.module.lab.leaderboard";}

    @Override
    public void render(int x, int y, RenderLocation renderLocation, boolean dummy) {
        int lineCount = 0;


        for (Map.Entry<String, Integer> e : LAB.leaderboard.entrySet()) {
            if (e.getKey().equals(The5zigAPI.getAPI().getGameProfile().getName())) {
                The5zigAPI.getAPI().getRenderHelper().drawString("§a" + e.getKey() + " | " + e.getValue(), x, y + lineCount * 10);
            } else {
                The5zigAPI.getAPI().getRenderHelper().drawString(getMainFormatting() + e.getKey() + " | " + e.getValue(), x, y + lineCount * 10);
            }
            lineCount++;
        }

    }
    
    

    @Override
	public int getHeight(boolean dummy) {
		// TODO Auto-generated method stub
		return 10 + LAB.leaderboard.size() * 10;
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
