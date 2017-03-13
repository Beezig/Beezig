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

package eu.the5zig.mod;

import com.mojang.authlib.GameProfile;
import eu.the5zig.mod.gui.IOverlay;
import eu.the5zig.mod.gui.ingame.ArmorSlot;
import eu.the5zig.mod.gui.ingame.ItemStack;
import eu.the5zig.mod.gui.ingame.PotionEffect;
import eu.the5zig.mod.gui.ingame.Scoreboard;
import eu.the5zig.mod.modules.AbstractModuleItem;
import eu.the5zig.mod.modules.Category;
import eu.the5zig.mod.plugin.PluginManager;
import eu.the5zig.mod.render.Formatting;
import eu.the5zig.mod.render.RenderHelper;
import eu.the5zig.mod.server.ServerInstance;
import eu.the5zig.mod.util.IKeybinding;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.mod.util.PlayerGameMode;
import io.netty.buffer.ByteBuf;

import java.util.List;

/**
 * Main API class.
 */
public interface ModAPI {

	/**
	 * @return the version of the 5zig mod.
	 */
	String getModVersion();

	/**
	 * @return the version of Minecraft.
	 */
	String getMinecraftVersion();

	/**
	 * Use this method to do important checks when in forge environment. Things like
	 * reflection *may* not work properly then.
	 *
	 * @return true, if we are in a forge environment.
	 */
	boolean isForgeEnvironment();

	/**
	 * @return the plugin manager.
	 */
	PluginManager getPluginManager();

	/**
	 * Registers a new ModuleItem.
	 *
	 * @param plugin     the plugin instance.
	 * @param key        a unique key of the module item.
	 * @param moduleItem the class of the module item.
	 * @param category   the category of the module item.
	 */
	void registerModuleItem(Object plugin, String key, Class<? extends AbstractModuleItem> moduleItem, Category category);

	/**
	 * Registers a new ModuleItem.
	 *
	 * @param plugin     the plugin instance.
	 * @param key        a unique key of the module item.
	 * @param moduleItem the class of the module item.
	 * @param category   the category of the module item.
	 */
	void registerModuleItem(Object plugin, String key, Class<? extends AbstractModuleItem> moduleItem, String category);

	/**
	 * Checks if the specified module item is currently activate.
	 *
	 * @param key the key of the module item.
	 * @return true, if the module item is currently active
	 */
	boolean isModuleItemActive(String key);

	/**
	 * Registers a new server instance listener.
	 *
	 * @param plugin         the plugin instance.
	 * @param serverInstance the class of the own server instance.
	 */
	void registerServerInstance(Object plugin, Class<? extends ServerInstance> serverInstance);

	/**
	 * @return the active server instance or null, if no server instance is active.
	 */
	ServerInstance getActiveServer();

	/**
	 * Registers a new keybinding.
	 *
	 * @param description the name / description of the keybinding.
	 * @param keyCode     the key code of the keybinding.
	 * @param category    the category of the keybinding.
	 * @return the registered keybinding.
	 * @see org.lwjgl.input.Keyboard
	 */
	IKeybinding registerKeyBiding(String description, int keyCode, String category);

	/**
	 * @return a class that contains some utility methods for rendering strings and rectangles.
	 */
	RenderHelper getRenderHelper();

	/**
	 * @return the default formatting of all module items.
	 */
	Formatting getFormatting();

	/**
	 * Creates a new overlay message that will appear in the top right corner, simmilar to achievement messages.
	 *
	 * @return a new overlay
	 */
	IOverlay createOverlay();

	/**
	 * Translates a key with current Resource Bundle and formats the String.
	 *
	 * @param key    The key of the Resource.
	 * @param format The objects to format the String
	 * @return The formatted, translated value of the key.
	 */
	String translate(String key, Object... format);

	/**
	 * @return true, if the player is currently playing in a world.
	 */
	boolean isInWorld();

	/**
	 * Sends a message from the player to the server. Use {@code /} for commands.
	 *
	 * @param message the message that should be sent.
	 */
	void sendPlayerMessage(String message);

	/**
	 * Sends a message directly to the client / chat window.
	 *
	 * @param message the message that should be sent.
	 */
	void messagePlayer(String message);

	/**
	 * Sends a message directly to the second chat of the client.
	 *
	 * @param message the message that should be sent.
	 */
	void messagePlayerInSecondChat(String message);

	/**
	 * Sends a custom payload to the server.
	 *
	 * @param channel the channel the payload should be sent on.
	 * @param payload the payload itself.
	 */
	void sendPayload(String channel, ByteBuf payload);

	/**
	 * @return the width of the window.
	 */
	int getWindowWidth();

