package tk.roccodev.beezig.advancedrecords.anywhere.statistic;

import org.json.simple.JSONObject;
import tk.roccodev.beezig.hiveapi.wrapper.APIUtils;

public class TimeStatistic extends RecordsStatistic {

    public TimeStatistic(String key, String apiKey) {
        super(key, apiKey);
    }

    @Override
    public Object getValue(JSONObject o) {
        return APIUtils.getTimePassed((long)getValueRaw(o));
    }
}
