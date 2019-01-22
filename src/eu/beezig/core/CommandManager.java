package eu.beezig.core;

import eu.the5zig.mod.The5zigAPI;
import eu.beezig.core.command.Command;

import java.util.*;

public class CommandManager {

    public static Set<Command> commandExecutors = new HashSet<>();


    /*
     * Dispatches a command.
     *
     * @return whether the command has been found
     *
     *
     */
    public static boolean dispatchCommand(String str) {

        String[] data = str.split(" ");
        String alias = data[0];
        Command cmdFound = null;
        for (Command cmd : commandExecutors) {
            for (String s : cmd.getAliases()) {
                if (s.equalsIgnoreCase(alias)) {
                    cmdFound = cmd;
                    break;
                }
            }
        }
        if (cmdFound == null) return false;

        List<String> dataList = new ArrayList<>(Arrays.asList(data));
        dataList.remove(0); //Remove alias


        try {
            if (!cmdFound.execute(dataList.toArray(new String[0]))) {
                return false; //Skip the command
            }

        } catch (Exception e) {
            e.printStackTrace();
            The5zigAPI.getAPI().messagePlayer(Log.error + "An error occurred while attempting to perform this command.");
        }
        return true;
    }

    public static void registerCommand(Command cmd) {
        commandExecutors.add(cmd);
    }

}