	/**
	 * @return the height of the window.
	 */
	int getWindowHeight();

	/**
	 * @return the scaled width of the window.
	 */
	int getScaledWidth();

	/**
	 * @return the scaled height of the window.
	 */
	int getScaledHeight();

	/**
	 * @return the scale factor of the window.
	 */
	int getScaleFactor();

	/**
	 * @return the game profile of the current player.
	 */
	GameProfile getGameProfile();

	/**
	 * @return the ip of the server the player is currently playing on.
	 */
	String getServer();

	/**
	 * @return a list containing all entries of the server player list.
	 * @since 1.0.3
	 */
	List<NetworkPlayerInfo> getServerPlayers();

	/**
	 * @return the x-coordinate of the player or of the entity he is currently spectating.
	 */
	double getPlayerPosX();

	/**
	 * @return the y-coordinate of the player or of the entity he is currently spectating.
	 */
	double getPlayerPosY();

	/**
	 * @return the z-coordinate of the player or of the entity he is currently spectating.
	 */
	double getPlayerPosZ();

	/**
	 * @return true, if the player is looking at a block and the block is in range.
	 */
	boolean hasTargetBlock();

	/**
	 * @return the x-coordinate of the target block.
	 */
	int getTargetBlockX();

	/**
	 * @return the y-coordinate of the target block.
	 */
	int getTargetBlockY();

	/**
	 * @return the z-coordinate of the target block.
	 */
	int getTargetBlockZ();

	/**
	 * @return the current game mode of the player.
	 */
	PlayerGameMode getGameMode();

	/**
	 * @return a list with all active potion effects of the player.
	 */
	List<? extends PotionEffect> getActivePotionEffects();

	/**
	 * @return the ItemStack in the main hand of the player.
	 */
	ItemStack getItemInMainHand();

	/**
	 * @return the ItemStack in the off hand of the player.
	 */
	ItemStack getItemInOffHand();

	/**
	 * Gets the ItemStack in the specified armor slot.
	 *
	 * @param slot the armor slot.
	 * @return the ItemStack in the specified armor slot or null, if there is no item.
	 */
	ItemStack getItemInArmorSlot(ArmorSlot slot);

	/**
	 * Gets an ItemStack, specified by its resource name.
	 *
	 * @param resourceName the resource name of the ItemStack.
	 * @return the ItemStack or null, if there is no ItemStack for the specified resource name.
	 */
	ItemStack getItemByName(String resourceName);

	/**
	 * Gets an ItemStack, specified by its resource name.
	 *
	 * @param resourceName the resource name of the ItemStack.
	 * @param amount       the amount of the ItemStack.
	 * @return the ItemStack or null, if there is no ItemStack for the specified resource name.
	 */
	ItemStack getItemByName(String resourceName, int amount);

	/**
	 * Gets the total amount of items in the players inventory by its resource key.
	 *
	 * @param key the resource key of the ItemStack.
	 * @return the total amount of items.
	 */
	int getItemCount(String key);

	/**
	 * @return the title of the opened container or {@code null}, if no container is opened.
	 */
	String getOpenContainerTitle();

	/**
	 * @return the selected hotbar item slot
	 */
	int getSelectedHotbarSlot();

	/**
	 * Sets the current hotbar item slot.
	 *
	 * @param slot the index of the slot that should be selected. Must be from 0 to 8.
	 * @throws IndexOutOfBoundsException if the specified slot parameter is either less than zero or greater than 8.
	 */
	void setSelectedHotbarSlot(int slot);

	/**
	 * Simulates a right mouse click / use action of the current item
	 */
	void rightClickItem();

	/**
	 * @return a class, representing a sidebar scoreboard.
	 */
	Scoreboard getSideScoreboard();

	/**
	 * @return true, if the player list is currently visible.
	 */
	boolean isPlayerListShown();

	/**
	 * Joins a Minecraft Server.
	 *
	 * @param host the host of the server.
	 * @param port the port of the server.
	 */
	void joinServer(String host, int port);

	/**
	 * Plays a sound to the player. Sounds need to be Minecraft vanilla sounds.
	 *
	 * @param sound the resource key of the sound.
	 * @param pitch the pitch value of the sound.
	 * @since 1.0.2
	 */
	void playSound(String sound, float pitch);

	/**
	 * Plays a sound to the player. Sounds need to be either Minecraft vanilla sounds or loaded via an additional resource pack.
	 *
	 * @param domain the resource domain of the sound.
	 * @param sound  the resource key of the sound.
	 * @param pitch  the pitch value of the sound.
	 * @since 1.0.2
	 */
	void playSound(String domain, String sound, float pitch);

}
