package org.java_websocket_remap.handshake;

public interface ServerHandshake extends Handshakedata {
	public short getHttpStatus();
	public String getHttpStatusMessage();
}
