package eu.beezig.core.util.text;

import eu.beezig.core.Beezig;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.Color;
import eu.the5zig.mod.event.ChatSendEvent;
import eu.the5zig.mod.event.EventHandler;

public class MessageOverlay {
    private String currentTarget;

    @EventHandler
    public void onSend(ChatSendEvent event) {
        if(currentTarget == null || !ServerHive.isCurrent()) return;
        ServerHive hive = ServerHive.current();
        if(hive.isInPartyChat()) {
            // Ensure target is null after party chat reset
            currentTarget = null;
            return;
        }
        String message = event.getMessage();
        if(message.trim().charAt(0) == '/') return;
        event.setCancelled(true);
        Beezig.api().sendPlayerMessage("/msg " + currentTarget + " " + message);
    }

    public void follow(String target) {
        currentTarget = target;
        Message.info(Beezig.api().translate("msg.msg_overlay.follow", Color.accent() + target + Color.primary()));
    }

    public void reset() {
        currentTarget = null;
        Message.info(Message.translate("msg.msg_overlay.reset"));
    }
}
