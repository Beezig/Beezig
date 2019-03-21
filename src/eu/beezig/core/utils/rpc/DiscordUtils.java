package eu.beezig.core.utils.rpc;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.User;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import eu.beezig.core.Log;
import eu.beezig.core.settings.Setting;
import eu.beezig.core.utils.URLs;
import eu.the5zig.mod.The5zigAPI;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.OffsetDateTime;

public class DiscordUtils {


    public static boolean shouldOperate = true;
    private static IPCClient rpcClient;

    public static void init() {
        if (!Setting.DISCORD_RPC.getValue())
            return;
        IPCClient client = new IPCClient(439523115383652372L);
        client.setListener(new IPCListener() {
            @Override
            public void onReady(IPCClient client, User user) {
                System.out.println("Connected to Discord as " + user.getName() + "#" + user.getDiscriminator() + "! ("
                        + user.getId() + ")");

                new Thread(() -> {
                    try {
                        URL url = new URL(URLs.REPORTS_URL + "/discord/check/"
                                + user.getId());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.addRequestProperty("User-Agent", Log.getUserAgent());
                        if (conn.getResponseCode() == 404) {
                            The5zigAPI.getAPI().messagePlayer(Log.info
                                    + "You are using Discord, but you're not in our server! Make sure to join.\nInvite: §ehttp://discord.gg/se7zJsU");
                        }

                    } catch (IOException e) {
                        // TODO Auto-generated catch block

                    }
                }, "Server Ping").start();

                rpcClient = client;


            }

        });
        try {
            client.connect();
        } catch (NoDiscordClientException e) {
            e.printStackTrace();
        }
    }


    /**
     * Updates the RPC presence.
     *
     * @param newPresence details
     * @param opts:       State (Lobby, game etc.); 1: Image; 2: Max party size (e.g. Teams
     *                    of 4); 3: Party name (e.g., Aqua Team)
     */
    public static void updatePresence(String newPresence, String... opts) {
        if (!shouldOperate)
            return;
        if (!Setting.DISCORD_RPC.getValue())
            return;
        new Thread(() -> {
            try {

                RichPresence.Builder builder = new RichPresence.Builder();
                builder.setDetails(newPresence)
                        .setStartTimestamp(OffsetDateTime.now())
                        .setLargeImage("background");

                if (opts.length >= 1)
                    builder.setState(opts[0]);
                if (opts.length >= 2)
                    builder.setSmallImage(opts[1]);


                rpcClient.sendRichPresence(builder.build());


            } catch (Throwable e) {
                e.printStackTrace();
                DiscordUtils.shouldOperate = false;

            }
        }).start();

    }

    public static void clearPresence() {
        if (rpcClient != null)
            rpcClient.sendRichPresence(null);
    }

    public static void closeClient() {
        if (rpcClient != null)
            rpcClient.close();
    }

}
