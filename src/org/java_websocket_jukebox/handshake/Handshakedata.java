package org.java_websocket_jukebox.handshake;

import java.util.Iterator;

public interface Handshakedata {
	public Iterator<String> iterateHttpFields();
	public String getFieldValue( String name );
	public boolean hasFieldValue( String name );
	public byte[] getContent();
}
