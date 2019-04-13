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

import com.mojang.authlib.GameProfile;
import eu.beezig.core.BeezigMain;
import eu.beezig.core.Log;
import eu.beezig.core.api.BeezigAPI;
import eu.beezig.core.games.TIMV;
import eu.beezig.core.utils.TIMVTest;
import eu.beezig.core.utils.TabCompletionUtils;
import eu.the5zig.mod.The5zigAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomTestCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "customtest";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/customtest", "/ctest"};
    }

    @Override
    public boolean execute(String[] args) {
        if (BeezigMain.hasExpansion && args.length == 0) {
            BeezigAPI.get().getListener().displayTIMVTestGui();
            return true;
        }

        String mode = args[0];
        StringBuilder msg = new StringBuilder();

        List<String> args1 = new ArrayList<>(Arrays.asList(args));
        args1.remove(0);

        args1.forEach(s -> msg.append(s).append(" "));

        String testMessage = msg.toString().trim();

        switch (mode.toLowerCase()) {

            case "add":
                try {
                    TIMVTest.add(testMessage);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    The5zigAPI.getAPI().messagePlayer(Log.error + "Failed to add message.");
                }
                The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully added message.");
                break;
            case "remove":
                try {
                    TIMVTest.remove(testMessage);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    The5zigAPI.getAPI().messagePlayer(Log.error + "Failed to add message.");
                }
                The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully removed message.");
                break;
            case "list":

                The5zigAPI.getAPI().messagePlayer(Log.info + "Test messages:");

                TIMV.testRequests.forEach(s -> The5zigAPI.getAPI().messagePlayer("§7 - §b" + s));

                break;
        }


        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(GameProfile sender, String[] args) {
        if (args.length == 1)
            return TabCompletionUtils.matching(args, Arrays.asList("add", "remove", "list"));
        return new ArrayList<>();
    }
}
