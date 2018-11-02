package org.java_websocket_jukebox.framing;

import org.java_websocket_jukebox.exceptions.InvalidDataException;

import java.nio.ByteBuffer;

public interface FrameBuilder extends Framedata {

	public abstract void setFin( boolean fin );

	public abstract void setOptcode( Opcode optcode );

	public abstract void setPayload( ByteBuffer payload ) throws InvalidDataException;

	public abstract void setTransferemasked( boolean transferemasked );

}