package tk.roccodev.zta;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.LoadEvent;
import eu.the5zig.mod.modules.Category;
import eu.the5zig.mod.plugin.Plugin;
import tk.roccodev.zta.modules.KarmaCounterItem;
import tk.roccodev.zta.modules.KarmaItem;

@Plugin(name="TIMVPlugin", version="1.0")
public class ZTAMain {
	
	@EventHandler(priority = EventHandler.Priority.LOW)
	public void onLoad(LoadEvent event) { 

		The5zigAPI.getLogger().info("Loaded TIMVPlugin");
		The5zigAPI.getAPI().registerModuleItem(this, "karma", KarmaItem.class, Category.SERVER_GENERAL);
		The5zigAPI.getAPI().registerModuleItem(this, "karmacounter", KarmaCounterItem.class, Category.SERVER_GENERAL);
		The5zigAPI.getAPI().registerServerInstance(this, IHive.class);
		
		
	}



	

}
