package eu.beezig.core.games;

public class GNTM extends Giant {

    public static GNTM instance;

    public GNTM() {
        instance = this;
    }

    @Override
    public boolean isMini() {

        return true;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "SkyGiants:Mini";
    }


}
