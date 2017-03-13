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
 * Represents the result of a matched pattern.
 */
public interface IPatternResult {

	/**
	 * @return the size of the result.
	 */
	int size();

	/**
	 * Gets a matched group at the specified index.
	 *
	 * @param index the index.
	 * @return a matched group at the specified index or an empty string, in the index exceeds the total group count.
	 */
	String get(int index);

	/**
	 * Allows you to ignore the matched message.
	 *
	 * @param ignore true, if the matched message should be ignored.
	 * @since 1.0.1
	 */
	void ignoreMessage(boolean ignore);

}
