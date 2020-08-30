package eu.beezig.core.command.commands;

import com.google.common.reflect.TypeToken;
import eu.beezig.core.Beezig;
import eu.beezig.core.command.Command;
import eu.beezig.core.data.DataPath;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.modes.DR;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;
import eu.beezig.hiveapi.wrapper.player.Profiles;
import eu.the5zig.mod.server.GameMode;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class DrPbCommand implements Command {
    @Override
    public String getName() {
        return "drpb";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/drpb"};
    }

    @Override
    public boolean execute(String[] args) {
        String player;
        GameMode mode = !ServerHive.isCurrent() ? null : Beezig.api().getActiveServer().getGameListener().getCurrentGameMode();
        DR.MapData map;
        if(args.length == 0 && mode instanceof DR) {
            map = ((DR) mode).getCurrentMapData();
            player = Beezig.user().getName();
        }
        else if(args.length == 1) {
            if(mode instanceof DR && ((DR) mode).getCurrentMapData() != null) {
                map = ((DR) mode).getCurrentMapData();
                player = args[0];
            }
            else {
                player = Beezig.user().getName();
                map = getData(args[0]);
            }
        }
        else {
            player = args[0];
            map = getData(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
        }
        if(map == null) {
            Message.error(Message.translate("error.map_not_found"));
            return true;
        }
        Profiles.dr(player).thenAcceptAsync(stats -> {
            String display = DurationFormatUtils.formatDuration(stats.getMapRecords().get(map.hive) * 1000, "m:ss");
            Message.info(Beezig.api().translate("msg.dr.pb", Color.accent() + display + Color.primary()));
        }).exceptionally(e -> {
            Message.error(Message.translate("error.dr.pb"));
            Beezig.logger.error("Error fetching player profile", e);
            return null;
        });
        return true;
    }

    private DR.MapData getData(String name) {
        try {
            Map<String, DR.MapData> maps = Beezig.get().getData().getDataMap(DataPath.DR_MAPS, new TypeToken<Map<String, DR.MapData>>() {});
            return maps.get(StringUtils.normalizeMapName(name));
        } catch (IOException e) {
            Beezig.logger.error("Tried to fetch maps but file wasn't found.", e);
            return null;
        }
    }
}
