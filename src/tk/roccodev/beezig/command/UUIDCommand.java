package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import pw.roccodev.beezig.hiveapi.wrapper.mojang.UsernameToUuid;
import tk.roccodev.beezig.Log;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class UUIDCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "uuid";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/uuid"};
    }


    @Override
    public boolean execute(String[] args) {


        if (args.length == 0) {
            The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /uuid [player] (s/c)");
            return true;
        } else {
            new Thread(() -> {
                String pl = args[0];
                boolean copy = false;
                if (args.length == 2) {
                    String modes = args[1];
                    copy = modes.contains("c");
                }

                String uuid = UsernameToUuid.getUUID(pl).replace("-", "");
                The5zigAPI.getAPI().messagePlayer(Log.info + pl + "'s UUID is §b" + uuid);
                if (copy) {
                    StringSelection sel = new StringSelection(uuid);
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, sel);
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Copied to clipboard!");
                }
            }).start();

        }


        return true;
    }


}
