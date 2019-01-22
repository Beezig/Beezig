package eu.beezig.core.command;

import com.mojang.authlib.GameProfile;

import java.util.List;

public interface Command {


    String getName();

    String[] getAliases();

    /*
     *
     * Executes the command
     *
     * @return !whether the command should be skipped and the server command should be run.
     *
     * Example:
     * "seen" command can only be run in Hive. If the server is not Hive, return false, "seen" server-side will be run.
     *
     */
    boolean execute(String[] args);

    default List<String> addTabCompletionOptions(GameProfile sender, String[] args) {
        return null;
    }


}
