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
        if(args.length == 0 && mode instanceof GRAV) {
            map = ((GRAV) mode).getMap();
            player = Beezig.user().getName();
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
            String display = DurationFormatUtils.formatDuration(time * 1000, "m:ss.SSS");
            Message.info(Beezig.api().translate("msg.dr.pb", Color.accent() + display + Color.primary()));
        }).exceptionally(e -> {
            Message.error(Message.translate("error.dr.pb"));
            ExceptionHandler.catchException(e, "Error fetching player profile");
            return null;
        });
        return true;
    }
}
