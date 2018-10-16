package tk.roccodev.beezig.hiveapi.wrapper.modes;

import eu.the5zig.mod.server.GameMode;
import org.json.simple.JSONObject;
import tk.roccodev.beezig.games.BED;
import tk.roccodev.beezig.hiveapi.stuff.bed.MonthlyPlayer;
import tk.roccodev.beezig.hiveapi.wrapper.APIUtils;
import tk.roccodev.beezig.hiveapi.wrapper.PvPMode;

import java.net.URL;
import java.util.Map;

public class ApiBED extends PvPMode {

    public ApiBED(String playerName, String... UUID) {
        super(playerName, UUID);

    }

    public static MonthlyPlayer getMonthlyStatusByPlace(int place) {
        try {
            JSONObject o = APIUtils.getObject(APIUtils.readURL(new URL("https://api.roccodev.pw/bedwars/monthlies/leaderboard?from="
            + (place - 1) + "&to=" + place)));
            for (Object e : o.entrySet()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) e;
                JSONObject j = (JSONObject) entry.getValue();
                    return new MonthlyPlayer(entry.getKey(), (String) j.get("name"), (int) (long) j.get("place"),
                            (long) j.get("points"), (long) j.get("kills"), (long) j.get("deaths"), (long) j.get("victories"),
                            (long) j.get("played"), (long) j.get("beds"), (long) j.get("teams"));



            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Class<? extends GameMode> getGameMode() {
        // TODO Auto-generated method stub
        return BED.class;
    }

    @Override
    public String getShortcode() {
        // TODO Auto-generated method stub
        return "BED";
    }

    public MonthlyPlayer getMonthlyStatus() {
        try {
            JSONObject j = APIUtils.getObject(APIUtils.readURL(new URL("https://api.roccodev.pw/bedwars/monthlies/profile/" + this.getUUID())));

            return new MonthlyPlayer(this.getUUID(), (String) j.get("name"), (int) (long) j.get("place"),
                    (long) j.get("points"), (long) j.get("kills"), (long) j.get("deaths"), (long) j.get("victories"),
                    (long) j.get("played"), (long) j.get("beds"), (long) j.get("teams"));
        } catch (Exception e) {
            return null;
        }

    }

    public int getStreak() {
        return Math.toIntExact((long)object("win_streak"));
    }

    @Override
    public boolean supportsMonthly() {
        // TODO Auto-generated method stub
        return true;
    }


}
