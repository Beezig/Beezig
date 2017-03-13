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

import eu.the5zig.mod.config.IConfigItem;
import eu.the5zig.util.Callable;

public interface ModuleItemProperties {

	/**
	 * Adds a setting to the module item.
	 *
	 * @param key          the identifier of the setting.
	 * @param defaultValue the default value of the setting.
	 * @param maxValue     the maximum value for the setting. When reaching it, the setting will start at zero again.
	 * @see #getSetting(String)
	 */
	void addSetting(String key, int defaultValue, int maxValue);

	/**
	 * Adds a setting to the module item.
	 *
	 * @param key          the identifier of the setting.
	 * @param defaultValue the default value of the setting.
	 * @param maxValue     the maximum value for the setting. When reaching it, the setting will start at zero again.
	 * @param customValue  a {@link Callable} that is used to display a custom value.
	 * @see #getSetting(String)
	 */
	void addSetting(String key, int defaultValue, int maxValue, Callable<String> customValue);

	/**
	 * Adds a setting to the module item.
	 *
	 * @param key          the identifier of the setting.
	 * @param defaultValue the default value of the setting.
	 * @param maxValue     the maximum value for the setting. When reaching it, the setting will start at zero again.
	 * @see #getSetting(String)
	 */
	void addSetting(String key, float defaultValue, float maxValue);

	/**
	 * Adds a setting to the module item. Will be displayed as slider.
	 *
	 * @param key          the identifier of the setting.
	 * @param suffix       the suffix of the setting.
	 * @param defaultValue the default value of the setting.
	 * @param minValue     the minimum value of the setting.
	 * @param maxValue     the maximum value of the setting.
	 * @param steps        the amount of steps or {@code -1} if the value should not be clamped.
	 * @see #getSetting(String)
	 */
	void addSetting(String key, String suffix, float defaultValue, float minValue, float maxValue, int steps);

	/**
	 * Adds a setting to the module item.
	 *
	 * @param key          the identifier of the setting.
	 * @param defaultValue the default value of the setting.
	 * @see #getSetting(String)
	 */
	void addSetting(String key, boolean defaultValue);

	/**
	 * Adds a setting to the module item.
	 *
	 * @param key          the identifier of the setting.
	 * @param defaultValue the default value of the setting.
	 * @param enumClass    the class of the Enum.
	 * @see #getSetting(String)
	 */
	<E extends Enum> void addSetting(String key, E defaultValue, Class<E> enumClass);

	IConfigItem getSetting(String key);

	/**
	 * @return the formatting of the module item or {@code null}, if the default mod formatting
	 * should be used.
	 * @see #setFormatting(ModuleItemFormatting)
	 */
	ModuleItemFormatting getFormatting();

	/**
	 * Sets the formatting of the module item.
	 *
	 * @param formatting the formatting or {@code null}, if the default mod formatting should
	 *                   be used.
	 * @see #getFormatting()
	 */
	void setFormatting(ModuleItemFormatting formatting);

	/**
	 * @return true, if the prefix of the module item is currently visible.
	 * @see #setShowPrefix(boolean)
	 */
	boolean isShowPrefix();

	/**
	 * Sets the visibility of the prefix.
	 *
	 * @param showPrefix true, if the prefix of the module item should be shown.
	 * @see #isShowPrefix()
	 */
	void setShowPrefix(boolean showPrefix);

	/**
	 * @return the display name of the module name. Either {@link AbstractModuleItem#getName()}
	 * or {@link AbstractModuleItem#getTranslation()}, depending whether the method returns {@code null}
	 * or not.
	 */
	String getDisplayName();

	/**
	 * Builds a prefix according to the specified prefix. The formatting that will be used is
	 * either {@link #getFormatting()} or the default mod formatting.
	 *
	 * @param prefixText the prefix.
	 * @return a formatted prefix string.
	 * @see #buildPrefix()
	 */
	String buildPrefix(String prefixText);

	/**
	 * Builds a prefix according to the current display name. The formatting that will be used is
	 * either {@link #getFormatting()} or the default mod formatting.
	 *
	 * @return a formatted prefix string.
	 * @see #buildPrefix(String)
	 */
	String buildPrefix();

	/**
	 * Shortens the decimals of a double.
	 *
	 * @param d the double that should be shortened.
	 * @return a shortened string of the double.
	 */
	String shorten(double d);

	/**
	 * Shortens the decimals of a float.
	 *
	 * @param f the float that should be shortened.
	 * @return a shortened string of the float.
	 */
	String shorten(float f);

}
