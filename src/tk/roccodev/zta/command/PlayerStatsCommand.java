package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.games.BED;
import tk.roccodev.zta.hiveapi.stuff.bed.BEDRank;
import tk.roccodev.zta.hiveapi.stuff.timv.TIMVRank;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiBED;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiHiveGlobal;
import tk.roccodev.zta.hiveapi.wrapper.modes.ApiTIMV;

import java.util.*;

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

		if(ActiveGame.is("timv") || args[0].equalsIgnoreCase("timv")){
			The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
			new Thread(new Runnable(){
				@Override
				public void run(){
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

					concurrentSort(points,	points,title,name);

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
				}
			}).start();
		}
		if(ActiveGame.is("bed") || args[0].equalsIgnoreCase("bed")){
			The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
			new Thread(new Runnable(){
				@Override
				public void run(){
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

					concurrentSort(points,	points,titlecolor,title,name);

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
				}
			}).start();
		}
		
		return true;
	}

	public static <T extends Comparable<T>> void concurrentSort( final List<T> key, List<?>... lists){

		//
		//
		//	Source: https://ideone.com/cXdw6T
		//
		//

		// Do validation
		if(key == null || lists == null)
			throw new NullPointerException("key cannot be null.");

		for(List<?> list : lists)
			if(list.size() != key.size())
				throw new IllegalArgumentException("all lists must be the same size");

		// Lists are size 0 or 1, nothing to sort
		if(key.size() < 2)
			return;

		// Create a List of indices
		List<Integer> indices = new ArrayList<Integer>();
		for(int i = 0; i < key.size(); i++)
			indices.add(i);

		// Sort the indices list based on the key
		Collections.sort(indices, new Comparator<Integer>(){
			@Override public int compare(Integer i, Integer j) {
				return key.get(i).compareTo(key.get(j));
			}
		});

		Map<Integer, Integer> swapMap = new HashMap<Integer, Integer>(indices.size());
		List<Integer> swapFrom = new ArrayList<Integer>(indices.size()),
				swapTo   = new ArrayList<Integer>(indices.size());

		// create a mapping that allows sorting of the List by N swaps.
		for(int i = 0; i < key.size(); i++){
			int k = indices.get(i);
			while(i != k && swapMap.containsKey(k))
				k = swapMap.get(k);

			swapFrom.add(i);
			swapTo.add(k);
			swapMap.put(i, k);
		}

		// use the swap order to sort each list by swapping elements
		for(List<?> list : lists)
			for(int i = 0; i < list.size(); i++)
				Collections.swap(list, swapFrom.get(i), swapTo.get(i));
	}
}
