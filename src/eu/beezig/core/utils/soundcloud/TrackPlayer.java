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

package eu.beezig.core.utils.soundcloud;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.PausablePlayer;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs_remap.CodecJOrbis;
import paulscode.sound.libraries_remap.LibraryJavaSound;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TrackPlayer {

    public static PausablePlayer player;
    public static BufferedInputStream cachedTrack;
    public static boolean playing;
    public static float gainToLoad = 0f;
    public static float rawGainToLoad = 0f;
    private static SoundSystem system;
    private static File configFile;

    public static void loadConfigFile(File f) {

        configFile = f;
        ArrayList<String> bloccs;
        try {
            bloccs = new ArrayList<>(Files.readAllLines(Paths.get(f.getPath())));
            if (bloccs.size() == 0) return;
            String consider = bloccs.get(0);
            int i = Integer.parseInt(consider);
            gainToLoad = i - 50f;
            rawGainToLoad = i / 100f;
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void saveNewGain(int gain) {

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(configFile.getAbsolutePath(), "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.println(gain);
        writer.close();


    }


    public static void init() throws JavaLayerException {

        if (player != null) player.close();
        player = new PausablePlayer(cachedTrack);
        player.player.setGain(gainToLoad);
        new Thread(() -> {
            try {
                SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
                SoundSystemConfig.addLibrary(LibraryJavaSound.class);
                system = new SoundSystem(LibraryJavaSound.class);

                try {


                    system.newSource(true, "vinyl", new URL("http://static.hivemc.com/bp/sfx/vinylstop.ogg"), "vinyl.ogg", false,
                            0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0);
                    system.newSource(true, "cheer", new URL("http://static.hivemc.com/bp/sfx/cheer.ogg"), "cheer.ogg", false,
                            0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0);


                    system.setVolume("vinyl", rawGainToLoad);
                    system.setVolume("cheer", rawGainToLoad);

                    system.setTemporary("vinyl", false);
                    system.setTemporary("cheer", false);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } catch (SoundSystemException e) {
                e.printStackTrace();
            }
        }).start();


    }

    public static void resume() {
        if (!player.firstResume) {
            player.firstResume = true;
            new Thread(() -> {
                try {
                    Thread.sleep(40);
                    player.player.setGain(gainToLoad);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }).start();
        }

        playing = true;
        player.player.setGain(gainToLoad);
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
        system.cleanup();
    }


    public static void playOgg(String which) {
        system.activate(which);
        system.stop(which);
        system.play(which);


    }


}
