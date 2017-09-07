package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.games.BED;
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.games.TIMV;

public class ReVoteCommand implements Command{
	@Override
	public String getName() {
		return "revote";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/revote", "/rev"};
	}
	

	@Override
	public boolean execute(String[] args) {
		
		//better way?
		TIMV.hasVoted = false;
		DR.hasVoted = false;
		BED.hasVoted = false;
		//Giant.hasVoted = false;
		
		The5zigAPI.getAPI().sendPlayerMessage("/v");
		return true;
		
	}
}
