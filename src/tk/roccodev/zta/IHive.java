package tk.roccodev.zta;

import eu.the5zig.mod.server.ServerInstance;
import tk.roccodev.zta.listener.BEDListener;
import tk.roccodev.zta.listener.DRListener;
import tk.roccodev.zta.listener.GiantListener;
import tk.roccodev.zta.listener.HiveListener;
import tk.roccodev.zta.listener.TIMVListener;

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
		return host.toUpperCase().contains("HIVEMC.") || host.toUpperCase().endsWith("HIVE.SEXY");
	}

	@Override
	public void registerListeners() {
		// TODO Auto-generated method stub
		getGameListener().registerListener(new TIMVListener());
		getGameListener().registerListener(new HiveListener());
		getGameListener().registerListener(new DRListener());
		getGameListener().registerListener(new BEDListener());
		getGameListener().registerListener(new GiantListener());
	}


}
