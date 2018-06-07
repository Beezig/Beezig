package tk.roccodev.beezig;

import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.ServerInstance;
import tk.roccodev.beezig.listener.BEDListener;
import tk.roccodev.beezig.listener.BPListener;
import tk.roccodev.beezig.listener.CAIListener;
import tk.roccodev.beezig.listener.DRListener;
import tk.roccodev.beezig.listener.GRAVListener;
import tk.roccodev.beezig.listener.GiantListener;
import tk.roccodev.beezig.listener.HIDEListener;
import tk.roccodev.beezig.listener.HiveListener;
import tk.roccodev.beezig.listener.MIMVListener;
import tk.roccodev.beezig.listener.SGNListener;
import tk.roccodev.beezig.listener.SKYListener;
import tk.roccodev.beezig.listener.TIMVListener;
import tk.roccodev.beezig.notes.NotesManager;
import tk.roccodev.beezig.utils.rpc.DiscordUtils;

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
		if (host.toUpperCase().contains("HIVEMC.") || host.toUpperCase().endsWith("HIVE.SEXY")) {
			System.out.println("Joined Hive.");
			try {
				DiscordUtils.init();
			} catch (Throwable e) {
				DiscordUtils.shouldOperate = false;
			} finally {
				DiscordUtils.updatePresence("Relaxing in the Hub", "In Lobby", "lobby");
			}
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
		getGameListener().registerListener(new GRAVListener());
		getGameListener().registerListener(new BPListener());
		getGameListener().registerListener(new SGNListener());
		
	}

	public static void genericReset(String... optionalParams) {
		DiscordUtils.updatePresence("Relaxing in the Hub", "In Lobby", "lobby");
	}

	public static void genericJoin(String... optionalParams) {
		if (NotesManager.HR1cm5z) {
			The5zigAPI.getAPI().messagePlayer(
					new String(DatatypeConverter.parseBase64Binary("V293ISBJdHNOaWtsYXNzIHRvZGF5IHR1cm5zIA=="))
							+ (Calendar.getInstance().get(Calendar.YEAR) - 0x7D0) + new String(DatatypeConverter
									.parseBase64Binary("ISBNYWtlIHN1cmUgdG8gd2lzaCBoaW0gYSBoYXBweSBiaXJ0aGRheSE=")));
		}
	}

}
