package org.java_websocket_remap.server;

import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;

import org.java_websocket_remap.WebSocketAdapter;
import org.java_websocket_remap.WebSocketImpl;
import org.java_websocket_remap.drafts.Draft;
import org.java_websocket_remap.server.WebSocketServer.WebSocketServerFactory;

public class DefaultWebSocketServerFactory implements WebSocketServerFactory {
	@Override
	public WebSocketImpl createWebSocket( WebSocketAdapter a, Draft d, Socket s ) {
		return new WebSocketImpl( a, d );
	}
	@Override
	public WebSocketImpl createWebSocket( WebSocketAdapter a, List<Draft> d, Socket s ) {
		return new WebSocketImpl( a, d );
	}
	@Override
	public SocketChannel wrapChannel( SocketChannel channel, SelectionKey key ) {
		return (SocketChannel) channel;
	}
}