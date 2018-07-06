package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.notes.NotesManager;
import tk.roccodev.beezig.utils.soundcloud.TrackPlayer;

public class VolumeCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "vol";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/volume", "/vol"};
    }


    @Override
    public boolean execute(String[] args) {

        if (!ActiveGame.is("bp")) return false;
        if (!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
        if (args.length == 0) {
            The5zigAPI.getAPI().messagePlayer(Log.info + "Usage /volume [0-100].");
            return true;
        }
        try {
            int vol = Integer.parseInt(args[0]);
            if(vol > 100 || vol < 0) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "Volume must be within 0-100!");
                return true;
            }

            TrackPlayer.gainToLoad = vol - 50f;
            TrackPlayer.rawGainToLoad = vol / 100f;
            if(TrackPlayer.player != null) TrackPlayer.player.player.setGain(vol - 50f);
            new Thread(() -> TrackPlayer.saveNewGain(vol)).start();

            The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully changed volume.");

        } catch(NumberFormatException e) {
            The5zigAPI.getAPI().messagePlayer(Log.error + "Volume must be within 0-100!");
        }

        return true;
    }


}
