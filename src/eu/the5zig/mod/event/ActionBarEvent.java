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
 * Fired, whenever the client receives an action bar message from the server.
 */
public class ActionBarEvent extends Event implements Cancelable {

	/**
	 * The message of the server.
	 */
	private String message;

	/**
	 * Indicates whether this event has been cancelled.
	 */
	private boolean cancelled;

	public ActionBarEvent(String message) {
		this.message = message;
	}

	/**
	 * @return the action bar message that the client has received from the server.
	 */
	public String getMessage() {
		return message;
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
