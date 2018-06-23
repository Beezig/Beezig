package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.games.TIMV;
import tk.roccodev.beezig.utils.TIMVTest;

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

}
