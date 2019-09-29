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

package eu.beezig.core.utils.rpc;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.User;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import eu.beezig.core.BeezigMain;
import eu.beezig.core.Log;
import eu.beezig.core.settings.Setting;
import eu.beezig.core.utils.URLs;
import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Random;

public class DiscordUtils {


    public static boolean shouldOperate = true;
    private static IPCClient rpcClient;
    private static String joinSecret;
    private static String lastPresence;
    private static String[] lastPresenceOpts;
    private static DiscordParty party;
    public static String lastInviteId;

    public static void init() {
        if (!Setting.DISCORD_RPC.getValue() || BeezigMain.laby) {
            shouldOperate = false;
            return;
        }
        IPCClient client = new IPCClient(439523115383652372L);

        client.setListener(new IPCListener() {

            @Override
            public void onActivityJoinRequest(IPCClient client, String secret, User user) {
                lastInviteId = user.getId();
                The5zigAPI.getAPI().messagePlayer(Log.info + "§b" + user.getName() + "#" + user.getDiscriminator()
                    + "§3 would like to play with you. Run §b/beezig discord§3 or check Discord to accept.");
            }

            @Override
            public void onActivityJoin(IPCClient client, String secret) {
                String json = new String(Base64.getDecoder().decode(secret), Charset.forName("UTF-8"));
                try {
                    JSONObject obj = (JSONObject) new JSONParser().parse(json);
                    String ign = obj.get("u").toString();
                    String password = obj.get("p").toString();

                    The5zigAPI.getAPI().sendPlayerMessage("/party join " + ign + " " + password);
                    JSONObject team = (JSONObject) obj.get("t");
                    party = new DiscordParty((int)team.get("m"), team.get("i").toString());

                    The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully accepted invite. You are now in the user's party.");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onReady(IPCClient client, User user) {
                System.out.println("Connected to Discord as " + user.getName() + "#" + user.getDiscriminator() + "! ("
                        + user.getId() + ")");

                client.subscribe(IPCClient.Event.ACTIVITY_JOIN_REQUEST);
                client.subscribe(IPCClient.Event.ACTIVITY_JOIN);

                new Thread(() -> {
                    try {
                        URL url = new URL(URLs.REPORTS_URL + "/discord/check/"
                                + user.getId());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.addRequestProperty("User-Agent", Log.getUserAgent());
                        if (conn.getResponseCode() == 404 && !Setting.IGNORE_WARNINGS.getValue()) {
                            The5zigAPI.getAPI().messagePlayer(Log.info
                                    + "You are using Discord, but you're not in our server! Make sure to join.");
                            The5zigAPI.getAPI().messagePlayer(Log.info + "Invite: §ehttp://discord.gg/se7zJsU");
                            The5zigAPI.getAPI().messagePlayer(Log.info +
                                    "Alternatively, you can ignore this with /settings ignore_warnings true");
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

    public static void reloadPresence() {
        updatePresence(lastPresence, lastPresenceOpts);
    }

    public static void accept() {
        rpcClient.sendResult(0, lastInviteId);
    }

    public static void noParty() {
        joinSecret = null;
        party.unregister();
        party = null;
        reloadPresence();
    }

    public static void setSecret(String password) {
        String ign = The5zigAPI.getAPI().getGameProfile().getName();
        String partyPassword = password == null ? "beezig-" + Integer.toString(new Random().nextInt()) : password;

        JSONObject obj = new JSONObject();
        obj.put("u", ign);
        obj.put("p", partyPassword);

        if(password == null)
            The5zigAPI.getAPI().sendPlayerMessage("/party password " + partyPassword);
        The5zigAPI.getAPI().sendPlayerMessage("/party");

        party = new DiscordParty(24, "beezig-" + ign + "-" + Integer.toString(new Random().nextInt()));
        obj.put("t", party.toJson());
        joinSecret = Base64.getEncoder().encodeToString(obj.toJSONString().getBytes(Charset.forName("UTF-8")));
        The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully set up your party. You can now invite friends through Discord.");
        The5zigAPI.getAPI().messagePlayer(Log.info + "For more info:§b https://beezig.eu/wiki/discord-party\n");

        reloadPresence();
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
                lastPresence = newPresence;
                lastPresenceOpts = opts;

                RichPresence.Builder builder = new RichPresence.Builder();
                builder.setDetails(newPresence)
                        .setStartTimestamp(OffsetDateTime.now())
                        .setLargeImage("background");

                if (opts.length >= 1)
                    builder.setState(opts[0]);
                if (opts.length >= 2)
                    builder.setSmallImage(opts[1]);

                if(joinSecret != null)
                    builder.setJoinSecret(joinSecret);
                if(party != null)
                    builder.setParty(party.getId(), party.getMembers(), party.getMaxMembers());


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
