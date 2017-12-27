package tk.roccodev.zta.command;

import tk.roccodev.zta.ActiveGame;

public class TeamsCommand implements Command {
	@Override
	public String getName() {
		return "teams";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/teams"};
	}

	@Override
	public boolean execute(String[] args) {
		if(!(ActiveGame.is("bed"))) return false;
		//On hold until a workaround for getDisplayName not being null is found.
		/*The5zigAPI.getAPI().messagePlayer(Log.info + "Gathering data...");
		new Thread(new Runnable(){
			@Override
			public void run(){
				// 0 1 2 3 4 5 6 7 8 9 a b c d e f
				List<String> teams = new ArrayList<>(Arrays.asList("0123456789abcdef".split("")));
				List<Long> teampoints = new ArrayList<>(Collections.nCopies(16, 0L));
				The5zigAPI.getLogger().info(teams);
				The5zigAPI.getLogger().info(teampoints);
				for(NetworkPlayerInfo npi : The5zigAPI.getAPI().getServerPlayers()){
					ApiBED api = new ApiBED(npi.getGameProfile().getName());
					The5zigAPI.getLogger().info(npi.getDisplayName());
					The5zigAPI.getLogger().info(npi.getGameProfile().getName());
					teampoints.set(
							teams.indexOf(
									String.valueOf(npi.getDisplayName().charAt(3))),
							api.getPoints() +
									teampoints.get(teams.indexOf(
											String.valueOf(npi.getDisplayName().charAt(3)))));
				}
				The5zigAPI.getLogger().info(teampoints);
				for(int i = 0; i < 16; i++){
					teams.set(i, (teampoints.get(i) + "," + teams.get(i)));
				}
				Collections.sort(teams);
				The5zigAPI.getLogger().info(teams);
				for(String s : teams){
					if(!s.split(",")[0].equals("0")){
						The5zigAPI.getAPI().messagePlayer(ChatColor.valueOf(s.split(",")[1]) + "Team " + ChatColor.valueOf(s.split(",")[1]).getName() + ChatColor.RESET + "§e: " + s.split(",")[0]);
					}
				}
			}
		}).start();
		*/
		return true;
	}
}
