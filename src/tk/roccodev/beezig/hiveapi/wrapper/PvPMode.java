package tk.roccodev.beezig.hiveapi.wrapper;

public class PvPMode extends APIGameMode {

    public PvPMode(String playerName, String... UUID) {
        super(playerName, UUID);

    }

    public long getKills() {
        return (long) object("kills");
    }

    public long getDeaths() {

        return (long) object("deaths");

    }


}
