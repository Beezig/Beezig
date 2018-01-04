package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.games.BED;
import tk.roccodev.zta.hiveapi.stuff.bed.BEDRank;
import tk.roccodev.zta.hiveapi.stuff.timv.TIMVRank;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiBED;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiHiveGlobal;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiTIMV;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatsCommand implements Command {
	@Override
	public String getName() {
		return "playerstats";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/playerstats", "/ps"};
	}

	@Override
	public boolean execute(String[] args) {

		String game;
		if(args.length == 0){
			game = ActiveGame.current();
		} else game = args[0];

		if(game.equalsIgnoreCase("timv")){
			The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
			new Thread(() -> {
				List<Long> points = new ArrayList<>();
				List<String> title = new ArrayList<>();
				List<String> name = new ArrayList<>();

				for(NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
					try {
						ApiTIMV apiTIMV = new ApiTIMV(npi.getGameProfile().getName());
						ApiHiveGlobal apiHIVE = new ApiHiveGlobal(npi.getGameProfile().getName());
						points.add(apiTIMV.getKarma());
						title.add(TIMVRank.getFromDisplay(apiTIMV.getTitle()).getTotalDisplay());
						name.add(apiHIVE.getNetworkRankColor() + npi.getGameProfile().getName());
					}catch (Exception e){
						//e.printStackTrace();
					}
				}

				APIUtils.concurrentSort(points,	points,title,name);

				The5zigAPI.getAPI().messagePlayer("\n" + Log.info + "Playerstats: (" + name.size() + ")");
				for(int i = name.size()-1; i != 0; i--){
					try {
						if (points.get(i) != 0) {
							The5zigAPI.getAPI().messagePlayer(Log.info + title.get(i).replaceAll(ChatColor.stripColor(title.get(i)), "") + points.get(i) + " §e- " + title.get(i) + " §r" + name.get(i));
						}
					} catch (Exception e){
						//e.printStackTrace();
					}
				}
				The5zigAPI.getAPI().messagePlayer("");
			}).start();
		}
		if(game.equalsIgnoreCase("bed")){
			The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
			new Thread(() -> {
				List<Long> points = new ArrayList<>();
				List<String> titlecolor = new ArrayList<>();
				List<String> title = new ArrayList<>();
				List<String> name = new ArrayList<>();

				for(NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()) {
					try {
						ApiBED apiBED = new ApiBED(npi.getGameProfile().getName());
						ApiHiveGlobal apiHIVE = new ApiHiveGlobal(npi.getGameProfile().getName());
						points.add(apiBED.getPoints());

						BEDRank rank = BEDRank.getRank(apiBED.getPoints());
						if(rank != null){
							int level = rank.getLevel((int)apiBED.getPoints());
							titlecolor.add(rank.getName().replaceAll(ChatColor.stripColor(rank.getName()), ""));
							title.add(BED.NUMBERS[level] + " " +rank.getName());
						}

						name.add(apiHIVE.getNetworkRankColor() + npi.getGameProfile().getName());
					}catch (Exception e){
						//e.printStackTrace();
					}
				}

				APIUtils.concurrentSort(points,	points,titlecolor,title,name);

				The5zigAPI.getAPI().messagePlayer("\n" + Log.info + "Playerstats: (" + name.size() + ")");
				for(int i = name.size()-1; i != 0; i--){
					try {
						if (points.get(i) != 0) {
							The5zigAPI.getAPI().messagePlayer(Log.info + titlecolor.get(i) + points.get(i) + " §e- " + titlecolor.get(i) + title.get(i) + " §r" + name.get(i));
						}
					} catch (Exception e){
						//e.printStackTrace();
					}
				}
				The5zigAPI.getAPI().messagePlayer("");
			}).start();
		}
		
		return true;
	}
}
