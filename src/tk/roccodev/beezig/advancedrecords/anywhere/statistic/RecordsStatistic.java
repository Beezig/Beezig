package tk.roccodev.beezig.advancedrecords.anywhere.statistic;

import org.json.simple.JSONObject;

public class RecordsStatistic {

    private String key;
    private String apiKey;

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

    public Object getValue(JSONObject o) {
        return (int) getValueRaw(o);
    }

    double getValueRaw(JSONObject o) {
        return (long) o.get(apiKey);
    }

}
