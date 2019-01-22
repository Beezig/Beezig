package eu.beezig.core.command;

import com.mojang.authlib.GameProfile;
import eu.beezig.core.advancedrecords.AdvancedRecords;
import eu.beezig.core.advancedrecords.anywhere.AdvancedRecordsAnywhere;
import eu.the5zig.mod.The5zigAPI;
import eu.beezig.core.Log;

import java.util.List;

public class StatsOverlayCommand implements Command {
    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/records", "/stats"};
    }

    @Override
    public boolean execute(String[] args) {
        if (AdvancedRecords.isRunning) {
            The5zigAPI.getAPI().messagePlayer(Log.error + "Advanced Records is already running.");
            return true;
        }
        if(args.length == 2) {
            AdvancedRecordsAnywhere.run(args[0].trim(), args[1].trim());
            return true;
        }
        else AdvancedRecords.player = args.length == 0
                ? The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "")
                : args[0].trim();
        return false;
    }

    @Override
    public List<String> addTabCompletionOptions(GameProfile sender, String[] args) {
        return null;
    }
}
