package eu.beezig.core.server;

public interface IMapExtra {
    /**
     * Called on each render, to retrieve the advanced map info
     * @return the advanced map info
     */
    String getMapInformation();
}
