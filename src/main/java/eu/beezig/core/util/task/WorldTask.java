/*
 * Copyright (C) 2017-2020 Beezig Team
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

package eu.beezig.core.util.task;

import eu.beezig.core.Beezig;

/**
 * A simple task that is run as soon as the player is in a world.
 */
public class WorldTask {
    private Runnable callback;

    private WorldTask(Runnable callback) {
        this.callback = callback;
    }

    void run() {
        callback.run();
    }

    public static void submit(Runnable runnable) {
        Beezig.get().getWorldTaskManager().submit(new WorldTask(runnable));
    }
}
