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
import eu.beezig.core.Constants;
import eu.beezig.core.command.Command;
import eu.beezig.core.command.CommandManager;
import eu.beezig.core.util.ArrayUtils;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.SystemInfo;
import eu.beezig.core.util.modules.IModulesProvider;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;
import eu.the5zig.mod.util.component.MessageComponent;
import eu.the5zig.mod.util.component.style.MessageAction;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.Comparator;

public class BeezigCommand implements Command {
    public static IModulesProvider modulesProvider;

    @Override
    public String getName() {
        return "beezig";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/beezig"};
    }

    @Override
    public boolean execute(String[] args) {
        if(args.length == 0) {
            MessageComponent commands = new MessageComponent(Message.infoPrefix() + Message.translate("msg.hint.commands"));
            commands.getStyle().setOnClick(new MessageAction(MessageAction.Action.RUN_COMMAND, "/beezig commands"));
            commands.getStyle().setOnHover(new MessageAction(MessageAction.Action.SHOW_TEXT, new MessageComponent(Color.primary() + Message.translate("msg.hint.commands"))));
            MessageComponent modules = new MessageComponent(Message.infoPrefix() + Message.translate("msg.hint.modules"));
            modules.getStyle().setOnClick(new MessageAction(MessageAction.Action.RUN_COMMAND, "/beezig modules"));
            modules.getStyle().setOnHover(new MessageAction(MessageAction.Action.SHOW_TEXT, new MessageComponent(Color.primary() + Message.translate("msg.hint.modules"))));
            Message.bar();
            Message.info(String.format("Running Beezig %s (%s%s%s)", Constants.VERSION, Color.accent(), Beezig.getVersionString(), Color.primary()));
            Beezig.api().messagePlayerComponent(commands, false);
            Beezig.api().messagePlayerComponent(modules, false);
            Message.bar();
        }
        else {
            String mode = args[0];
            if("info".equalsIgnoreCase(mode)) {
                String sysInfo = SystemInfo.getSystemInfo();
                StringSelection sel = new StringSelection(sysInfo);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, sel);
                Message.info("Debug info has been copied to your clipboard.");
            }
            else if("debug".equalsIgnoreCase(mode)) {
                Message.info("Title debug: " + (Beezig.get().toggleTitleDebug() ? "§aenabled" : "§cdisabled"));
            }
            else if("commands".equalsIgnoreCase(mode)) {
                int page = args.length < 2 ? 1 : Integer.parseInt(args[1], 10);
                showCommands(page);
            }
            else if("modules".equalsIgnoreCase(mode)) {
                if(modulesProvider != null) modulesProvider.openModulesGui();
                else Message.error(Message.translate("error.hint.modules"));
            }
        }
        return true;
    }

    private void showCommands(int pageNo) {
        Command[] page = ArrayUtils.getPage(CommandManager.commandExecutors.stream().sorted(Comparator.comparing(c -> c.getAliases()[0])).toArray(Command[]::new), pageNo - 1, 10);
        if(page == null) {
            Message.error(Message.translate("error.page"));
            return;
        }
        Beezig.api().messagePlayer(StringUtils.linedCenterText(eu.beezig.core.util.Color.primary(), eu.beezig.core.util.Color.accent()
            + Message.translate("msg.commands.list") + " " + Beezig.api().translate("msg.page", pageNo)));
        for(Command cmd : page) {
            MessageComponent base = new MessageComponent(Color.primary() + " - " + Color.accent() + cmd.getAliases()[0]);
            String[] otherAliases = cmd.getAliases().length <= 1 ? null : Arrays.copyOfRange(cmd.getAliases(), 1, cmd.getAliases().length);
            if(otherAliases != null) {
                MessageComponent hover = new MessageComponent(Color.primary() + Beezig.api().translate("msg.commands.aliases",
                    Color.accent() + String.join(Color.primary() + ", " + Color.accent(), otherAliases)));
                base.getStyle().setOnHover(new MessageAction(MessageAction.Action.SHOW_TEXT, hover));
            }
            base.getStyle().setOnClick(new MessageAction(MessageAction.Action.SUGGEST_COMMAND, cmd.getAliases()[0]));
            Beezig.api().messagePlayerComponent(base, false);
        }
        Beezig.api().messagePlayer(StringUtils.linedCenterText(eu.beezig.core.util.Color.primary(), Color.accent() + Message.translate("cmd.commands")));
    }
}
