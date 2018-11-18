package org.java_websocket_jukebox.client;

import org.java_websocket_jukebox.WebSocket;
import org.java_websocket_jukebox.WebSocketAdapter;
import org.java_websocket_jukebox.WebSocketImpl;
import org.java_websocket_jukebox.drafts.Draft;

import java.net.Socket;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;

public class DefaultWebSocketClientFactory implements WebSocketClient.WebSocketClientFactory {
	/**
	 * 
	 */
	private final WebSocketClient webSocketClient;
	/**
	 * @param webSocketClient
	 */
	public DefaultWebSocketClientFactory( WebSocketClient webSocketClient ) {
		this.webSocketClient = webSocketClient;
	}
	@Override
	public WebSocket createWebSocket( WebSocketAdapter a, Draft d, Socket s ) {
		return new WebSocketImpl( this.webSocketClient, d );
	}
	@Override
	public WebSocket createWebSocket( WebSocketAdapter a, List<Draft> d, Socket s ) {
		return new WebSocketImpl( this.webSocketClient, d );
	}
	@Override
	public ByteChannel wrapChannel( SocketChannel channel, SelectionKey c, String host, int port ) {
		if( c == null )
			return channel;
		return channel;
	}
}