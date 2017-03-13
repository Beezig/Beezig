package eu.the5zig.mod.event;

import eu.the5zig.mod.gui.ingame.ItemStack;

/**
 * This event is called whenever an item gets set into the slot of a chest.
 */
public class ChestSetSlotEvent extends Event {

	/**
	 * The title of the chest.
	 */
	private String containerTitle;
	/**
	 * The slot where the {@link #itemStack} has been set to.
	 */
	private int slot;
	/**
	 * The {@link ItemStack} that has been set.
	 */
	private ItemStack itemStack;

	public ChestSetSlotEvent(String containerTitle, int slot, ItemStack itemStack) {
		this.containerTitle = containerTitle;
		this.slot = slot;
		this.itemStack = itemStack;
	}

	/**
	 * @return the title of the chest.
	 */
	public String getContainerTitle() {
		return containerTitle;
	}

	/**
	 * @return the slot, whether the {@link ItemStack} has been set to.
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * @return the {@link ItemStack} that has been set.
	 */
	public ItemStack getItemStack() {
		return itemStack;
	}
}
