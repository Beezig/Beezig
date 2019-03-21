package eu.beezig.core.utils.ws.api.handler;

import eu.beezig.core.Log;
import eu.beezig.core.utils.ws.api.PacketHandler;
import eu.beezig.core.utils.ws.api.PacketOpcodes;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import org.json.simple.JSONObject;

public class C03NewAnnouncement extends PacketHandler {
    @Override
    public int getOpcode() {
        return PacketOpcodes.C_NEW_ANNOUNCEMENT;
    }

    @Override
    public void handlePacket(JSONObject packetIn) {
        JSONObject data = (JSONObject)packetIn.get("data");

        String title = data.get("title").toString();
        String content = data.get("content").toString();

        The5zigAPI.getAPI().messagePlayer(Log.info + "§bNEW ANNOUNCEMENT!");
        The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.translateAlternateColorCodes('&', title.trim()));
        The5zigAPI.getAPI().messagePlayer(Log.info + ChatColor.translateAlternateColorCodes('&', content.trim()));
        The5zigAPI.getAPI().playSound("note.pling", 1f);
    }
}
