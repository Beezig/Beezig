package eu.beezig.core.hiveapi.stuff;

public interface RankEnum {

    static RankEnum getFromDisplay(String display) {
        return null;
    }

    String getTotalDisplay();

    String getPrefix();

    String getDisplay();

}
