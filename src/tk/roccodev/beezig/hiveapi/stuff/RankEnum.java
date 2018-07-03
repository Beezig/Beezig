package tk.roccodev.beezig.hiveapi.stuff;

public interface RankEnum {

    String getTotalDisplay();
    String getPrefix();
    String getDisplay();
    static RankEnum getFromDisplay(String display) {
        return null;
    };

}
