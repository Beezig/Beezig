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

package eu.beezig.core.utils.ws;

import eu.beezig.core.Log;
import eu.beezig.core.utils.soundcloud.TrackDownloader;
import eu.beezig.core.utils.soundcloud.TrackPlayer;
import eu.the5zig.mod.The5zigAPI;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import javazoom.jl.decoder.JavaLayerException;
import org.json.simple.JSONObject;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

public class Connector {

    public static Client client;
    public static SocketIO socket;

    public static String lastBPServer = null;

    public static String URL = "wss://api.beezig.eu";
    public static String IO_BP_URL = "https://api.hivemc.com:8443";


    public static void connectBP(String server) {
        if (socket != null) socket.disconnect();
        try {
            socket = new SocketIO(IO_BP_URL);
            socket.addHeader("User-Agent", Log.getUserAgent());
            SocketIO.setDefaultSSLSocketFactory(SSLContext.getDefault());
            socket.connect(new IOCallback() {
                @Override
                public void onDisconnect() {

                }

                @Override
                public void onConnect() {
                    System.out.println("Connected to BlockParty Websocket on server " + server);
                }

                @Override
                public void onMessage(String data, IOAcknowledge ack) {
                    System.out.println(data);
                }

                @Override
                public void onMessage(JSONObject json, IOAcknowledge ack) {
                    System.out.println("JSON Data: " + json.toJSONString());
                }

                @Override
                public void on(String event, IOAcknowledge ack, Object... args) {
                    if (args.length != 0 && args[0] != null)
                        System.out.println("Event from server: " + event + " with data " + args[0].toString() + " (" + args[0].getClass().getName() + ")");
                    switch (event) {
                        case "loadsong":
                            JSONObject json = (JSONObject) args[0];
                            JSONObject data = (JSONObject) json.get("data");
                            String name = (String) data.get("name");
                            The5zigAPI.getAPI().messagePlayer("§8▍ §bB§al§eo§6c§ck§3§lParty§8 ▏§3 Voting has ended! §bThe song §f" + name + "§b has won!");
                            String trackId = (String) data.get("soundcloud");
                            new Thread(() -> {
                                try {
                                    TrackPlayer.cachedTrack = TrackDownloader.trackStream(trackId);

                                    TrackPlayer.init();
                                    The5zigAPI.getAPI().messagePlayer(Log.info + "Loaded the song to your jukebox. To adjust the volume, use /vol [0-100].");
                                } catch (IOException | JavaLayerException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                            break;
                        case "control":
                            new Thread(() -> {
                                if (TrackPlayer.playing) {
                                    System.out.println("Pausing track...");
                                    TrackPlayer.stop();
                                    TrackPlayer.playOgg("vinyl");
                                } else {

                                    System.out.println("Resuming track...");
                                    TrackPlayer.playOgg("cheer");
                                    TrackPlayer.resume();

                                }
                            }).start();

                            break;
                        case "endgame":
                            new Thread(TrackPlayer::close).start();
                            break;
                    }
                }

                @Override
                public void onError(SocketIOException socketIOException) {
                    socketIOException.printStackTrace();
                }
            });
            JSONObject servObj = new JSONObject();
            servObj.put("server", server);
            lastBPServer = server;
            socket.emit("joinserver", servObj);


        } catch (MalformedURLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void leaveLobbyBP() {
        new Thread(() -> {
            if (socket != null) {
                JSONObject servObj = new JSONObject();
                if (lastBPServer != null) {
                    servObj.put("server", lastBPServer);
                    socket.emit("leaveserver", servObj);
                }
                socket.disconnect();
            }
            TrackPlayer.close();

        }).start();
    }

    public static void connect() {
        try {
            client = new Client(new URI(URL));
            // client = new Client(new URI("ws://localhost:8080"));
            client.connect();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }
}
