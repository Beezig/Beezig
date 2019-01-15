package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;
import pw.roccodev.beezig.hiveapi.wrapper.mojang.UsernameToUuid;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.hiveapi.wrapper.APIUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map;

public class BestGameCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "bestgame";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/bestgame", "/bg", "/maingame"};
    }

    private DecimalFormat df = new DecimalFormat();

    @Override
    public boolean execute(String[] args) {

        if (!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;


        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);


        new Thread(() -> {
            String player = args.length == 0
                    ? The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "")
                    : UsernameToUuid.getUUID(args[0]);

            if(player == null) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "Player not found.");
                return;
            }

            boolean displayAll = args.length > 1;
            The5zigAPI.getAPI().messagePlayer(Log.info + "Calculating, this will take a long time...");
            try {
                JSONObject data = APIUtils.getObject(APIUtils.readURL(new URL("https://app-beezigmainserver.wedeploy.io/bestgame/" + player)));
                JSONObject modes = (JSONObject) data.get("data");
                Date nextReset = new Date((long)data.get("cache"));

                Map.Entry<String, Double> best = null;
                Map.Entry<String, Double> worst = null;

                    for(Object o : modes.entrySet())  {
                        Map.Entry<String, Double> entry = parseEntry((Map.Entry<String, Object>)o);
                        if(entry == null) continue;
                        if(entry.getValue() == null) continue;
                        if(best == null || entry.getValue() > best.getValue())
                            best = entry;

                        if(worst == null || entry.getValue() < worst.getValue())
                            worst = entry;

                        if(displayAll) {
                            String display = parseValue(entry.getValue());
                            if (display == null) continue;

                            The5zigAPI.getAPI().messagePlayer("§a§3" + entry.getKey() + ": §b" + display);
                        }
                    }
                The5zigAPI.getAPI().messagePlayer(Log.info + "Most played: §b" + best.getKey() + ": §b" + parseValue(best.getValue()));
                The5zigAPI.getAPI().messagePlayer(Log.info + "Least played: §b" + worst.getKey() + ": §b" + parseValue(worst.getValue()));
                The5zigAPI.getAPI().messagePlayer(Log.info + "Next refresh:§b " + new SimpleDateFormat("MMM d, hh:mm a").format(nextReset));


            } catch (MalformedURLException e) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "An error occurred. Refer to console for details.");
                e.printStackTrace();
            }
        }).start();

        return true;
    }

    private String parseValue(Object in) {
        if(in == null) return null;
        double d = (double) in;
        if(d == 0) return "§cNever Played";
        return df.format(d);
    }

    private Map.Entry<String, Double> parseEntry(Map.Entry<String, Object> in) {
        if(in == null) return null;
        if(in.getValue() == null) return null;
        double value = in.getValue() instanceof Double
                ? (double) in.getValue()
                : ((Long)in.getValue()).doubleValue();
        return new AbstractMap.SimpleEntry<>(in.getKey(), value);
    }
}
