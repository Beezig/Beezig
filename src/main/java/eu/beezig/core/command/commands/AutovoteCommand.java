/*
 * Copyright (C) 2017-2020 Beezig Team
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

import com.google.common.base.Strings;
import eu.beezig.core.Beezig;
import eu.beezig.core.autovote.AutovoteConfig;
import eu.beezig.core.command.Command;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.TextButton;
import eu.the5zig.mod.util.component.MessageComponent;

import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

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
            MessageComponent parent = new MessageComponent(Message.infoPrefix() + Beezig.api().translate("msg.autovote.list", mode.toUpperCase(Locale.ROOT)) + " ");
            TextButton addBtn = new TextButton("btn.autovote.add.name", "btn.autovote.add.desc", "§a");
            addBtn.doSuggestCommand("/autovote add " + mode + " ");
            parent.getSiblings().add(addBtn);
            Beezig.api().messagePlayerComponent(parent, false);
            List<String> maps = config.getMaps(mode.toLowerCase(Locale.ROOT));
            IntStream.range(0, maps.size())
                    .forEach(i -> {
                        MessageComponent parentMsg = new MessageComponent("");
                        String s = maps.get(i);
                        TextButton deleteBtn = new TextButton("✖", "btn.autovote.delete.desc", "§c");
                        TextButton moveUpBtn = new TextButton("▲", "btn.autovote.up.desc", "§7");
                        TextButton moveDownBtn = new TextButton("▼", "btn.autovote.down.desc", "§7");
                        deleteBtn.doRunCommand("/autovote remove " + mode + " " + s);
                        moveUpBtn.doRunCommand("/autovote place " + mode + " " + s + " " + i);
                        moveDownBtn.doRunCommand("/autovote place " + mode + " " + s + " " + (i + 2));
                        MessageComponent map = new MessageComponent(String.format(" %s%s", Color.primary(), s));
                        parentMsg.getSiblings().add(deleteBtn);
                        int spaces = 0;
                        if(i > 0) parentMsg.getSiblings().add(moveUpBtn);
                        else spaces += 4;
                        if(i < maps.size() - 1) parentMsg.getSiblings().add(moveDownBtn);
                        else spaces += 4;
                        parentMsg.getSiblings().add(new MessageComponent(Strings.repeat(" ", spaces)));
                        parentMsg.getSiblings().add(map);
                        Beezig.api().messagePlayerComponent(parentMsg, false);
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
