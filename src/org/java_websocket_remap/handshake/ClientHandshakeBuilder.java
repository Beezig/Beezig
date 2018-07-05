package org.java_websocket_remap.handshake;

public interface ClientHandshakeBuilder extends HandshakeBuilder, ClientHandshake {
	public void setResourceDescriptor( String resourcedescriptor );
}
