package org.java_websocket_remap;

import java.net.Socket;
import java.util.List;

import org.java_websocket_remap.drafts.Draft;

public interface WebSocketFactory {
	public WebSocket createWebSocket( WebSocketAdapter a, Draft d, Socket s );
	public WebSocket createWebSocket( WebSocketAdapter a, List<Draft> drafts, Socket s );

}
