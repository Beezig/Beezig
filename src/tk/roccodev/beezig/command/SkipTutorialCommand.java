package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.utils.tutorial.TutorialManager;

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
