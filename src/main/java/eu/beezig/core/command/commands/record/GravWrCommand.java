package eu.beezig.core.command.commands.record;

import eu.beezig.core.Beezig;
import eu.beezig.core.command.Command;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.modes.GRAV;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.speedrun.GravWorldRecords;
import eu.beezig.core.util.text.Message;
import eu.the5zig.mod.server.GameMode;

public class GravWrCommand implements Command {
    @Override
    public String getName() {
        return "gravwr";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/gravwr", "/grwr"};
    }

    @Override
    public boolean execute(String[] args) {
        GameMode mode = !ServerHive.isCurrent() ? null : Beezig.api().getActiveServer().getGameListener().getCurrentGameMode();
        String map;
        if(args.length == 0 && mode instanceof GRAV) map = ((GRAV) mode).getMap();
        else map = String.join(" ", args);
        if(map == null) {
            Message.error(Message.translate("error.map_not_found"));
            return true;
        }
        GravWorldRecords.getRecord(map)
            .thenAcceptAsync(record -> Message.info(Beezig.api().translate("msg.dr.wr", Color.accent() + record.getTimeDisplay() + Color.primary(),
            Color.accent() + record.getName() + Color.primary()))).exceptionally(e -> {
            ExceptionHandler.catchException(e, "GRAV /wr");
            Message.error(Message.translate("error.map_not_found"));
            return null;
        });
        return true;
    }
}
