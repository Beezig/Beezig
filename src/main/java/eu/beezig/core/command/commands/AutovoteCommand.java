/*
 * Copyright (C) 2019 Beezig Team
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

package eu.beezig.core.command.commands;

import eu.beezig.core.Beezig;
import eu.beezig.core.autovote.AutovoteConfig;
import eu.beezig.core.command.Command;
import eu.beezig.core.util.Message;

import java.util.Locale;

public class AutovoteCommand implements Command {
    private static final String usage = "/autovote [list/add/remove/place] [mode] (map) (place)";
    @Override
    public String getName() {
        return "autovote";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/autovote", "/av"};
    }

    @Override
    public boolean execute(String[] args) {
        if(args.length < 2) {
            sendUsage(usage);
            return true;
        }
        String opMode = args[0];
        String mode = args[1];
        AutovoteConfig config = new AutovoteConfig();
        if("list".equalsIgnoreCase(opMode)) {
            Message.info(Beezig.api().translate("msg.autovote.list", mode.toUpperCase(Locale.ROOT)));
            config.getMaps(mode.toLowerCase(Locale.ROOT)).forEach(s -> {
                Beezig.api().messagePlayer(" §3- §a" + s);
            });
            return true;
        }
        if(args.length < 3) {
            sendUsage(usage);
            return true;
        }
        String map = args[2];
        if("add".equalsIgnoreCase(opMode)) {
            config.addMapToMode(mode, map);
        }
        else if("remove".equalsIgnoreCase(opMode)) {
            config.removeMapForMode(mode, map);
        }
        else if(args.length == 4 && "place".equalsIgnoreCase(opMode)) {
            int place = Integer.parseInt(args[3], 10);
            config.setPlace(mode, map, place);
        }
        else {
            sendUsage(usage);
            return true;
        }
        config.save();
        Message.info(Message.translate("msg.autovote.success"));
        return true;
    }
}
