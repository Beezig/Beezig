package eu.the5zig.mod.event;

/**
 * Fired, when the server teleports the client to a new Location
 */
public class PlayerTeleportEvent extends Event {

	/**
	 * The absolute x-coordinate of the player.
	 */
	private double x;
	/**
	 * The absolute y-coordinate of the player.
	 */
	private double y;
	/**
	 * The absolute z-coordinate of the player.
	 */
	private double z;
	/**
	 * The yaw-rotation of the player.
	 */
	private float yaw;
	/**
	 * The pitch-rotation of the player.
	 */
	private float pitch;

	public PlayerTeleportEvent(double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	/**
	 * @return the absolute x-coordinate of the player.
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the absolute y-coordinate of the player.
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return the absolute z-coordinate of the player.
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @return the yaw-rotation of the player.
	 */
	public float getYaw() {
		return yaw;
	}

	/**
	 * @return the pitch-rotation of the player.
	 */
	public float getPitch() {
		return pitch;
	}
}
