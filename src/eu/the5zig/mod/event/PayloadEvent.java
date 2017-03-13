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

import io.netty.buffer.ByteBuf;

/**
 * Called, whenever the client receives a custom payload from the server.
 */
public class PayloadEvent extends Event {

	/**
	 * The channel the payload has been sent on.
	 */
	private String channel;
	/**
	 * The payload itself.
	 */
	private ByteBuf payload;

	public PayloadEvent(String channel, ByteBuf payload) {
		this.channel = channel;
		this.payload = payload;
	}

	/**
	 * @return the channel the payload has been sent on.
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * @return the custom payload that has been sent.
	 */
	public ByteBuf getPayload() {
		return payload;
	}
}
