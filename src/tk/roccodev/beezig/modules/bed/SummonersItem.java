package tk.roccodev.beezig.modules.bed;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.render.RenderLocation;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.BED;

public class SummonersItem extends GameModeItem<BED> {

    public SummonersItem() {
        super(BED.class);
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
        if (!The5zigAPI.getAPI().isInWorld()) return "Not in world";
        if (getProperties().getSetting("mode").get() == ResourcesMode.INLINE) {
            try {
                StringBuilder sb = new StringBuilder();
                boolean colors = (boolean) getProperties().getSetting("showcolors").get();


                if (BED.ironGen != 0)
                    sb.append(colors ? "§f" : "").append(BED.NUMBERS[BED.ironGen]).append(" Iron ").append(getMainFormatting()).append("/ ");
                if (BED.goldGen != 0)
                    sb.append(colors ? "§e" : "").append(BED.NUMBERS[BED.goldGen]).append(" Gold ").append(getMainFormatting()).append("/ ");
                if (BED.diamondGen != 0)
                    sb.append(colors ? "§b" : "").append(BED.NUMBERS[BED.diamondGen]).append(" Diamond ").append(getMainFormatting()).append("/ ");
                if (sb.length() > 2) sb.deleteCharAt(sb.length() - 2);


                return sb.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        } else if (getProperties().getSetting("mode").get() == ResourcesMode.INLINE_SHORTENED) {
            try {
                StringBuilder sb = new StringBuilder();

                boolean colors = (boolean) getProperties().getSetting("showcolors").get();
                if (BED.ironGen != 0)
                    sb.append(colors ? "§f" : "").append(BED.NUMBERS[BED.ironGen]).append(" I ").append(getMainFormatting()).append("/ ");
                if (BED.goldGen != 0)
                    sb.append(colors ? "§e" : "").append(BED.NUMBERS[BED.goldGen]).append(" G ").append(getMainFormatting()).append("/ ");
                if (BED.diamondGen != 0)
                    sb.append(colors ? "§b" : "").append(BED.NUMBERS[BED.diamondGen]).append(" D ").append(getMainFormatting()).append("/ ");
                if (sb.length() > 2) sb.deleteCharAt(sb.length() - 2);
                if (sb.length() > 2) sb.deleteCharAt(sb.length() - 2);


                return sb.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        } else {

            return "";
        }

    }

    @Override
    public void render(int x, int y, RenderLocation renderLocation, boolean dummy) {
        if (getProperties().getSetting("mode").get() != ResourcesMode.EXTENDED) {
            super.render(x, y, renderLocation, dummy);
            return;
        }
        if (!The5zigAPI.getAPI().isInWorld()) return;
        int lineCount = 0;
        The5zigAPI.getAPI().getRenderHelper().drawString(getPrefix(), x, y);
        lineCount++;

        boolean colors = (boolean) getProperties().getSetting("showcolors").get();


        if (BED.ironGen != 0) {
            if (colors) {
                The5zigAPI.getAPI().getRenderHelper().drawString(BED.NUMBERS[BED.ironGen] + " Iron", x, y + lineCount * 10, ChatColor.WHITE.getColor(), true);
            } else {
                The5zigAPI.getAPI().getRenderHelper().drawString(BED.NUMBERS[BED.ironGen] + " Iron", x, y + lineCount * 10, true);
            }
            lineCount++;
        }
        if (BED.goldGen != 0) {
            if (colors) {
                The5zigAPI.getAPI().getRenderHelper().drawString(BED.NUMBERS[BED.goldGen] + " Gold", x, y + lineCount * 10, ChatColor.YELLOW.getColor(), true);
            } else {
                The5zigAPI.getAPI().getRenderHelper().drawString(BED.NUMBERS[BED.goldGen] + " Gold", x, y + lineCount * 10, true);
            }
            lineCount++;
        }
        if (BED.diamondGen != 0) {
            if (colors) {
                The5zigAPI.getAPI().getRenderHelper().drawString(BED.NUMBERS[BED.diamondGen] + " Diamonds", x, y + lineCount * 10, ChatColor.AQUA.getColor(), true);
            } else {
                The5zigAPI.getAPI().getRenderHelper().drawString(BED.NUMBERS[BED.diamondGen] + " Diamonds", x, y + lineCount * 10, true);
            }
            lineCount++;
        }


    }
    
    

    @Override
	public int getHeight(boolean dummy) {
    	ResourcesMode mode = (ResourcesMode) getProperties().getSetting("mode").get();
    	switch(mode) {
    	
    	case INLINE:
    	case INLINE_SHORTENED:
    		return super.getHeight(dummy);
    	case EXTENDED:
    		return 10 + (BED.diamondGen != 0 ? 10 : 0)
    				+ (BED.ironGen != 0 ? 10 : 0)
    				+ (BED.goldGen != 0 ? 10 : 0);
    	}
    	
    	return super.getHeight(dummy);
	}

	@Override
    public String getTranslation() { return "beezig.module.bed.summoners";}


    @Override
    public void registerSettings() {


        getProperties().addSetting("mode", ResourcesMode.INLINE, ResourcesMode.class);
        getProperties().addSetting("showcolors", true);

    }

    @Override
    public boolean shouldRender(boolean dummy) {
        try {
            if (getGameMode() == null) return false;
            return dummy || getGameMode().getState() == GameState.GAME;
        } catch (Exception e) {
            return false;
        }
    }

}
