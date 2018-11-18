package tk.roccodev.beezig.advancedrecords.anywhere.statistic;

import org.json.simple.JSONObject;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.settings.Setting;

public class RecordsStatistic {

    private String key;
    private String apiKey;
    private Setting setting;

    public String getKey() {
        return key;
    }

    public String getApiKey() {
        return apiKey;
    }

    public RecordsStatistic(String key, String apiKey) {
        this.key = key;
        this.apiKey = apiKey;
    }

    public RecordsStatistic(String key, String apiKey, Setting setting) {
        this(key, apiKey);
        this.setting = setting;
    }

    public Object getValue(JSONObject o) {
        return Setting.THOUSANDS_SEPARATOR.getValue() ? Log.df((long) getValueRaw(o)) : (int)getValueRaw(o);
    }

    double getValueRaw(JSONObject o) {
        return (long) o.get(apiKey);
    }

    public Setting getSetting() {
        return setting;
    }
}
