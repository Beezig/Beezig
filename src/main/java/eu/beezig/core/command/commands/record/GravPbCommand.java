/*
 * Copyright (C) 2017-2021 Beezig Team
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

package eu.beezig.core.command.commands.record;

import eu.beezig.core.Beezig;
import eu.beezig.core.command.Command;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.modes.DR;
import eu.beezig.core.server.modes.GRAV;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.text.Message;
import eu.beezig.hiveapi.wrapper.player.Profiles;
import eu.the5zig.mod.server.GameMode;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.Arrays;
import java.util.Locale;

public class GravPbCommand implements Command {
    @Override
    public String getName() {
        return "gravpb";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/gravpb", "/grpb"};
    }

    @Override
    public boolean execute(String[] args) {
        String player;
        GameMode mode = !ServerHive.isCurrent() ? null : Beezig.api().getActiveServer().getGameListener().getCurrentGameMode();
        String map;
        if(args.length == 0) {
            if (mode instanceof GRAV) {
                map = ((GRAV) mode).getMap();
                player = Beezig.user().getName();
            } else {
                Message.error(Message.translate("error.map_not_found"));
                return true;
            }
        }
        else if(args.length == 1) {
            if(mode instanceof DR && ((DR) mode).getCurrentMapData() != null) {
                map = ((DR) mode).getMap();
                player = args[0];
            }
            else {
                player = Beezig.user().getName();
                map = args[0];
            }
        }
        else {
            player = args[0];
            map = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }
        if(map == null) {
            Message.error(Message.translate("error.map_not_found"));
            return true;
        }
        Profiles.grav(player).thenAcceptAsync(stats -> {
            Long time = stats.getMapRecords().get(map.toUpperCase(Locale.ROOT).replace(" ", "_"));
            if(time == null) {
                Message.error(Message.translate("error.dr.pb"));
                return;
            }
            String display = DurationFormatUtils.formatDuration(time, "m:ss.SSS");
            Message.info(Beezig.api().translate("msg.dr.pb", Color.accent() + display + Color.primary()));
        }).exceptionally(e -> {
            Message.error(Message.translate("error.dr.pb"));
            ExceptionHandler.catchException(e, "Error fetching player profile");
            return null;
        });
        return true;
    }
}
