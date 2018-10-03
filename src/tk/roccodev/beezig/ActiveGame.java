package tk.roccodev.beezig;

import tk.roccodev.beezig.api.BeezigAPI;

public class ActiveGame {

    private static String current = "";

    public static String current() {
        return current;
    }

    public static void set(String s) {
        current = s;
        if(BeezigMain.hasExpansion) BeezigAPI.get().getListener().setActiveGame(s);
    }

    public static boolean is(String game) {
        return current.toUpperCase().equals(game.toUpperCase());
    }

    public static void reset(String game) {
        if (is(game)) set("");
    }

}
