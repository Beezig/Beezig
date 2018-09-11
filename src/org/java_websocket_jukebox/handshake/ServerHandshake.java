package org.java_websocket_jukebox.handshake;

public interface ServerHandshake extends Handshakedata {
	public short getHttpStatus();
	public String getHttpStatusMessage();
}
