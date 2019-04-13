/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
 *
 * This file is part of Beezig.
 *
 * Beezig is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beezig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beezig.  If not, see <http://www.gnu.org/licenses/>.
 */

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
