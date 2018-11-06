package tk.roccodev.beezig.utils.tutorial;

import org.json.simple.JSONArray;
import tk.roccodev.beezig.Log;

public class SendTutorial {

    public static void send(String key, Object... args) {
        if(!TutorialManager.shouldOperate()) return;
        if(TutorialManager.progress.containsKey(key) && (boolean)TutorialManager.progress.get(key)) return;
        String toFormat = (String) TutorialManager.remote.get(key);
        if(toFormat == null) return;
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
        for(Object toCheck : toComplete) {
            if(TutorialManager.progress.containsKey(toCheck) && (boolean)TutorialManager.progress.get(toCheck)) {
                completed = false;
                break;
            }
        }
        TutorialManager.progress.put("completed", completed);

        new Thread(() -> TutorialManager.save(TutorialManager.progress)).start();


    }

}
