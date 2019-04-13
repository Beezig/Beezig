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

import eu.beezig.core.Log;
import eu.beezig.core.utils.tutorial.TutorialManager;
import eu.the5zig.mod.The5zigAPI;

public class SkipTutorialCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "skiptutorial";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/skiptutorial"};
    }


    @Override
    public boolean execute(String[] args) {
        TutorialManager.progress.put("completed", true);
        new Thread(() -> TutorialManager.save(TutorialManager.progress)).start();
        TutorialManager.remote = null; // Stop operating
        The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully skipped tutorial.");
        return true;
    }


}
