package tk.roccodev.beezig.hiveapi.wrapper.modes;

public class ApiGNTM extends ApiGiant {


    public ApiGNTM(String playerName, String... UUID) {
        super(playerName, UUID);
    }

    @Override
    public String getShortcode() {
        return "GNTM";
    }
}
