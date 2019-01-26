package eu.beezig.core.command;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.utils.soundcloud.TrackPlayer;
import eu.the5zig.mod.The5zigAPI;

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
            if (vol > 100 || vol < 0) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "Volume must be within 0-100!");
                return true;
            }

            TrackPlayer.gainToLoad = vol - 50f;
            TrackPlayer.rawGainToLoad = vol / 100f;
            if (TrackPlayer.player != null) TrackPlayer.player.player.setGain(vol - 50f);
            new Thread(() -> TrackPlayer.saveNewGain(vol)).start();

            The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully changed volume.");

        } catch (NumberFormatException e) {
            The5zigAPI.getAPI().messagePlayer(Log.error + "Volume must be within 0-100!");
        }

        return true;
    }


}
