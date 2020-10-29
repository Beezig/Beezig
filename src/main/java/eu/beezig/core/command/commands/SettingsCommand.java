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
import eu.beezig.core.config.EnumSetting;
import eu.beezig.core.config.Settings;
import eu.beezig.core.util.ArrayUtils;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;
import eu.the5zig.mod.util.component.MessageComponent;
import eu.the5zig.mod.util.component.style.MessageAction;
import org.apache.commons.lang3.text.WordUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

public class SettingsCommand implements Command {
    @Override
    public String getName() {
        return "bsettings";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/bsettings"};
    }

    @Override
    public boolean execute(String[] args) {
        Settings setting;
        if(args.length == 0) {
            show(1);
        }
        else if(args.length == 1) {
            if(args[0].matches("\\d+")) {
                show(Integer.parseInt(args[0], 10));
                return true;
            }
            if((setting = getSetting(args[0])) == null) return true;
            Message.info(String.format("%s%s %s(%s):%s %s", Color.accent(), setting.getName(), Color.primary(),
                    setting.getDescription(), Color.primary(), setting.get().getDisplay()));
        }
        else {
            if((setting = getSetting(args[0])) == null) return true;
            if(!Beezig.cfg().set(setting, String.join(" ", Arrays.copyOfRange(args, 1, args.length)))) return true;
            try {
                Beezig.cfg().save();
                Message.info(Message.translate("msg.config.save"));
            } catch (IOException e) {
                Message.error(Message.translate("error.data_read"));
                ExceptionHandler.catchException(e);
            }
        }
        return true;
    }

    private void show(int pageNo) {
        Settings[] page = ArrayUtils.getPage(Arrays.stream(Settings.values()).filter(s -> s.getCategory() != Settings.Category.HIDDEN).toArray(Settings[]::new),
            pageNo - 1, 10);
        if(page == null) {
            Message.error(Message.translate("error.page"));
            return;
        }
        Beezig.api().messagePlayer(StringUtils.linedCenterText(Color.primary(), Color.accent()
                + Message.translate("msg.config.list") + " " + Beezig.api().translate("msg.page", pageNo)));
        for(Settings key : page) {
            MessageComponent component = new MessageComponent(String.format("%s - %s%s: %s%s", Color.accent(), Color.primary(), key.getName(), Color.accent(), key.get().getDisplay()));
            MessageComponent desc = new MessageComponent(String.format("§7%s\n§m                \n%s%s", key.name().toLowerCase(Locale.ROOT), Color.primary(), key.getDescription()));
            if(Enum.class.isAssignableFrom(key.getSettingType()) || EnumSetting.class.isAssignableFrom(key.getSettingType())) {
                try {
                    desc.getSiblings().add(new MessageComponent(WordUtils.wrap("\n\n" + Color.primary()
                            + Beezig.api().translate("msg.enum.values", Color.accent() + Beezig.cfg().getEnumValues(key.getSettingType())),
                            50,"\n" + Color.accent(), false)));
                } catch (ReflectiveOperationException e) {
                    ExceptionHandler.catchException(e, "Couldn't add values information");
                }
            }
            component.getStyle().setOnHover(new MessageAction(MessageAction.Action.SHOW_TEXT, desc));
            if(key.getSettingType() == Boolean.class)
                component.getStyle().setOnClick(new MessageAction(MessageAction.Action.RUN_COMMAND, String.format("/bsettings %s %s", key.name(), Boolean.toString(!key.get().getBoolean()))));
            else component.getStyle().setOnClick(new MessageAction(MessageAction.Action.SUGGEST_COMMAND, String.format("/bsettings %s ", key.name())));
            Beezig.api().messagePlayerComponent(component, false);
        }
        Beezig.api().messagePlayer(StringUtils.linedCenterText(Color.primary(), Color.accent() + Message.translate("cmd.settings")));
        MessageComponent settings = new MessageComponent(Message.infoPrefix() + Message.translate("msg.hint.settings2"));
        settings.getStyle().setOnClick(new MessageAction(MessageAction.Action.RUN_COMMAND, "/beezig settings"));
        settings.getStyle().setOnHover(new MessageAction(MessageAction.Action.SHOW_TEXT, new MessageComponent(Color.primary() + Message.translate("msg.hint.settings"))));
        Beezig.api().messagePlayerComponent(settings, false);
    }

    private Settings getSetting(String name) {
        try {
            return Settings.valueOf(name.toUpperCase(Locale.ROOT).replace(".", "_"));
        }
        catch (IllegalArgumentException ex) {
            Message.error(Message.translate("error.setting_not_found"));
            return null;
        }
    }
}
