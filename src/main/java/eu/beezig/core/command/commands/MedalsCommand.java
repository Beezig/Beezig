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

package eu.beezig.core.command.commands;

import eu.beezig.core.Beezig;
import eu.beezig.core.command.Command;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.UUIDUtils;
import eu.beezig.core.util.text.Message;
import eu.beezig.hiveapi.wrapper.exception.ProfileNotFoundException;
import eu.beezig.hiveapi.wrapper.player.Profiles;
import org.json.simple.parser.ParseException;

public class MedalsCommand implements Command {
    @Override
    public String getName() {
        return "medals";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/medals"};
    }

    @Override
    public boolean execute(String[] args) {
        if (!ServerHive.isCurrent()) return false;
        String name = args.length == 0 ? Beezig.user().getName() : args[0];
        Profiles.global(args.length == 0 ? UUIDUtils.strip(Beezig.user().getId()) : args[0])
            .thenAcceptAsync(profile -> {
                Message.info(Beezig.api().translate("msg.medals", UUIDUtils.getNameWithOptionalRank(name, name, profile).join() + Color.primary(), Color.accent()
                    + Message.formatNumber(profile.getMedals()) + Color.primary()));
            }).exceptionally(e -> {
            if (e.getCause() instanceof ProfileNotFoundException || e.getCause() instanceof ParseException) {
                Message.info(Beezig.api().translate("msg.medals", Color.accent() + name + Color.primary(),
                    Color.accent() + Message.formatNumber(Math.abs(name.toLowerCase().hashCode() / (name.length() * 200_000)))) + Color.primary());
            } else ExceptionHandler.catchException(e);
            return null;
        });
        return true;
    }
}
