package eu.beezig.core.advancedrecords.anywhere.statistic;

import eu.beezig.core.Log;
import eu.beezig.core.settings.Setting;
import org.json.simple.JSONObject;

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
    public double getValueRaw(JSONObject o) {
        long val1 = (long) o.get(ratio1);
        long val2 = (long) o.get(ratio2);

        return val1 / (double) val2;
    }

    @Override
    public Object getValue(JSONObject o) {
        return Log.ratio(getValueRaw(o));
    }
}
