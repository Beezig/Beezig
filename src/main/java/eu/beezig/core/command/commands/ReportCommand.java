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

import com.google.common.base.Splitter;
import eu.beezig.core.Beezig;
import eu.beezig.core.command.Command;
import eu.beezig.core.config.Settings;
import eu.beezig.core.net.packets.PacketReport;
import eu.beezig.core.report.ReportOutgoing;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;
import eu.beezig.core.util.text.TextButton;
import eu.beezig.hiveapi.wrapper.mojang.UsernameToUuid;
import eu.the5zig.mod.util.component.MessageComponent;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReportCommand implements Command {
    private static final Pattern REPORT_CMD_REGEX = Pattern.compile("(.+?)(?=(?<!,)\\s) (.+)$");

    @Override
    public String getName() {
        return "report";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/breport", "/brep"};
    }

    @Override
    public boolean execute(String[] args) {
        if(!ServerHive.isCurrent()) return false;
        if(args.length > 1) {
            String cmd = String.join(" ", args);
            Matcher m = REPORT_CMD_REGEX.matcher(cmd);
            if (m.matches()) {
                String gPlayers = m.group(1);
                String gReasons = m.group(2);
                List<String> players = Splitter.onPattern(",\\s?").splitToList(gPlayers);
                List<String> reasons = Splitter.onPattern("\\s").splitToList(gReasons);
                String pDisplay = Color.accent() + StringUtils.localizedJoin(players) + Color.primary();
                String rDisplay = Color.accent() + StringUtils.localizedJoin(reasons) + Color.primary();
                // Friend broadcast
                String broadcastMessage = Settings.REPORTS_BROADCAST_MESSAGE.get().getString();
                broadcastMessage = broadcastMessage.replace("{player}", StringUtils.englishJoin(players)).replace("{reason}", StringUtils.englishJoin(reasons));
                if(Settings.REPORTS_BROADCAST.get().getBoolean()) {
                    Message.info(Beezig.api().translate("msg.report.submit", pDisplay, rDisplay));
                    Beezig.api().sendPlayerMessage("/friend broadcast " + broadcastMessage);
                } else {
                    MessageComponent base = new MessageComponent(Message.infoPrefix() + Beezig.api().translate("msg.report.submit", pDisplay, rDisplay) + " ");
                    TextButton btn = new TextButton("btn.report.broadcast.name", "btn.report.broadcast.desc", "§e");
                    btn.doRunCommand("/friend broadcast " + broadcastMessage);
                    base.getSiblings().add(btn);
                    Beezig.api().messagePlayerComponent(base, false);
                }
                CompletableFuture.allOf(players.stream().map(this::checkUsername).toArray(CompletableFuture[]::new))
                    .thenAcceptAsync(v -> Beezig.net().getHandler()
                        .sendPacket(PacketReport.newReport(new ReportOutgoing(ReportOutgoing.ReportType.PLAYER, players, reasons))))
                    .exceptionally(e -> {
                        Message.error(Message.translate("error.report.username"));
                        return null;
                    });
            }
            return true;
        }
        sendUsage("/breport [player1(, player2, player3)] [reason]");
        return true;
    }

    private CompletableFuture<Void> checkUsername(String name) {
        return UsernameToUuid.getUUID(name).thenAcceptAsync(s -> {});
    }
}
