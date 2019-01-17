package tk.roccodev.beezig.api;

import tk.roccodev.beezig.autovote.AutovoteUtils;

import java.util.ArrayList;
import java.util.List;

public class AutovoteAPI {

    public List<String> getMapsForMode(String mode) {
        return AutovoteUtils.getMapsForMode(mode.toLowerCase());
    }

    public void setMapsForMode(String mode, ArrayList<String> maps) {
        AutovoteUtils.set(mode.toLowerCase(), maps);
        AutovoteUtils.dump();
    }

}
