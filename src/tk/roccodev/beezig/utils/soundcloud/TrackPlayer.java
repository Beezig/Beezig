package tk.roccodev.beezig.utils.soundcloud;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.PausablePlayer;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs_remap.CodecJOrbis;
import paulscode.sound.libraries_remap.LibraryJavaSound;

import java.io.BufferedInputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class TrackPlayer {

   public static PausablePlayer player;
   public static BufferedInputStream cachedTrack;
   public static boolean playing;
   private static SoundSystem system;


    public static void init() throws JavaLayerException{

        if(player != null) player.close();
       player = new PausablePlayer(cachedTrack);
       player.player.setGain(-30f);
       new Thread(() -> {
           try {
               SoundSystemConfig.setCodec( "ogg", CodecJOrbis.class );


           SoundSystemConfig.addLibrary(LibraryJavaSound.class);
           system = new SoundSystem(LibraryJavaSound.class);
           try {


               system.newSource(true, "vinyl", new URL("http://static.hivemc.com/bp/sfx/vinylstop.ogg"), "vinyl.ogg", false,
                       0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0 );
               system.newSource(true, "cheer", new URL("http://static.hivemc.com/bp/sfx/cheer.ogg"), "cheer.ogg", false,
                       0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0 );

               system.setTemporary("vinyl", false);
               system.setTemporary("cheer", false);

           } catch (MalformedURLException e) {
               e.printStackTrace();
           }
        } catch (SoundSystemException e){
            e.printStackTrace();
        }
        }).start();



    }

    public static void resume(){
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
        system.removeSource("cheer");
        system.removeSource("vinyl");
    }


    public static void playOgg(String which) {
        system.activate(which);
        system.stop(which);
        system.play(which);


    }






}
