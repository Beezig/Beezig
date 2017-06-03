package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.IHive;
import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.notes.NotesManager;

public class NotesCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "notes";
	}

	@Override
	public String[] getAliases() {
		String[] aliases = {"/notes"};
		return aliases;
	}

	@Override
	public void execute(String[] args) {
		if(The5zigAPI.getAPI().getActiveServer() instanceof IHive && ZTAMain.isTIMV){
			The5zigAPI.getAPI().messagePlayer("§a[TIMV Plugin] §eNotes:");
			for(String s : NotesManager.notes){
				The5zigAPI.getAPI().messagePlayer("§e - §r" + s);
			}
		}
		
		
		
		
	}

	

}
