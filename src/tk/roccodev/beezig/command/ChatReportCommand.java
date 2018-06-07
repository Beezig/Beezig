package tk.roccodev.beezig.command;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.utils.acr.ChatReason;

public class ChatReportCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "cr";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"/chatreport", "/reportchat"};
	}
	

	@Override
	public boolean execute(String[] args) {
		
		if(!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
		if(args.length == 2){
			String pl = args[0];
			if(ChatReason.is(args[1].toUpperCase())) {
				BeezigMain.crInteractive = true;
				BeezigMain.lrRS = args[1].toLowerCase();
				The5zigAPI.getAPI().sendPlayerMessage("/chatreport " + args[0]);
				return true;
			}
			else {
				The5zigAPI.getAPI().messagePlayer(Log.error + "Invalid reason. Available reasons: " + String.join(", ", Stream.of(ChatReason.values()).map(ChatReason::toString).collect(Collectors.toList())));
			}
		}
		return false;
		
		
	}

	

	

}
