package tk.roccodev.zta.briefing;

import java.util.ArrayList;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.Log;

public class NewsServer {

	public static void serveNews(ArrayList<News> news) {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(news.size() == 0) return;
		The5zigAPI.getAPI()
				.messagePlayer(Log.info + "Here's your daily briefing!\n\n - " + ChatColor.ITALIC + "Our news:");
		for (News n : news) {
			The5zigAPI.getAPI().messagePlayer("\n§e" + ChatColor.UNDERLINE + n.getTitle());
			The5zigAPI.getAPI().messagePlayer("§e" + n.getContent());
			
		}

	}

}
