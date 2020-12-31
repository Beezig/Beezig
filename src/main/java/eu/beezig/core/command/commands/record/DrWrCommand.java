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

package eu.beezig.core.command.commands.record;

import com.google.common.reflect.TypeToken;
import eu.beezig.core.Beezig;
import eu.beezig.core.command.Command;
import eu.beezig.core.data.DataPath;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.modes.DR;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.speedrun.DrWorldRecords;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;
import eu.the5zig.mod.server.GameMode;

import java.io.IOException;
import java.util.Map;

public class DrWrCommand implements Command {
    @Override
    public String getName() {
        return "drwr";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/drwr"};
    }

    @Override
    public boolean execute(String[] args) {
        GameMode mode = !ServerHive.isCurrent() ? null : Beezig.api().getActiveServer().getGameListener().getCurrentGameMode();
        DR.MapData map;
        if(args.length == 0 && mode instanceof DR) map = ((DR) mode).getCurrentMapData();
        else {
            try {
                Map<String, DR.MapData> maps = Beezig.get().getData().getDataMap(DataPath.DR_MAPS, new TypeToken<Map<String, DR.MapData>>() {});
                map = maps.get(StringUtils.normalizeMapName(String.join(" ", args)));
            } catch (IOException e) {
                Message.error("error.data_read");
                Beezig.logger.error("Tried to fetch maps but file wasn't found.");
                return true;
            }
        }
        if(map == null) {
            Message.error(Message.translate("error.map_not_found"));
            return true;
        }
        DrWorldRecords.getRecord(map)
            .thenAcceptAsync(record -> Message.info(Beezig.api().translate("msg.dr.wr", Color.accent() + record.getTimeDisplay() + Color.primary(),
            Color.accent() + record.getName() + Color.primary()))).exceptionally(e -> {
            Message.error(Message.translate("error.map_not_found"));
            return null;
        });
        return true;
    }
}
