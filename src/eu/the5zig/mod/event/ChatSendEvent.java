package eu.the5zig.mod.event;

/**
 * Called before a chat message gets sent from the client to the server.
 *
 * @since 1.0.3
 */
public class ChatSendEvent extends Event implements Cancelable {

	/**
	 * The message of the event.
	 */
	private String message;
	/**
	 * Indicates whether the event has been cancelled.
	 */
	private boolean cancelled;

	public ChatSendEvent(String message) {
		this.message = message;
	}

	/**
	 * @return the chat message that will be sent to the server.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Alters the message that should be sent.
	 *
	 * @param message the new message that should be sent instead.
	 * @throws NullPointerException if the message is {@code null}.
	 */
	public void setMessage(String message) {
		if (message == null) {
			throw new NullPointerException("Message cannot be null.");
		}
		this.message = message;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
