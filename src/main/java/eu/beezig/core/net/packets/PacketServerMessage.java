package eu.beezig.core.net.packets;

import eu.beezig.core.Beezig;
import eu.beezig.core.net.Packet;
import eu.beezig.core.net.handler.Connection;
import eu.beezig.core.net.util.PacketBuffer;
import eu.beezig.core.util.task.WorldTask;
import eu.beezig.core.util.text.Message;

public class PacketServerMessage implements Packet {
    private Type type;
    private FormattingType formattingType;
    private String key;
    private String[] format;

    @Override
    public void read(PacketBuffer buffer) {
        type = Type.values()[(int)buffer.readByte()];
        formattingType = FormattingType.values()[(int)buffer.readByte()];
        key = buffer.readString();
        if(formattingType == FormattingType.FORMATTED) {
            format = new String[buffer.readInt()];
            for(int i = 0; i < format.length; i++) format[i] = buffer.readString();
        }
    }

    @Override
    public void write(PacketBuffer buffer) {

    }

    @Override
    public void handle(Connection handler) {
        String message = formattingType == FormattingType.SIMPLE
                ? Message.translate(key)
                : Beezig.api().translate(key, format);
        WorldTask.submit(() -> {
            if (type == Type.INFO) Message.info(message);
            else Message.error(message);
        });
    }

    private enum Type {
        INFO, ERROR
    }

    private enum FormattingType {
        SIMPLE, FORMATTED
    }
}
