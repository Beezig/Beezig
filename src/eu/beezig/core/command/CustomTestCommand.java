package eu.beezig.core.command;

import com.mojang.authlib.GameProfile;
import eu.beezig.core.BeezigMain;
import eu.beezig.core.Log;
import eu.beezig.core.api.BeezigAPI;
import eu.beezig.core.games.TIMV;
import eu.beezig.core.utils.TIMVTest;
import eu.beezig.core.utils.TabCompletionUtils;
import eu.the5zig.mod.The5zigAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomTestCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "customtest";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/customtest", "/ctest"};
    }

    @Override
    public boolean execute(String[] args) {
        if(BeezigMain.hasExpansion && args.length == 0) {
            BeezigAPI.get().getListener().displayTIMVTestGui();
            return true;
        }

        String mode = args[0];
        StringBuilder msg = new StringBuilder();

        List<String> args1 = new ArrayList<>(Arrays.asList(args));
        args1.remove(0);

        args1.forEach(s -> msg.append(s).append(" "));

        String testMessage = msg.toString().trim();

        switch (mode.toLowerCase()) {

            case "add":
                try {
                    TIMVTest.add(testMessage);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    The5zigAPI.getAPI().messagePlayer(Log.error + "Failed to add message.");
                }
                The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully added message.");
                break;
            case "remove":
                try {
                    TIMVTest.remove(testMessage);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    The5zigAPI.getAPI().messagePlayer(Log.error + "Failed to add message.");
                }
                The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully removed message.");
                break;
            case "list":

                The5zigAPI.getAPI().messagePlayer(Log.info + "Test messages:");

                TIMV.testRequests.forEach(s -> The5zigAPI.getAPI().messagePlayer("§7 - §b" + s));

                break;
        }


        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(GameProfile sender, String[] args) {
        if(args.length == 1)
            return TabCompletionUtils.matching(args, Arrays.asList("add", "remove", "list"));
        return new ArrayList<>();
    }
}
