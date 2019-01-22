package eu.beezig.core.command;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.notes.NotesManager;
import eu.the5zig.mod.The5zigAPI;

public class NotesCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "notes";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/notes"};
    }

    @Override
    public boolean execute(String[] args) {
        if (The5zigAPI.getAPI().getActiveServer() instanceof IHive && ActiveGame.is("timv")) {
            The5zigAPI.getAPI().messagePlayer("\n" + Log.info + "Notes:");
            for (String s : NotesManager.notes) {
                The5zigAPI.getAPI().messagePlayer("§7 - §b" + s);
            }
            The5zigAPI.getAPI().messagePlayer("\n");
            return true;
        } else {
            return false;
        }


    }


}
