package tk.roccodev.beezig.utils.ws;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.settings.Setting;
import tk.roccodev.beezig.utils.NotificationManager;

public class Client extends WebSocketClient{

	public Client(URI serverUri) {
		super(serverUri);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		// TODO Auto-generated method stub
		System.out.println("Connected!");
		this.send("I am " + The5zigAPI.getAPI().getGameProfile().getName());
		
	}

	@Override
	public void onMessage(String message) {
		String data[] = message.split("\\:", 2);
		if(data.length == 0) return;
		if(data[0].equals("newReport") && Setting.MOD_REPORT_NOTIFICATION.getValue() && BeezigMain.isStaffChat()) {
			String[] data2 = data[1].split("§");
			String users = data2[0].trim();
			String reason = data2[1].trim();
			The5zigAPI.getAPI().messagePlayer(Log.info + "§b" + users + "§3 " + (users.split(",").length == 0 ? "is" : "are") + " being reported for §b" + reason + "§3.");
			if(Setting.PM_PING.getValue()) {
				The5zigAPI.getAPI().playSound("note.pling", 1f);
			}
			if(Setting.PM_NOTIFICATION.getValue() && !NotificationManager.isInGameFocus()) {
				NotificationManager.sendNotification("New Report Received", users + " reported for " + reason);
			}
		}
		else if(data[0].equals("0nline cl1ents")) {
			The5zigAPI.getAPI().messagePlayer(Log.info + "There are §b" + data[1] + "§3 connected clients.");
		}
		else if(data[0].equals("lookingForParty")) {
			String data2[] = data[1].split("§");
			String who = data2[0];
			String mode = data2[1];
			String amount = data2[2];
			The5zigAPI.getAPI().messagePlayer(Log.info + "§b" + who.trim() + "§3 is looking for §b" + amount.trim() + "§3 player(s) to play §b" + mode.trim() + "§3.");
		}
		else if(data[0].equals("partyAccepted")) {
			The5zigAPI.getAPI().messagePlayer(Log.info + "§b" + data[1].trim() + "§3 accepted your party invite!");
		}
		
		
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("Disconnected from WebSocket (" + code + "): " + reason + " [" + remote + "]");
		
	}

	@Override
	public void onError(Exception ex) {
		// TODO Auto-generated method stub
		ex.printStackTrace();
	}

}
