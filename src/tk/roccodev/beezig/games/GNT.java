package tk.roccodev.beezig.games;

public class GNT extends Giant {

    public static GNT instance;

    public GNT() {
        instance = this;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "SkyGiants";
    }

    //No need to override isMini(), as it returns false as default.


}
