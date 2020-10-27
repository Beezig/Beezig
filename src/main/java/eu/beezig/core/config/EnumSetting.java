package eu.beezig.core.config;

/**
 * Represents an enum-like configuration entry.
 * Implementors must include the `static T[] values()` and `static T valueOf(String)` functions,
 * like the ones in Enum.
 */
public abstract class EnumSetting {
    public abstract String name();

    @Override
    public String toString() {
        return name();
    }
}
