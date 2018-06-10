package tk.roccodev.beezig.hiveapi.wrapper.modes;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameMode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import tk.roccodev.beezig.games.DR;
import tk.roccodev.beezig.hiveapi.StuffFetcher;
import tk.roccodev.beezig.hiveapi.stuff.dr.DRMap;
import tk.roccodev.beezig.hiveapi.wrapper.APIUtils;
import tk.roccodev.beezig.hiveapi.wrapper.PvPMode;

public class ApiDR extends PvPMode {

    public ApiDR(String playerName, String... UUID) {
        super(playerName, UUID);

    }


    @Override
    public Class<? extends GameMode> getGameMode() {
        // TODO Auto-generated method stub
        return DR.class;
    }

    @Override
    public String getShortcode() {
        // TODO Auto-generated method stub
        return "DR";
    }

    public String getPersonalBest(DRMap map) {
        try {

            JSONObject mapRecords = (JSONObject) object("maprecords");
            Long time = (long) mapRecords.get(map.getHiveAPIName());

            if (time >= 60) {
                int seconds = Math.toIntExact(time) % 60;
                int minutes = Math.floorDiv(Math.toIntExact(time), 60);
                if (seconds < 10) {
                    return (minutes + ":0" + seconds);
                }
                return (minutes + ":" + seconds);
            }
            return "0:" + time;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getTotalPB() {
        try {
            JSONObject mapRecords = (JSONObject) object("maprecords");
            The5zigAPI.getLogger().info("mr-s" + mapRecords.size());
            The5zigAPI.getLogger().info("sf-s" + StuffFetcher.getDeathRunMaps().size());
            if (mapRecords.size() != StuffFetcher.getDeathRunMaps().size()) return null;
            //Doesn't have a time on every map

            long time = 0;

            for (Object x : mapRecords.values()) {
                time += (long) x;
            }

            int seconds = Math.toIntExact(time) % 60;
            int minutes = Math.floorDiv(Math.toIntExact(time), 60);
            if (seconds < 10) {
                return (minutes + ":0" + seconds);
            }
            return (minutes + ":" + seconds);

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public Double getRawWorldRecord(DRMap map) {
        String mapid = map.getSpeedrunID();
        JSONParser parser = new JSONParser();
        JSONObject run0 = null;
        // run0 = Information about the WR run entry on speedrun.com
        try {
            The5zigAPI.getLogger().info(APIUtils.readURL(APIUtils.speedrunPublic(mapid, 0)).toString());
            run0 = (JSONObject) parser.parse((
                    (JSONObject) parser.parse((
                            (JSONArray) parser.parse((
                                    (JSONObject) parser.parse((
                                            (JSONObject) parser.parse(
                                                    APIUtils.readURL(
                                                            APIUtils.speedrunPublic(mapid, 0)))).get("data").toString())).get("runs").toString())).get(0).toString())).get("run").toString());
        } catch (Exception e) {
            The5zigAPI.getLogger().info("Failed DRgetWR (run0)");
            e.printStackTrace();
        }

        Double time = null;
        try {
            //Returns the world record time in seconds
            time = (Double) ((JSONObject) parser.parse(run0.get("times").toString())).get("primary_t");
        } catch (Exception e) {
            The5zigAPI.getLogger().info("Failed DRgetWR (time)");
            e.printStackTrace();
        }
        return time;
    }

    public String getWorldRecord(DRMap map) {

        Double time = getRawWorldRecord(map);

        if (time >= 60) {
            int seconds = (int) (Math.floor(time) % 60);
            double millis = Math.floor(((time - seconds) - 60) * 1000) / 1000;
            int minutes = Math.floorDiv((int) (time - millis), 60);
            if (seconds < 10) {
                return (minutes + ":0" + (seconds + millis));
            }
            return (minutes + ":" + (seconds + millis));
        }
        return "0:" + time;

    }


    @Override
    public boolean supportsMonthly() {
        // TODO Auto-generated method stub
        return true;
    }


    public String getWorldRecordHolder(DRMap map) {
        String mapid = map.getSpeedrunID();
        String WRHolder;
        JSONParser parser = new JSONParser();
        JSONObject run0;
        // run0 = Information about the WR run entry on speedrun.com
        try {
            run0 = (JSONObject) parser.parse(((JSONObject) parser.parse(((JSONArray) parser.parse(((JSONObject) parser.parse(((JSONObject) parser.parse(APIUtils.readURL(APIUtils.speedrunPublic(mapid, 0)))).get("data").toString())).get("runs").toString())).get(0).toString())).get("run").toString());
        } catch (Exception e) {
            The5zigAPI.getLogger().info("Failed DRgetWRHolder (run0)");
            e.printStackTrace();
            return "No Holder";
        }
        try {
            //Returns the world record holder username... lmao
            WRHolder = ((JSONObject) parser.parse(((JSONObject) parser.parse(((JSONObject) parser.parse(((JSONObject) parser.parse(APIUtils.readURL(APIUtils.speedrunPublic((String) ((JSONObject) parser.parse(((JSONArray) parser.parse(((JSONObject) parser.parse(run0.toJSONString())).get("players").toString())).get(0).toString())).get("id"), 1)))).toJSONString())).get("data").toString())).get("names").toString())).get("international").toString();
        } catch (Exception e) {
            The5zigAPI.getLogger().info("Failed DRgetWRHolder (WRHolder)");
            e.printStackTrace();
            return "No Holder";
        }
        if (WRHolder == null) {
            try {
                WRHolder = (((JSONObject) parser.parse(((JSONArray) parser.parse(((JSONObject) parser.parse(run0.toJSONString())).get("players").toString())).get(0).toString())).get("name")).toString();
            } catch (Exception e) {
                The5zigAPI.getLogger().info("Failed DRgetWRHolder (guest)");
                e.printStackTrace();
                return "No Holder";
            }
        }
        return WRHolder != null ? WRHolder : "No Holder";
    }


    public long getGamesPlayedAsRunner() {
        return (long) object("runnergamesplayed");
    }

    public long getGamesPlayedAsDeath() {
        return (long) object("deathgamesplayed");
    }

    public long getVictoriesAsRunner() {
        return (long) object("runnerwins");
    }


}
