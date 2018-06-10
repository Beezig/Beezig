package tk.roccodev.beezig.briefing.fetcher;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.briefing.News;
import tk.roccodev.beezig.briefing.Pools;
import tk.roccodev.beezig.briefing.lergin.NewMap;
import tk.roccodev.beezig.briefing.lergin.StaffChangeType;
import tk.roccodev.beezig.briefing.lergin.StaffUpdate;
import tk.roccodev.beezig.hiveapi.wrapper.APIUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

public class NewsFetcher {


    public static ArrayList<News> getApplicableNews(long lastLogin) {

        ArrayList<News> tr = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            JSONArray arr = (JSONArray) parser.parse(APIUtils.readURL(new URL(Links.OUR_NEWS)));
            System.out.println("Fetched " + arr.size() + " news. Lastlogin is " + lastLogin);
            for (Object o : arr) {
                JSONObject j = (JSONObject) o;

                long postedAt = (long) j.get("postedAt");
                if (j.containsKey("versionExclusive")) {
                    JSONArray versions = (JSONArray) j.get("versionExclusive");
                    boolean cont = true;
                    for (Object o1 : versions) {
                        Long l = (long) o1;
                        if (l < 0) { //e.g, -460 is exclusive to all versions below 4.6.0
                            if (BeezigMain.getCustomVersioning() >= Math.abs(l)) {
                                break;
                            }

                        } else if (BeezigMain.getCustomVersioning() == l) {
                            cont = false;
                            break;
                        }
                    }
                    if (cont) continue;
                }


                if (postedAt < lastLogin) continue;

                tr.add(new News((String) j.get("title"), (String) j.get("content"), (long) j.get("postedAt")));

            }
            System.out.println("Loaded " + tr.size() + " news.");

        } catch (IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return tr;
    }

    public static ArrayList<NewMap> getApplicableNewMaps(long lastLogin) {

        ArrayList<NewMap> tr = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            JSONArray arr = (JSONArray) parser.parse(APIUtils.readURL(new URL(Links.LERGIN_MAP)));
            System.out.println("Fetched " + arr.size() + " maps. Lastlogin is " + lastLogin);

            for (Object o : arr) {
                JSONObject j = (JSONObject) o;

                long postedAt = (long) j.get("date");
                if (postedAt < lastLogin) continue;

                tr.add(new NewMap((String) j.get("gameType"), (String) j.get("mapName")));
            }
            System.out.println("Loaded " + tr.size() + " maps.");

        } catch (Exception e) {

            Pools.error = true;
            e.printStackTrace();
        }
        if (tr.size() < 10) return tr;
        return new ArrayList<>(tr.subList(0, 10));
    }

    public static ArrayList<StaffUpdate> getApplicableStaffUpdates(long lastLogin) {

        ArrayList<StaffUpdate> tr = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            JSONArray arr = (JSONArray) parser.parse(APIUtils.readURL(new URL(Links.LERGIN_STAFF)));
            System.out.println("Fetched " + arr.size() + " staff updates. Lastlogin is " + lastLogin);

            for (Object o : arr) {
                JSONObject j = (JSONObject) o;

                long postedAt = (long) j.get("date");
                if (postedAt < lastLogin) continue;

                StaffUpdate s = new StaffUpdate(StaffChangeType.valueOf((String) j.get("type")), (String) j.get("name"));

                tr.add(s);
            }
            System.out.println("Loaded " + tr.size() + " staff updates.");

        } catch (IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (tr.size() > 10) tr = new ArrayList<>(tr.subList(0, 10));
        tr.sort(Comparator.comparing(StaffUpdate::getType));


        return tr;
    }


}
