package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.hiveapi.stuff.bed.BEDRank;
import tk.roccodev.beezig.hiveapi.stuff.bp.BPRank;
import tk.roccodev.beezig.hiveapi.stuff.cai.CAIRank;
import tk.roccodev.beezig.hiveapi.stuff.dr.DRRank;
import tk.roccodev.beezig.hiveapi.stuff.grav.GRAVRank;
import tk.roccodev.beezig.hiveapi.stuff.hide.HIDERank;
import tk.roccodev.beezig.hiveapi.stuff.mimv.MIMVRank;
import tk.roccodev.beezig.hiveapi.stuff.sgn.SGNRank;
import tk.roccodev.beezig.hiveapi.stuff.sky.SKYRank;
import tk.roccodev.beezig.hiveapi.stuff.timv.TIMVRank;


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
				The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

				for(TIMVRank timvRank : TIMVRank.values()){
					The5zigAPI.getAPI().messagePlayer(Log.info + timvRank.getTotalDisplay()  + " §7- " + timvRank.getTotalDisplay().replaceAll(timvRank.getDisplay(), "") + timvRank.getStart());
				}

				The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
			}).start();
		}
		if(game.equalsIgnoreCase("bed")){
			new Thread(() -> {
				The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");
				for(BEDRank bedRank : BEDRank.values()){
					The5zigAPI.getAPI().messagePlayer(Log.info + "" + bedRank.getName() + "" + "§7 - " + "" + bedRank.getName().replaceAll(ChatColor.stripColor(bedRank.getName()), "") + "" + bedRank.getStart());
				}

				The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
			}).start();
		}
		if(game.equalsIgnoreCase("dr")){
			new Thread(() -> {
				The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

				for(DRRank drRank : DRRank.values()){
					The5zigAPI.getAPI().messagePlayer(Log.info + drRank.getTotalDisplay()  + " §7- " + drRank.getTotalDisplay().replaceAll(drRank.getDisplay(), "") + drRank.getStart());
				}

				The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
			}).start();
		}
		if(game.equalsIgnoreCase("cai")){
			new Thread(() -> {
				The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

				for(CAIRank caiRank : CAIRank.values()){
					The5zigAPI.getAPI().messagePlayer(Log.info + caiRank.getTotalDisplay()  + " §7- " + caiRank.getTotalDisplay().replaceAll(caiRank.getDisplay(), "") + caiRank.getStart());
				}

				The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
			}).start();
		}
		if(game.equalsIgnoreCase("hide")){
			new Thread(() -> {
				The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

				for(HIDERank hideRank : HIDERank.values()){
					if(hideRank == HIDERank.MASTER_OF_DISGUISE) {
						The5zigAPI.getAPI().messagePlayer(Log.info + hideRank.getTotalDisplay()  + " §7- §e§l" +  hideRank.getStart());
					}
					else {
						The5zigAPI.getAPI().messagePlayer(Log.info + hideRank.getTotalDisplay()  + " §7- " + hideRank.getTotalDisplay().replaceAll(hideRank.getDisplay(), "") + hideRank.getStart());
					}
					
				}

				The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
			}).start();
		}
		if(game.equalsIgnoreCase("sky")){
			new Thread(() -> {
				The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

				for(SKYRank skyRank : SKYRank.values()){
					The5zigAPI.getAPI().messagePlayer(Log.info + skyRank.getTotalDisplay()  + " §7- " + skyRank.getTotalDisplay().replaceAll(skyRank.getDisplay(), "") + skyRank.getStart());
				}

				The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
			}).start();
		}
		if(game.equalsIgnoreCase("grav")){
			new Thread(() -> {
				The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

				for(GRAVRank gravRank : GRAVRank.values()){
					The5zigAPI.getAPI().messagePlayer(Log.info + gravRank.getTotalDisplay()  + " §7- " + gravRank.getTotalDisplay().replaceAll(gravRank.getDisplay(), "") + gravRank.getStart());
				}

				The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
			}).start();
		}
		if(game.equalsIgnoreCase("mimv")){
			new Thread(() -> {
				The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

				for(MIMVRank mimvRank : MIMVRank.values()){
					The5zigAPI.getAPI().messagePlayer(Log.info + mimvRank.getTotalDisplay()  + " §7- " + mimvRank.getTotalDisplay().replaceAll(mimvRank.getDisplay(), "") + mimvRank.getStart());
				}

				The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
			}).start();
		}
		if(game.equalsIgnoreCase("bp")){
			new Thread(() -> {
				The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

				for(BPRank bpRank : BPRank.values()){
					The5zigAPI.getAPI().messagePlayer(Log.info + bpRank.getTotalDisplay()  + " §7- " + bpRank.getTotalDisplay().replaceAll(bpRank.getDisplay(), "") + bpRank.getStart());
				}

				The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
			}).start();
		}
		if(game.equalsIgnoreCase("sgn")){
			new Thread(() -> {
				The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

				for(SGNRank sgnRank : SGNRank.values()){
					The5zigAPI.getAPI().messagePlayer(Log.info + sgnRank.getTotalDisplay()  + " §7- " + sgnRank.getTotalDisplay().replaceAll(sgnRank.getDisplay(), "") + sgnRank.getRequiredPoints());
				}

				The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    " + "\n");
			}).start();
		}

		return true;
	}
}
