package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.notes.NotesManager;
import tk.roccodev.beezig.utils.autogg.Triggers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UptimeCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "uptime";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/uptime"};
    }


    @Override
    public boolean execute(String[] args) {

        The5zigAPI.getAPI().messagePlayer(Log.bar + "\n");
        The5zigAPI.getAPI().messagePlayer(Log.info + (IHive.joined == 0 ? "§3Not on Hive" : "On Hive since §b" + new SimpleDateFormat("HH:mm").format(new Date(IHive.joined))) + "§3.");
        The5zigAPI.getAPI().messagePlayer(Log.info + (Triggers.lastPartyJoined == 0 ? "§3Not in a party" : "In a party since §b" + new SimpleDateFormat("HH:mm").format(new Date(Triggers.lastPartyJoined))) + "§3.");
        The5zigAPI.getAPI().messagePlayer("\n" + Log.bar);

        return true;
    }


}
