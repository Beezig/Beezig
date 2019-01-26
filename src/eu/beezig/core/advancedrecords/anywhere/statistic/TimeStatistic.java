package eu.beezig.core.advancedrecords.anywhere.statistic;

import eu.beezig.core.hiveapi.wrapper.APIUtils;
import org.json.simple.JSONObject;

public class TimeStatistic extends RecordsStatistic {

    public TimeStatistic(String key, String apiKey) {
        super(key, apiKey);
    }

    @Override
    public Object getValue(JSONObject o) {
        return APIUtils.getTimePassed((long) getValueRaw(o));
    }
}
