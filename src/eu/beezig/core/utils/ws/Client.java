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

import com.mojang.authlib.GameProfile;
import eu.beezig.core.BeezigMain;
import eu.beezig.core.Log;
import eu.beezig.core.utils.ws.api.PacketHandler;
import eu.beezig.core.utils.ws.api.PacketOpcodes;
import eu.the5zig.mod.The5zigAPI;
import org.java_websocket_beezig.client.WebSocketClient;
import org.java_websocket_beezig.handshake.ServerHandshake;
import org.json.simple.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class Client extends WebSocketClient {

    private static int reconnAttempts = 0;

    public Client(URI serverUri) {
        super(serverUri);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // TODO Auto-generated method stub
        System.out.println("Connected!");

        JSONObject message = new JSONObject();
        message.put("opcode", PacketOpcodes.S_IDENTIFICATION);
        message.put("ua", Log.getUserAgent());

        GameProfile profile = The5zigAPI.getAPI().getGameProfile();
        message.put("uuid", profile.getId().toString().replace("-", ""));
        message.put("name", profile.getName());
        message.put("platform", BeezigMain.laby ? "LabyMod" : "5zig");

        sendJson(message);

    }

    @Override
    public void onMessage(String message) {
        PacketHandler.parseIncomingPacket(message);
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {

        if(++reconnAttempts >= 50)
            return;

        System.out.println("Disconnected from WebSocket (" + code + "): " + reason + " [" + remote + "]");
        System.out.println("Attempting to reconnect...");
        new Thread(() -> {
            Connector.client.close();
            try {
                Connector.client = new Client(new URI(Connector.URL));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            Connector.client.connect();
        }).start();

    }

    @Override
    public void onError(Exception ex) {
        // TODO Auto-generated method stub
        ex.printStackTrace();
    }

    public void sendJson(JSONObject object) {
        send(object.toJSONString());
    }

}
