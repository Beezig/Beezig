package tk.roccodev.beezig.modules.bed;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.render.RenderLocation;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.BED;

public class ResourcesItem extends GameModeItem<BED>{

	public ResourcesItem(){
		super(BED.class);
	}
	
	private String getMainFormatting(){
		if(this.getProperties().getFormatting() != null){
			if(this.getProperties().getFormatting().getMainColor() != null && this.getProperties().getFormatting().getMainFormatting() == null){
				return The5zigAPI.getAPI().getFormatting().getMainFormatting().replace((The5zigAPI.getAPI().getFormatting().getMainFormatting()).charAt(1), this.getProperties().getFormatting().getMainColor().toString().charAt(1));
				//Replaces Char at index 1 (ColorTag) of the Main formatting with the custom one.
			}
			if(this.getProperties().getFormatting().getMainColor() == null && this.getProperties().getFormatting().getMainFormatting() != null){
				return The5zigAPI.getAPI().getFormatting().getMainFormatting().replace((The5zigAPI.getAPI().getFormatting().getMainFormatting()).charAt(3), this.getProperties().getFormatting().getMainFormatting().toString().charAt(3));
				//Replaces Char at index 3 (FormattingTag) of the Main formatting with the custom one.
			}
			if(this.getProperties().getFormatting().getMainColor() != null && this.getProperties().getFormatting().getMainFormatting() != null){
				return this.getProperties().getFormatting().getMainColor() +""+ this.getProperties().getFormatting().getMainFormatting();
			}
		}
		return The5zigAPI.getAPI().getFormatting().getMainFormatting();	
	}
	
	@Override
	protected Object getValue(boolean dummy) {
		if(!The5zigAPI.getAPI().isInWorld()) return "Not in world";
		if(getProperties().getSetting("mode").get() == ResourcesMode.INLINE){
			try{
			StringBuilder sb = new StringBuilder();
			boolean colors = (boolean)getProperties().getSetting("showcolors").get();
			
			int ironIngots = The5zigAPI.getAPI().getItemCount("minecraft:iron_ingot");
			int goldIngots = The5zigAPI.getAPI().getItemCount("minecraft:gold_ingot");
			int diamonds = The5zigAPI.getAPI().getItemCount("minecraft:diamond");
			int emeralds = The5zigAPI.getAPI().getItemCount("minecraft:emerald");
			if(ironIngots != 0) sb.append(colors ? "§7" + ironIngots : ironIngots).append(colors ? " §7Iron " + getMainFormatting() + "/ " : " Iron / ");
			if(goldIngots != 0) sb.append(colors ? "§6" + goldIngots : goldIngots).append(colors ? " §6Gold " + getMainFormatting() + "/ " : " Gold / ");
			if(diamonds != 0) sb.append(colors ? "§b" + diamonds : diamonds).append(colors ? " §bDiamonds " + getMainFormatting() + "/ " : " Diamonds / ");
			if(emeralds != 0) sb.append(colors ? "§a" + emeralds : emeralds).append(colors ? " §aEmeralds " + getMainFormatting() + "/ " : " Emeralds / ");
			 if(sb.length() > 2) sb.deleteCharAt(sb.length() - 2);

			
			return sb.toString().trim();
			}catch(Exception e){
				e.printStackTrace();
				return "Error";
			}
		} else if(getProperties().getSetting("mode").get() == ResourcesMode.INLINE_SHORTENED){
			try{
			StringBuilder sb = new StringBuilder();
			
			boolean colors = (boolean)getProperties().getSetting("showcolors").get();
			int ironIngots = The5zigAPI.getAPI().getItemCount("minecraft:iron_ingot");
			int goldIngots = The5zigAPI.getAPI().getItemCount("minecraft:gold_ingot");
			int diamonds = The5zigAPI.getAPI().getItemCount("minecraft:diamond");
			int emeralds = The5zigAPI.getAPI().getItemCount("minecraft:emerald");
			if(ironIngots != 0) sb.append(colors ? "§7" + ironIngots : ironIngots).append(colors ? " §7I " + getMainFormatting() + "/ " : " I / ");
			if(goldIngots != 0) sb.append(colors ? "§6" + goldIngots : goldIngots).append(colors ? " §6G " + getMainFormatting() + "/ " : " G / ");
			if(diamonds != 0) sb.append(colors ? "§b" + diamonds : diamonds).append(colors ? " §bD " + getMainFormatting() + "/ " : " D / ");
			if(emeralds != 0) sb.append(colors ? "§a" + emeralds : emeralds).append(colors ? " §aE " + getMainFormatting() + "/ " : " E / ");
			if(sb.length() > 2) sb.deleteCharAt(sb.length() - 2);

			
			return sb.toString().trim();
			}catch(Exception e){
				e.printStackTrace();
				return "Error";
			}
		}
		else{
		
		return "";
		}
		
	}
	
