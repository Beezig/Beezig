package eu.beezig.core.hiveapi.stuff.dr;

public class DRMap {


    private int checkpoints;
    private String speedrunid;
    private String name;
    private String api;

    public DRMap(long checkpoints, String speedrunid, String name, String api) {


        this.checkpoints = Math.toIntExact(checkpoints);
        this.speedrunid = speedrunid;
        this.name = name;
        this.api = api;
    }


    public String getDisplayName() {
        return name;
    }

    public String getHiveAPIName() {
        return api;
    }

    public int getCheckpoints() {
        return checkpoints;
    }

    public String getSpeedrunID() {
        return speedrunid;
    }


}
