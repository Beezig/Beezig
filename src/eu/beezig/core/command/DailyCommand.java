/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
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

package eu.beezig.core.command;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.BeezigMain;
import eu.beezig.core.Log;
import eu.the5zig.mod.The5zigAPI;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DailyCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "daily";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/daily"};
    }


    @Override
    public boolean execute(String[] args) {


        String mode = args.length > 0 ? args[0] : ActiveGame.current();

        new Thread(() -> {
            try {
                Class clazz = Class.forName("eu.beezig.core.games." + mode.toUpperCase());
                Field f = clazz.getSimpleName().equals("TIMV") ? clazz.getDeclaredField("dailyKarmaName") : clazz.getDeclaredField("dailyPointsName");
                f.setAccessible(true);
                ArrayList<String> lines = new ArrayList<>(
                        new ArrayList<>(Files.readAllLines(Paths.get(new File(BeezigMain.mcFile + "/" + mode.toLowerCase() + "/" +
                                (mode.equalsIgnoreCase("timv") ? "dailykarma" : "dailypoints") +
                                "/" + f.get(null)).getPath()))));
                The5zigAPI.getAPI().messagePlayer(Log.info + "Daily Points: §b" + lines.get(0));
            } catch (IOException | ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "No daily data found.");
                e.printStackTrace();
            }
        }).start();


        return true;
    }


}
