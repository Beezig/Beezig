package tk.roccodev.beezig.utils.soundcloud;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.PausablePlayer;

import java.io.BufferedInputStream;

public class TrackPlayer {

   public static PausablePlayer player;
   public static BufferedInputStream cachedTrack;
   public static boolean playing;


    public static void init() throws JavaLayerException {

        if(player != null) player.close();
        player = new PausablePlayer(cachedTrack);
        player.player.setGain(-30f);



    }

    public static void resume() throws JavaLayerException {
        playing = true;
        player.play();
    }

    public static void stop() {
        playing = false;
        player.pause();

    }

    public static void close() {
        playing = false;
        player.stop();
        player.close();
    }

}
