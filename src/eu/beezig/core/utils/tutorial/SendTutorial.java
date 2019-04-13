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

package eu.beezig.core.utils.tutorial;

import eu.beezig.core.Log;
import org.json.simple.JSONArray;

public class SendTutorial {

    public static void send(String key, Object... args) {
        if (!TutorialManager.shouldOperate()) return;
        if (TutorialManager.progress.containsKey(key) && (boolean) TutorialManager.progress.get(key)) return;
        String toFormat = (String) TutorialManager.remote.get(key);
        if (toFormat == null) return;
        String formatted = String.format(toFormat, args);
        Log.addToSendQueue(formatted.replace("{info}", Log.info).replace("{error}", Log.error));
        Log.addToSendQueue("");
        Log.addToSendQueue(Log.info + "This was a tutorial message. To disable these messages, run §b/skiptutorial§3.");
        checkForCompletion(key);
    }

    private static void checkForCompletion(String key) {
        TutorialManager.progress.put(key, true);

        JSONArray toComplete = (JSONArray) TutorialManager.remote.get("toComplete");
        boolean completed = true;
        for (Object toCheck : toComplete) {

            if (!TutorialManager.progress.containsKey(toCheck) || !(boolean) TutorialManager.progress.get(toCheck)) {
                completed = false;
                break;
            }
        }
        TutorialManager.progress.put("completed", completed);

        new Thread(() -> TutorialManager.save(TutorialManager.progress)).start();


    }

}
