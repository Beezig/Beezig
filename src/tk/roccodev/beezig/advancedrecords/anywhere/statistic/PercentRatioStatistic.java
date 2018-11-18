package tk.roccodev.beezig.advancedrecords.anywhere.statistic;

import org.json.simple.JSONObject;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.settings.Setting;

public class PercentRatioStatistic extends RatioRecordsStatistic {

    public PercentRatioStatistic(String key, String apiKey1, String apiKey2) {
        super(key, apiKey1, apiKey2);
    }

    public PercentRatioStatistic(String key, String apiKey1, String apiKey2, Setting setting) {
        super(key, apiKey1, apiKey2, setting);
    }


    @Override
    public Object getValue(JSONObject o) {
        return Log.ratio(super.getValueRaw(o) * 100) + "%";
    }
}
