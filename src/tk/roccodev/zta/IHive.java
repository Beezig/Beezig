package tk.roccodev.zta;

import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.ServerInstance;
import tk.roccodev.zta.listener.BEDListener;
import tk.roccodev.zta.listener.CAIListener;
import tk.roccodev.zta.listener.DRListener;
import tk.roccodev.zta.listener.GiantListener;
import tk.roccodev.zta.listener.HIDEListener;
import tk.roccodev.zta.listener.HiveListener;
import tk.roccodev.zta.listener.MIMVListener;
import tk.roccodev.zta.listener.SKYListener;
import tk.roccodev.zta.listener.TIMVListener;
import tk.roccodev.zta.notes.NotesManager;
import tk.roccodev.zta.utils.rpc.DiscordUtils;

public class IHive extends ServerInstance {

	@Override
	public String getName() {
		return "HiveMC";
	}

	@Override
	public String getConfigName() {
		return "hive";
	}

	@Override
	public boolean handleServer(String host, int port) {
		if(host.toUpperCase().contains("HIVEMC.") || host.toUpperCase().endsWith("HIVE.SEXY")) {
			System.out.println("Joined Hive.");
			DiscordUtils.init();
			DiscordUtils.updatePresence("Relaxing in the Hub", "In Lobby", "lobby");
		return true;	
		}
		return false;
	}

	@Override
	public void registerListeners() {
		// TODO Auto-generated method stub
		getGameListener().registerListener(new TIMVListener());
		getGameListener().registerListener(new HiveListener());
		getGameListener().registerListener(new DRListener());
		getGameListener().registerListener(new BEDListener());
		getGameListener().registerListener(new GiantListener());
		getGameListener().registerListener(new HIDEListener());
		getGameListener().registerListener(new CAIListener());
		getGameListener().registerListener(new SKYListener());
		getGameListener().registerListener(new MIMVListener());
	}

	public static void genericReset(String... optionalParams){
		DiscordUtils.updatePresence("Relaxing in the Hub", "In Lobby", "lobby");
	}
	
	public static void genericJoin(String... optionalParams){
		if(NotesManager.HR1cm5z){
			The5zigAPI.getAPI().messagePlayer(
					new String(DatatypeConverter.parseBase64Binary("V293ISBJdHNOaWtsYXNzIHRvZGF5IHR1cm5zIA=="))
					+
					(Calendar.getInstance().get(Calendar.YEAR) - 0x7D0)
					+
					new String(DatatypeConverter.parseBase64Binary("ISBNYWtlIHN1cmUgdG8gd2lzaCBoaW0gYSBoYXBweSBiaXJ0aGRheSE="))
					);
		}
	}
	

}
