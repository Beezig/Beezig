package tk.roccodev.zta.utils.rpc;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordEventHandlers.OnReady;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordUser;
import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.settings.Setting;

public class DiscordUtils {

	public static Thread callbacksThread;
	public static boolean init;
	
	public static void init() {
		DiscordRPC lib = DiscordRPC.INSTANCE;
		String applicationId = "439523115383652372";

		DiscordEventHandlers handlers = new DiscordEventHandlers();
		handlers.disconnected = new DiscordEventHandlers.OnStatus() {
			
			@Override
			public void accept(int errorCode, String message) {
				System.out.println("RPC: " + message);
				
			}
		};
		handlers.ready = new OnReady() {

			
		

			@Override
			public void accept(DiscordUser user) {
				
				System.out.println("Connected to Discord as " + user.username + "#" + user.discriminator + "! (" + user.userId + ")");
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							URL url = new URL("http://botzig-atactest.7e14.starter-us-west-2.openshiftapps.com/check/" + user.userId);
							HttpURLConnection conn = (HttpURLConnection) url.openConnection();
							conn.addRequestProperty("User-Agent", "Beezig v" + ZTAMain.BEEZIG_VERSION + " on " + System.getProperty("os.name"));
							if(conn.getResponseCode() == 404) {
								The5zigAPI.getAPI().messagePlayer(Log.info + "You are using Discord, but you're not in our server! Make sure to join.\nInvite: §ehttp://discord.gg/se7zJsU");
							}
						
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, "Server Ping").start();
				
			}
		};
		lib.Discord_Initialize(applicationId, handlers, true, "");


		callbacksThread = new Thread(new Runnable() {
        	@Override
        	public void run() {
        		 while (!Thread.currentThread().isInterrupted()) {
                     lib.Discord_RunCallbacks();
                     try {
                         Thread.sleep(2000);
                     } catch (InterruptedException ignored) {
                    
                     }
        		 }
        	}
        }, "RPC Callbacks");
        callbacksThread.start();
	}
	
	/**
	 * 
	 * Updates the RPC presence.
	 * 
	 * @param Presence details
	 * @param 0: State (Lobby, game etc.); 1: Image; 2: Max party size (e.g. Teams of 4); 3: Party name (e.g., Aqua Team)
	 */
	public static void updatePresence(String newPresence, String... opts) {
		if(!Setting.DISCORD_RPC.getValue()) return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				DiscordRichPresence presence = new DiscordRichPresence();
		        presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
		        presence.details = newPresence;
		        presence.largeImageKey = "background";
		        if(opts.length >= 1) presence.state = opts[0];
		        if(opts.length >= 2) presence.smallImageKey = opts[1];
		        if(opts.length >= 3) presence.partyMax = Integer.parseInt(opts[2]);
		        if(opts.length >= 4) presence.partyId = opts[3];
		        DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);
			}
		}).start();
	
	}
	
}
