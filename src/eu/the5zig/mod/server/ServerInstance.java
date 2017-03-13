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

package eu.the5zig.mod.server;

/**
 * Represents a custom server handler. Extend to create a custom server handler.
 * <p>
 * Create optionally a message resource file that is located in {@code core/servers}
 * of your plugin and called like {@link #getConfigName()} and ends with
 * {@code .properties}. This file should contain all messages that the mod should listen on. When
 * matched, the active {@link AbstractGameListener} will be called with the message key.
 */
public abstract class ServerInstance {

	/**
	 * Utility class that holds all game mode listeners and contains util methods to send & ignore server messages.
	 */
	GameListenerRegistry gameListener;

	/**
	 * Register all your game mode listeners in this method, using {@link #getGameListener()}
	 * and {@link GameListenerRegistry#registerListener(AbstractGameListener)}
	 */
	public abstract void registerListeners();

	/**
	 * @return the display name of the server instance.
	 */
	public abstract String getName();

	/**
	 * @return the config name of the server instance, used for modules and the message property file.
	 */
	public abstract String getConfigName();

	/**
	 * Called, whenever the client joins a Minecraft server to check, whether the server
	 * is handled by any registered server instance.
	 *
	 * @param host the host of the Minecraft server.
	 * @param port the port of the Minecraft server.
	 * @return true, if the server should be handled by this server instance.
	 */
	public abstract boolean handleServer(String host, int port);

	/**
	 * @return an utility class that holds all registered listeners.
	 */
	public final GameListenerRegistry getGameListener() {
		return gameListener;
	}
}
