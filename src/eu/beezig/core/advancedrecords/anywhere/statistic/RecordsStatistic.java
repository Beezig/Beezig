package eu.beezig.core.advancedrecords.anywhere.statistic;

import eu.beezig.core.Log;
import eu.beezig.core.settings.Setting;
import org.json.simple.JSONObject;

public class RecordsStatistic {

    private String key;
    private String apiKey;
    private Setting setting;

    public RecordsStatistic(String key, String apiKey) {
        this.key = key;
        this.apiKey = apiKey;
    }

    public RecordsStatistic(String key, String apiKey, Setting setting) {
        this(key, apiKey);
        this.setting = setting;
    }

    public String getKey() {
        return key;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Object getValue(JSONObject o) {
        return Setting.THOUSANDS_SEPARATOR.getValue() ? Log.df((long) getValueRaw(o)) : (int) getValueRaw(o);
    }

    public double getValueRaw(JSONObject o) {
        return (long) o.get(apiKey);
    }

    public Setting getSetting() {
        return setting;
    }
}
