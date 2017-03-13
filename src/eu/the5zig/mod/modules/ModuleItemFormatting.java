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

import eu.the5zig.util.minecraft.ChatColor;

/**
 * Stores the formatting of a module item.
 */
public interface ModuleItemFormatting {

	/**
	 * @return the formatting of the prefix (either {@link ChatColor#RESET}, {@link ChatColor#BOLD},
	 * {@link ChatColor#ITALIC}, {@link ChatColor#UNDERLINE} or {@link ChatColor#STRIKETHROUGH}).
	 */
	ChatColor getPrefixFormatting();

	/**
	 * @return the color of the prefix.
	 */
	ChatColor getPrefixColor();

	/**
	 * @return the main formatting (either {@link ChatColor#RESET}, {@link ChatColor#BOLD},
	 * {@link ChatColor#ITALIC}, {@link ChatColor#UNDERLINE} or {@link ChatColor#STRIKETHROUGH}).
	 */
	ChatColor getMainFormatting();

	/**
	 * @return the main color.
	 */
	ChatColor getMainColor();

}