package eu.beezig.core.command.commands;

import eu.beezig.core.Beezig;
import eu.beezig.core.command.Command;
import eu.beezig.core.logging.ws.WinstreakService;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.ActiveGame;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class WinstreakCommand implements Command {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    @Override
    public String getName() {
        return "streak";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/bstreak", "/bwinstreak"};
    }

    @Override
    public boolean execute(String[] args) {
        if(!ServerHive.isCurrent()) return false;
        String id = ActiveGame.getID();
        if(args.length == 0 && id == null) {
            sendUsage("/bstreak [mode]");
            return true;
        } else if(args.length > 0) id = args[0];
        WinstreakService service = Beezig.get().getWinstreakManager().getService(id.toLowerCase(Locale.ROOT));
        Message.info(Beezig.api().translate("msg.winstreak", Color.accent() + Message.formatNumber(service.getCurrent()) + Color.primary(),
            Color.accent() + Message.formatNumber(service.getBest()) + Color.primary()));
        ZonedDateTime last = service.getLastReset() == null ? null : Instant.ofEpochMilli(service.getLastReset()).atZone(ZoneId.systemDefault());
        ZonedDateTime best = service.getBestReset() == null ? null : Instant.ofEpochMilli(service.getBestReset()).atZone(ZoneId.systemDefault());
        if(last != null) Message.info(Beezig.api().translate("msg.winstreak.reset", Color.accent() + last.format(formatter) + " (" + StringUtils.getTimeAgo(service.getLastReset()) + ")"));
        if(best != null) Message.info(Beezig.api().translate("msg.winstreak.reset.best", Color.accent() + best.format(formatter) + " (" + StringUtils.getTimeAgo(service.getBestReset()) + ")"));
        return true;
    }
}
