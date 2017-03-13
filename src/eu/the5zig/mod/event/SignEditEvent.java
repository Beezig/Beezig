package eu.the5zig.mod.event;

/**
 * Called when a player finishes editing a sign.
 *
 * @since 1.0.9
 */
public class SignEditEvent extends Event {

	/**
	 * The lines of the sign
	 */
	private String[] lines;

	public SignEditEvent(String[] lines) {
		this.lines = lines;
	}

	/**
	 * Gets the text of a specific line.
	 *
	 * @param index the index of the line.
	 * @return the text of a specific line.
	 */
	public String getLine(int index) {
		return lines[index];
	}

	/**
	 * Changes the text of a specific line.
	 *
	 * @param index the index of the line
	 * @param text  the new text
	 * @throws NullPointerException if the text is {@code null}.
	 */
	public void setLine(int index, String text) {
		if (text == null) {
			throw new NullPointerException("Line cannot be null.");
		}
		this.lines[index] = text;
	}

	/**
	 * @return an array that contains all lines of the sign.
	 */
	public String[] getLines() {
		return lines;
	}
}
