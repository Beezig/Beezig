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
 * Fired, whenever the client presses a key while in game.
 */
public class KeyPressEvent extends Event {

	/**
	 * The {@link org.lwjgl.input.Keyboard} key code of the key that has been pressed.
	 */
	private int keyCode;

	public KeyPressEvent(int keyCode) {
		this.keyCode = keyCode;
	}

	/**
	 * @return the {@link org.lwjgl.input.Keyboard} key code of the key that has been pressed.
	 */
	public int getKeyCode() {
		return keyCode;
	}
}
