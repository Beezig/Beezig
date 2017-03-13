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
 * Represents multiple messages that have been ignored.
 */
public interface IMultiPatternResult {

	/**
	 * Tries to find a message that messages a specific pattern, specified by the messages file resource key, and
	 * removes it from the ignored messages list.
	 *
	 * @param key the resource key of the pattern.
	 * @return a pattern result or null, if no such message has been ignored.
	 */
	IPatternResult parseKey(String key);

	/**
	 * @return the amount of messages that have not yet been handled.
	 */
	int getRemainingMessageCount();

	/**
	 * Gets a message at a specific index.
	 *
	 * @param index the index of the message.
	 * @return the message at the specified index.
	 */
	String getMessage(int index);

}
