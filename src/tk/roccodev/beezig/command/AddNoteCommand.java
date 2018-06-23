package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.notes.NotesManager;

public class AddNoteCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "addnote";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/note", "/addnote"};
    }


    @Override
    public boolean execute(String[] args) {

        if (!ActiveGame.is("timv")) return false;
        if (!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
        if (args.length == 0) {
            The5zigAPI.getAPI().messagePlayer(Log.info + "Note may not be empty.");
            return true;
        }
        StringBuilder note = new StringBuilder();
        for (String s : args) {
            note.append(s).append(" ");
        }
        NotesManager.notes.add(note.toString().trim());
        The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully added note.");

        return true;
    }


}
