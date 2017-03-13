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

package eu.the5zig.mod.gui;

/**
 * Represents an overlay that can display a notification in the top right corner of the screen.
 */
public interface IOverlay {

	/**
	 * Displays an overlay.
	 *
	 * @param title    the title of the overlay (will be rendered in yellow, if no other color specified).
	 * @param subtitle the subtitle of the overlay.
	 */
	void displayMessage(String title, String subtitle);

	/**
	 * Displays an overlay.
	 *
	 * @param message the subtitle of the overlay. The title will be {@code "The 5zig Mod"}
	 */
	void displayMessage(String message);

	/**
	 * Displays an overlay and splits the message into two lines.
	 *
	 * @param message the message that should be displayed.
	 */
	void displayMessageAndSplit(String message);

}
