package eu.beezig.core.util.migrate;

import com.google.common.base.Splitter;
import eu.beezig.core.Beezig;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.modes.DR;
import eu.beezig.core.server.modes.HIDE;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.text.Message;
import eu.the5zig.mod.event.ChatSendEvent;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.server.GameMode;

public class CommandMigration {
    private static final Splitter SPACE = Splitter.on(' ');
    @EventHandler
    public void onChatSend(ChatSendEvent event) {
        if(!ServerHive.isCurrent() || event.getMessage().charAt(0) != '/') return;
        GameMode mode = Beezig.api().getActiveServer().getGameListener().getCurrentGameMode();
        String message = SPACE.split(event.getMessage()).iterator().next();
        if(mode instanceof DR) {
            if("/wr".equalsIgnoreCase(message)) sendMessage("/drwr");
            else if("/pb".equalsIgnoreCase(message)) sendMessage("/drpb");
        } else if(mode instanceof HIDE) {
            if("/bs".equalsIgnoreCase(message)) sendMessage("/hidebs");
        }
    }

    private void sendMessage(String newCommand) {
        Message.info(Beezig.api().translate("msg.command.migrate", Color.accent() + newCommand + Color.primary()));
    }
}
