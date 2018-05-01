package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.hiveapi.stuff.bed.BEDRank;
import tk.roccodev.zta.hiveapi.stuff.cai.CAIRank;
import tk.roccodev.zta.hiveapi.stuff.dr.DRRank;
import tk.roccodev.zta.hiveapi.stuff.grav.GRAVRank;
import tk.roccodev.zta.hiveapi.stuff.hide.HIDERank;
import tk.roccodev.zta.hiveapi.stuff.sky.SKYRank;
import tk.roccodev.zta.hiveapi.stuff.timv.TIMVRank;


public class RanksCommand implements Command {
	@Override
	public String getName() {
		return "ranks";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/ranks", ""};
	}

	@Override
	public boolean execute(String[] args) {

		String game;
		if(args.length == 0){
			game = ActiveGame.current();
		} else game = args[0];

		if(game.equalsIgnoreCase("timv")){
			new Thread(() -> {
				The5zigAPI.getAPI().messagePlayer("\n" + "    §e§m                                                                                    ");

				for(TIMVRank timvRank : TIMVRank.values()){
					The5zigAPI.getAPI().messagePlayer(Log.info + timvRank.getTotalDisplay()  + " §e- " + timvRank.getTotalDisplay().replaceAll(timvRank.getDisplay(), "") + timvRank.getStart());
				}

				The5zigAPI.getAPI().messagePlayer("    §e§m                                                                                    " + "\n");
			}).start();
		}
		if(game.equalsIgnoreCase("bed")){
			new Thread(() -> {
				The5zigAPI.getAPI().messagePlayer("\n" + "    §e§m                                                                                    ");

				for(BEDRank bedRank : BEDRank.values()){
					The5zigAPI.getAPI().messagePlayer(Log.info + bedRank.name()  + " §e- " + bedRank.name().replaceAll(ChatColor.stripColor(bedRank.name()), "") + bedRank.getStart());
				}

				The5zigAPI.getAPI().messagePlayer("    §e§m                                                                                    " + "\n");
			}).start();
		}
		if(game.equalsIgnoreCase("dr")){
			new Thread(() -> {
				The5zigAPI.getAPI().messagePlayer("\n" + "    §e§m                                                                                    ");

				for(DRRank drRank : DRRank.values()){
					The5zigAPI.getAPI().messagePlayer(Log.info + drRank.getTotalDisplay()  + " §e- " + drRank.getTotalDisplay().replaceAll(drRank.getDisplay(), "") + drRank.getStart());
				}

				The5zigAPI.getAPI().messagePlayer("    §e§m                                                                                    " + "\n");
			}).start();
		}
		if(game.equalsIgnoreCase("cai")){
			new Thread(() -> {
				The5zigAPI.getAPI().messagePlayer("\n" + "    §e§m                                                                                    ");

				for(CAIRank caiRank : CAIRank.values()){
					The5zigAPI.getAPI().messagePlayer(Log.info + caiRank.getTotalDisplay()  + " §e- " + caiRank.getTotalDisplay().replaceAll(caiRank.getDisplay(), "") + caiRank.getStart());
				}

				The5zigAPI.getAPI().messagePlayer("    §e§m                                                                                    " + "\n");
			}).start();
		}
		if(game.equalsIgnoreCase("hide")){
			new Thread(() -> {
				The5zigAPI.getAPI().messagePlayer("\n" + "    §e§m                                                                                    ");

				for(HIDERank hideRank : HIDERank.values()){
					The5zigAPI.getAPI().messagePlayer(Log.info + hideRank.getTotalDisplay()  + " §e- " + hideRank.getTotalDisplay().replaceAll(hideRank.getDisplay(), "") + hideRank.getStart());
				}

				The5zigAPI.getAPI().messagePlayer("    §e§m                                                                                    " + "\n");
			}).start();
		}
		if(game.equalsIgnoreCase("sky")){
			new Thread(() -> {
				The5zigAPI.getAPI().messagePlayer("\n" + "    §e§m                                                                                    ");

				for(SKYRank skyRank : SKYRank.values()){
					The5zigAPI.getAPI().messagePlayer(Log.info + skyRank.getTotalDisplay()  + " §e- " + skyRank.getTotalDisplay().replaceAll(skyRank.getDisplay(), "") + skyRank.getStart());
				}

				The5zigAPI.getAPI().messagePlayer("    §e§m                                                                                    " + "\n");
			}).start();
		}
		if(game.equalsIgnoreCase("grav")){
			new Thread(() -> {
				The5zigAPI.getAPI().messagePlayer("\n" + "    §e§m                                                                                    ");

				for(GRAVRank gravRank : GRAVRank.values()){
					The5zigAPI.getAPI().messagePlayer(Log.info + gravRank.getTotalDisplay()  + " §e- " + gravRank.getTotalDisplay().replaceAll(gravRank.getDisplay(), "") + gravRank.getStart());
				}

				The5zigAPI.getAPI().messagePlayer("    §e§m                                                                                    " + "\n");
			}).start();
		}

		return true;
	}
}
