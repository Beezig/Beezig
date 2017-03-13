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

package eu.the5zig.mod.gui.ingame;

import java.util.List;

public interface ItemStack {

	/**
	 * @return the amount of items in this stack
	 */
	int getAmount();

	/**
	 * @return the maximum durability of the ItemStack.
	 */
	int getMaxDurability();

	/**
	 * @return the current durability of the ItemStack.
	 */
	int getCurrentDurability();

	/**
	 * @return the resource key of the ItemStack.
	 */
	String getKey();

	/**
	 * @return the display name of the ItemStack.
	 */
	String getDisplayName();

	/**
	 * @return the lore of the ItemStack.
	 */
	List<String> getLore();

	/**
	 * @return the food regeneration amount of this item or {@code 0}, if this item is not a food item.
	 */
	int getHealAmount();

	/**
	 * @return the food saturation of this item or {@code 0}, if this item is not a food item.
	 */
	float getSaturationModifier();

	/**
	 * Renders the ItemStack to the screen at given coordinates.
	 *
	 * @param x                     the x-position of the ItemStack.
	 * @param y                     the y-position of the ItemStack.
	 * @param withGenericAttributes true, of the ItemStack should be rendered with generic attributes, indicated by small numbers.
	 */
	void render(int x, int y, boolean withGenericAttributes);

}
