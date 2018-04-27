package tk.roccodev.zta.utils.rpc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordEventHandlers.OnReady;
import tk.roccodev.zta.settings.Setting;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordUser;

public class DiscordUtils {

	public static void init() {
		DiscordRPC lib = DiscordRPC.INSTANCE;
		String applicationId = "439523115383652372";

		DiscordEventHandlers handlers = new DiscordEventHandlers();
		handlers.ready = new OnReady() {

		

			@Override
			public void accept(DiscordUser user) {
				System.out.println("Connected to Discord!");
				
			}
		};
		lib.Discord_Initialize(applicationId, handlers, true, "");

        new Thread(new Runnable() {
        	@Override
        	public void run() {
        		 while (!Thread.currentThread().isInterrupted()) {
                     lib.Discord_RunCallbacks();
                     try {
                         Thread.sleep(2000);
                     } catch (InterruptedException ignored) {}
        		 }
        	}
        }, "RPC Callbacks").start(); 
	}
	
	/**
	 * 
	 * Updates the RPC presence.
	 * 
	 * @param Presence details
	 * @param 0: State (Lobby, game etc.)
	 */
	public static void updatePresence(String newPresence, String... opts) {
		if(!Setting.DISCORD_RPC.getValue()) return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				DiscordRichPresence presence = new DiscordRichPresence();
		        presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
		        presence.details = newPresence;
		        DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);
			}
		}).start();
	
	}
	
}
