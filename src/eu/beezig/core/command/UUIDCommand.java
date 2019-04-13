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
import eu.the5zig.mod.The5zigAPI;
import pw.roccodev.beezig.hiveapi.wrapper.mojang.UsernameToUuid;

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
