package tk.roccodev.zta.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.notes.NotesManager;

public class AddNoteCommand implements Command{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "addnote";
	}

	@Override
	public String[] getAliases() {
		String[] aliases = {"/note", "/addnote"};
		return aliases;
	}

	@Override
	public void execute(String[] args) {
		if( args.length == 0 ){
			The5zigAPI.getAPI().messagePlayer("§a[TIMV Plugin] §eNote may not be empty.");
			return;
		}
		StringBuilder note = new StringBuilder();
		for(String s : args){
			note.append(s).append(" ");
		}
		NotesManager.notes.add(note.toString().trim());
		The5zigAPI.getAPI().messagePlayer("§a[TIMV Plugin] §eSuccesfully added note.");
	}

	

}
