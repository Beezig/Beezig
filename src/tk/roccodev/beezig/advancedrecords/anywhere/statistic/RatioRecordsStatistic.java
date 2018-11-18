package tk.roccodev.beezig.advancedrecords.anywhere.statistic;

import org.json.simple.JSONObject;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.settings.Setting;

public class RatioRecordsStatistic extends RecordsStatistic {

    private String ratio1, ratio2;

    public RatioRecordsStatistic(String key, String apiKey1, String apiKey2) {
        super(key, apiKey1);
        ratio1 = apiKey1;
        ratio2 = apiKey2;
    }

    public RatioRecordsStatistic(String key, String apiKey1, String apiKey2, Setting setting) {
        super(key, apiKey1, setting);
        ratio1 = apiKey1;
        ratio2 = apiKey2;
    }

    @Override
    double getValueRaw(JSONObject o) {
        long val1 = (long) o.get(ratio1);
        long val2 = (long) o.get(ratio2);

        return val1 / (double) val2;
    }

    @Override
    public Object getValue(JSONObject o) {
        return Log.ratio(getValueRaw(o));
    }
}
