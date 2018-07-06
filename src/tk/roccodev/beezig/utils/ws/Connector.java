package tk.roccodev.beezig.utils.ws;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import javazoom.jl.decoder.JavaLayerException;
import org.json.simple.JSONObject;
import paulscode.sound.SoundSystemException;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.utils.soundcloud.TrackDownloader;
import tk.roccodev.beezig.utils.soundcloud.TrackPlayer;

import javax.net.ssl.SSLContext;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

public class Connector {

    public static Client client;
    public static SocketIO socket;

    public static String lastBPServer = null;

    public static String URL = "ws://beezignode-beezig-node.a3c1.starter-us-west-1.openshiftapps.com:80";
    public static String IO_BP_URL = "https://api.hivemc.com:8443";



    public static void connectBP(String server){
        if(socket != null) socket.disconnect();
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
                    if(args.length != 0 && args[0] != null)
                    System.out.println("Event from server: " + event + " with data " + args[0].toString() + " (" + args[0].getClass().getName() + ")");
                    if(event.equals("loadsong")) {
                        JSONObject json = (JSONObject) args[0];
                        JSONObject data = (JSONObject) json.get("data");
                        String trackId = (String) data.get("soundcloud");
                        new Thread(() -> {
                            try {
                                TrackPlayer.cachedTrack = TrackDownloader.trackStream(trackId);

                                TrackPlayer.init();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JavaLayerException e) {
                                e.printStackTrace();
                            } catch (UnsupportedAudioFileException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                    else if(event.equals("control")) {
                        new Thread(() -> {
                            if(TrackPlayer.playing)  {
                                System.out.println("Pausing track...");
                                TrackPlayer.stop();
                                TrackPlayer.playOgg("vinyl");
                            }
                            else {

                                    System.out.println("Resuming track...");
                                    TrackPlayer.playOgg("cheer");
                                    TrackPlayer.resume();

                                }
                        }).start();

                    }
                    else if(event.equals("endgame")) {
                        new Thread(TrackPlayer::close).start();
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


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void leaveLobbyBP() {
        new Thread(() -> {
            if(socket != null) {
                JSONObject servObj = new JSONObject();
                servObj.put("server", lastBPServer);
                socket.emit("leaveserver", servObj);
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
