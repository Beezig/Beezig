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

package eu.the5zig.mod.modules;

import eu.the5zig.mod.render.RenderHelper;
import eu.the5zig.mod.render.RenderLocation;

/**
 * A module item that can be rendered onto the ingame screen.
 */
public abstract class AbstractModuleItem {

	/**
	 * Some properties of the module item, like settings and the formatting of it.
	 */
	ModuleItemProperties properties;

	/**
	 * @return the properties of this module item.
	 */
	public final ModuleItemProperties getProperties() {
		return properties;
	}

	/**
	 * Override this method to register new settings.
	 */
	public void registerSettings() {
	}

	/**
	 * Called, whenever the module item should be rendered.
	 *
	 * @param x              the x-position of the item.
	 * @param y              the y-position of the item.
	 * @param renderLocation the {@link RenderLocation} of the item.
	 * @param dummy          true, if the item should render dummy values.
	 */
	public abstract void render(int x, int y, RenderLocation renderLocation, boolean dummy);

	/**
	 * Implement to return the width of the module item. Usually {@link RenderHelper#getStringWidth(String)}.
	 *
	 * @param dummy true, if the item should render dummy values.
	 * @return the width of the module item.
	 */
	public abstract int getWidth(boolean dummy);

	/**
	 * Implement to return the height of the module item. Usually {@code 10}.
	 *
	 * @param dummy true, if the item should render dummy values.
	 * @return the height of the module item.
	 */
	public abstract int getHeight(boolean dummy);

	/**
	 * Override to specify, when the module item should be rendered.
	 *
	 * @param dummy true, if the item should render dummy values.
	 * @return true, if the item should be rendered.
	 */
	public boolean shouldRender(boolean dummy) {
		return true;
	}

	/**
	 * Override to return a display name.
	 *
	 * @return the name of the module item.
	 * @see #getTranslation() getTranslation(): use this method instead, if you want to return different names for different languages.
	 */
	public String getName() {
		return null;
	}

	/**
	 * @return the translation key of the module item.
	 */
	public String getTranslation() {
		return null;
	}

	/**
	 * Builds a prefix according to the specified text.
	 *
	 * @param prefixText the prefix.
	 * @return a formatted prefix string.
	 */
	protected final String getPrefix(String prefixText) {
		return properties.buildPrefix(prefixText);
	}

	/**
	 * Builds a prefix according to the current display name of the item.
	 *
	 * @return a formatted prefix string.
	 */
	protected final String getPrefix() {
		return properties.buildPrefix();
	}

	/**
	 * Shortens the decimals of a double.
	 *
	 * @param d the double that should be shortened.
	 * @return a shortened string of the double.
	 */
	protected final String shorten(double d) {
		return properties.shorten(d);
	}

	/**
	 * Shortens the decimals of a float.
	 *
	 * @param f the float that should be shortened.
	 * @return a shortened string of the float.
	 */
	protected final String shorten(float f) {
		return properties.shorten(f);
	}

}
