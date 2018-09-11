package tk.roccodev.beezig.hiveapi.wrapper.modes;

public class ApiGNT extends ApiGiant {

    public ApiGNT(String playerName, String... UUID) {
        super(playerName, UUID);
    }

    @Override
    public String getShortcode() {
        return "GNT";
    }
}
