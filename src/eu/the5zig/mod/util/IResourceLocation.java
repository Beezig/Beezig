package eu.the5zig.mod.util;

/**
 * Represents a Minecraft resource location.
 */
public interface IResourceLocation {

	/**
	 * @return the resource path.
	 */
	String getResourcePath();

	/**
	 * @return the resource domain (Eg. minecraft).
	 */
	String getResourceDomain();

}
