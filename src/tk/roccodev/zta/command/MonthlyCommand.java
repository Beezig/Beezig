package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.ActiveGame;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.hiveapi.HiveAPI;

public class MonthlyCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "monthly";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/monthly"};
	}

	@Override
	public boolean execute(String[] args) {
		if(!(ActiveGame.is("dr")) && !(ActiveGame.is("timv"))) return false;
		if(args.length == 1){
			try{
				int index = Integer.parseInt(args[0]) - 1;	
				new Thread(new Runnable(){
					@Override
					public void run(){
						String mode = null;
						String unit = null;
						if(ActiveGame.is("timv")){
							mode = "TIMV";
							unit = "karma.";
						}
						else if(ActiveGame.is("dr")){
							mode = "DR";
							unit = "points.";
						}
						String[] data = HiveAPI.getMonthlyLeaderboardsPlayerInfo(index, mode).split(",");
						switch(ActiveGame.current()){
						
						case "DR": The5zigAPI.getAPI().messagePlayer(Log.info + "§B#" + (index+1) + "§3 is §B" + data[0] + "§3 with §B" + data[1] + " §3" + unit + " (PpG: §B" + data[2] + " §3| W/L: §B" + data[3] + "%§3)");
									break;
						
						case "TIMV": The5zigAPI.getAPI().messagePlayer(Log.info + "§B#" + (index+1) + "§3 is §B" + data[0] + "§3 with §B" + data[1] + " §3" + unit + " (K/R: §B" + data[2] + " §3| T/R: " + data[3] + "%§3)");
									break;
						
						}
					}
				}).start();
			} catch (Exception e){
				The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /monthly [position] | Use /records to find out a players' rank.");
				return true;
			}
		}
		else{
			The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /monthly [position] | Use /records to find out a players' rank.");
		}		
		return true;
	}

	

}
