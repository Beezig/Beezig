package eu.beezig.core.server;

public interface IDynamicMode {
    /**
     * Used for gamemodes that support multiple modes (SGN, SKY, BED).
     * Sets the current mode variant.
     * @param lobby the lobby ID, without the trailing numbers
     */
    void setModeFromLobby(String lobby);
}
