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

package eu.beezig.core.listener.arcade.subs;

import eu.beezig.core.games.Arcade;
import eu.beezig.core.listener.arcade.ArcadeSubListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElectricFloorSubListener extends ArcadeSubListener {

    @Override
    public void onServerChat(Arcade gameMode, String message) {
        if (message.startsWith("§8▍ §3§lElectric§b§lFloor§8 ▏ §3Voting has ended! §bThe map §f")) {
            String map = "";
            String afterMsg = message.split("§8▍ §3§lElectric§b§lFloor§8 ▏ §3Voting has ended! §bThe map")[1];
            Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
            Matcher matcher = pattern.matcher(afterMsg);
            while (matcher.find()) {
                map = matcher.group(1);
            }
            gameMode.map = map;
        }
    }
}
