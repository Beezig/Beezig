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
