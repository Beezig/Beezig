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
 * Called, when the client has received a chat message from the server.
 */
public class ChatEvent extends Event implements Cancelable {

	/**
	 * The message of the server.
	 */
	private String message;
	/**
	 * An object that represents a minecraft internal chat component. Used internally to send messages to the second chat.
	 */
	private Object chatComponent;

	/**
	 * Indicates whether the event has been cancelled.
	 */
	private boolean cancelled;

	public ChatEvent(String message, Object chatComponent) {
		this.message = message;
		this.chatComponent = chatComponent;
	}

	/**
	 * @return the chat message that has been received.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the minecraft internal chat component.
	 */
	public Object getChatComponent() {
		return chatComponent;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