    @Override
    public void render(int x, int y, RenderLocation renderLocation, boolean dummy) {
    if(getProperties().getSetting("mode").get() != ResourcesMode.EXTENDED) {
    	super.render(x, y, renderLocation, dummy);
    	return;  
    }
    if(!The5zigAPI.getAPI().isInWorld()) return;
    	int lineCount = 0;
        The5zigAPI.getAPI().getRenderHelper().drawString(getPrefix(), x, y);
        lineCount++;
       
        boolean colors = (boolean)getProperties().getSetting("showcolors").get();
        
        int ironIngots = The5zigAPI.getAPI().getItemCount("minecraft:iron_ingot");
		int goldIngots = The5zigAPI.getAPI().getItemCount("minecraft:gold_ingot");
		int diamonds = The5zigAPI.getAPI().getItemCount("minecraft:diamond");
		int emeralds = The5zigAPI.getAPI().getItemCount("minecraft:emerald");
		if(ironIngots != 0) {
			if(colors){
			 The5zigAPI.getAPI().getRenderHelper().drawString(ironIngots + " Iron", x, y + lineCount * 10, ChatColor.GRAY.getColor(), true);
			}
			else{
				The5zigAPI.getAPI().getRenderHelper().drawString(ironIngots + " Iron", x, y + lineCount * 10, true);
			}
			 lineCount++;
		}
		if(goldIngots != 0) {
			if(colors){
			 The5zigAPI.getAPI().getRenderHelper().drawString(goldIngots + " Gold", x, y + lineCount * 10, ChatColor.GOLD.getColor(), true);
			}
			else{
				 The5zigAPI.getAPI().getRenderHelper().drawString(goldIngots + " Gold", x, y + lineCount * 10, true);
			}
			 lineCount++;
		}
		if(diamonds != 0) {
			if(colors){
			 The5zigAPI.getAPI().getRenderHelper().drawString(diamonds + " Diamonds", x, y + lineCount * 10, ChatColor.AQUA.getColor(), true);
			}else{
				The5zigAPI.getAPI().getRenderHelper().drawString(diamonds + " Diamonds", x, y + lineCount * 10, true);
			}
			 lineCount++;
		}
		if(emeralds != 0) {
			if(colors){
				The5zigAPI.getAPI().getRenderHelper().drawString(emeralds + " Emeralds", x, y + lineCount * 10, ChatColor.GREEN.getColor(), true);
			}
			else{
				The5zigAPI.getAPI().getRenderHelper().drawString(emeralds + " Emeralds", x, y + lineCount * 10, true);
			}
			 
			lineCount++;
		}

    }
	
	@Override
	public String getName() {
		return Log.t("beezig.module.bed.resources");
	}
	
	
	
	@Override
	public void registerSettings() {
		

		getProperties().addSetting("mode", ResourcesMode.EXTENDED, ResourcesMode.class);
		getProperties().addSetting("showcolors", true);
		
	}

	@Override
	public boolean shouldRender(boolean dummy){		
		try{
			if(!(getGameMode() instanceof BED)) return false;
		return dummy || getGameMode().getState() == GameState.GAME;
		}catch(Exception e){
			return false;
		}
	}

}
