package eu.beezig.core.command;

import eu.beezig.core.Log;
import eu.beezig.core.settings.Setting;
import eu.beezig.core.utils.autogg.Triggers;
import eu.the5zig.mod.The5zigAPI;

import java.io.IOException;
import java.util.Arrays;

public class AutoGGCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "autogg";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/autogg"};
    }


    @Override
    public boolean execute(String[] args) {

        if (args.length == 0) {
            The5zigAPI.getAPI().messagePlayer(Log.bar + "\n");
            The5zigAPI.getAPI().messagePlayer(Log.info + "AutoGG: §b" + (Triggers.enabled ? "Loaded" : "Unloaded"));
            The5zigAPI.getAPI().messagePlayer(Log.info + "GG Text: §b" + Triggers.ggText);
            The5zigAPI.getAPI().messagePlayer(Log.info + "Delay in milliseconds: §b" + Triggers.delay);
            The5zigAPI.getAPI().messagePlayer(Log.info + "Enabled: " + (Setting.AUTOGG.getValue() ? "§aYes" : "§cNo"));
            The5zigAPI.getAPI().messagePlayer("");
            The5zigAPI.getAPI().messagePlayer(Log.info + "To customize, use §b/autogg [text/delay] [value].");
            The5zigAPI.getAPI().messagePlayer(Log.info + "To enable/disable modes, use §b/autogg [enable/disable] [shortcode].");
            The5zigAPI.getAPI().messagePlayer("");
            The5zigAPI.getAPI().messagePlayer(Log.bar);
        } else if (args.length >= 2) {
            String mode = args[0].toLowerCase();
            String value = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            switch (mode) {
                case "text":
                    Triggers.ggText = value;
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully changed text.");
                    break;

                case "delay":
                    Triggers.delay = Integer.parseInt(value);
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully changed delay.");
                    break;

                case "enable":
                    Triggers.changeMode(value.toUpperCase(), true);
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully enabled mode.");
                    break;

                case "disable":
                    Triggers.changeMode(value.toUpperCase(), false);
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully disabled mode.");
                    break;

            }

            new Thread(() -> {
                try {
                    Triggers.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();


        }

        return true;
    }


}
