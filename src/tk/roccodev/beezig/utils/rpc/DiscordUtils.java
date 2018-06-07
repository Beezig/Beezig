package tk.roccodev.beezig.utils.rpc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.settings.Setting;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordUtils {

	public static Thread callbacksThread;
	public static boolean init;
	public static boolean shouldOperate = true;

	public static void init() {
		if (!shouldOperate)
			return;
		DiscordRPC lib = DiscordRPC.INSTANCE;
		String applicationId = "439523115383652372";

		DiscordEventHandlers handlers = new DiscordEventHandlers();
		handlers.disconnected = (errorCode, message) -> System.out.println("RPC: " + message);
		handlers.ready = user -> {

			System.out.println("Connected to Discord as " + user.username + "#" + user.discriminator + "! ("
					+ user.userId + ")");
			new Thread(() -> {
				try {
					URL url = new URL("http://botzig-atactest.7e14.starter-us-west-2.openshiftapps.com/check/"
											  + user.userId);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.addRequestProperty("User-Agent", Log.getUserAgent());
					if (conn.getResponseCode() == 404) {
						The5zigAPI.getAPI().messagePlayer(Log.info
																  + "You are using Discord, but you're not in our server! Make sure to join.\nInvite: §ehttp://discord.gg/se7zJsU");
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block

				}
			}, "Server Ping").start();

		};
		lib.Discord_Initialize(applicationId, handlers, true, "");

		callbacksThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				lib.Discord_RunCallbacks();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ignored) {

				}
			}
		}, "RPC Callbacks");
		callbacksThread.start();
	}

	/**
	 * 
	 * Updates the RPC presence.
	 * 
	 * @param newPresence
	 *            details
	 * @param opts:
	 *            State (Lobby, game etc.); 1: Image; 2: Max party size (e.g. Teams
	 *            of 4); 3: Party name (e.g., Aqua Team)
	 */
	public static void updatePresence(String newPresence, String... opts) {
		if (!shouldOperate)
			return;
		if (!Setting.DISCORD_RPC.getValue())
			return;
		new Thread(() -> {
			try {
				DiscordRichPresence presence = new DiscordRichPresence();
				presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
				presence.details = newPresence;

				presence.largeImageKey = "background";
				if (opts.length >= 1)
					presence.state = opts[0];
				if (opts.length >= 2)
					presence.smallImageKey = opts[1];
				if (opts.length >= 3) {
					presence.partyMax = Integer.parseInt(opts[2]);
					presence.partySize = 1;
				}
				if (opts.length >= 4)
					presence.partyId = opts[3];
				DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);

			} catch (Throwable e) {
				e.printStackTrace();
				DiscordUtils.shouldOperate = false;

			}
		}).start();

	}

}
