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

import eu.beezig.core.Beezig;
import eu.beezig.core.command.Command;
import eu.beezig.core.data.timv.TestMessagesManager;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.TextButton;
import eu.the5zig.mod.util.component.MessageComponent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class CustomTestCommand implements Command {
    private static final String usage = "/ctest [list/add/remove] [message/id]";
    @Override
    public String getName() {
        return "ctest";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/customtest", "/ctest"};
    }

    @Override
    public boolean execute(String[] args) {
        String opMode = args.length == 0 ? "list" : args[0];
        TestMessagesManager mgr = Beezig.get().getData().getCustomTestMessages();
        if("list".equalsIgnoreCase(opMode)) {
            MessageComponent parent = new MessageComponent(Message.infoPrefix() + Message.translate("msg.ctest.list") + " ");
            TextButton addBtn = new TextButton("btn.ctest.add.name", "btn.ctest.add.desc", "§a");
            addBtn.doSuggestCommand("/ctest add ");
            parent.getSiblings().add(addBtn);
            Beezig.api().messagePlayerComponent(parent, false);
            List<String> messages = mgr.getCustomMessages();
            IntStream.range(0, messages.size())
                    .forEach(i -> {
                        MessageComponent parentMsg = new MessageComponent("");
                        String s = messages.get(i);
                        TextButton deleteBtn = new TextButton("✖", "btn.ctest.delete.desc", "§c");
                        deleteBtn.doRunCommand("/ctest remove " + i);
                        MessageComponent msg = new MessageComponent(String.format(" %s%s", Color.primary(), s));
                        parentMsg.getSiblings().add(deleteBtn);
                        parentMsg.getSiblings().add(msg);
                        Beezig.api().messagePlayerComponent(parentMsg, false);
                    });
            return true;
        }
        try {
            if (args.length > 1 && "add".equalsIgnoreCase(opMode)) {
                String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                mgr.add(message);
            } else if ("remove".equalsIgnoreCase(opMode)) {
                mgr.remove(Integer.parseInt(args[1], 10));
            } else {
                sendUsage(usage);
                return true;
            }
        } catch(IOException e) {
            ExceptionHandler.catchException(e, "Couldn't save ctest config");
            Message.error(Message.translate("error.data_read"));
        }
        Message.info(Message.translate("msg.ctest.success"));
        return true;
    }
}
