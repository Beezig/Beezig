package eu.beezig.core.command;

import com.mojang.authlib.GameProfile;
import eu.beezig.core.BeezigMain;
import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.utils.TabCompletionUtils;
import eu.beezig.core.utils.acr.ChatReason;
import eu.the5zig.mod.The5zigAPI;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChatReportCommand implements Command {

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

        if (!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
        BeezigMain.crInteractive = false;
        BeezigMain.lrRS = "";
        BeezigMain.checkForNewLR = false;
        BeezigMain.lrID = "";
        if (args.length >= 2) {
            BeezigMain.lrPL = args[0];

            String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replace(" ", "");
            if (ChatReason.is(reason.toUpperCase())) {
                BeezigMain.crInteractive = true;
                BeezigMain.lrRS = reason;
                The5zigAPI.getAPI().sendPlayerMessage("/chatreport " + args[0]);
                return true;
            } else {
                The5zigAPI.getAPI().messagePlayer(Log.error + "Invalid reason. Available reasons: " + String.join(", ", Stream.of(ChatReason.values()).map(ChatReason::toString).collect(Collectors.toList())));
            }
        }
        return false;


    }

    @Override
    public List<String> addTabCompletionOptions(GameProfile sender, String[] args) {
        if (args.length == 2)
            return TabCompletionUtils.matching(args, Arrays.stream(ChatReason.values()).map(ChatReason::toString).collect(Collectors.toList()));
        return null;
    }
}
