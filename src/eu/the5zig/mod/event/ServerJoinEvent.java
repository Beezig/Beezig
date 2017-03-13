/*
 *    Copyright 2016 5zig
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.the5zig.mod.event;

/**
 * Fired, whenever the client joins a Minecraft server (network). This does <b>not</b> include switching servers on a BungeeCord network.
 */
public class ServerJoinEvent extends Event {

	/**
	 * The host of the server.
	 */
	private String host;
	/**
	 * The port of the port.
	 */
	private int port;

	public ServerJoinEvent(String host, int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * @return the host of the server.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the port of the server.
	 */
	public int getPort() {
		return port;
	}
}
