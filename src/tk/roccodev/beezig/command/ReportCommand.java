package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;
import tk.roccodev.beezig.hiveapi.HiveAPI;
import tk.roccodev.beezig.hiveapi.wrapper.NetworkRank;
import tk.roccodev.beezig.hiveapi.wrapper.modes.ApiHiveGlobal;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ReportCommand implements Command {

    private long lastOne;
    private boolean shouldConfirm = false;
    public String Hive = "§8§l| §a§lBeezig§c§lReport §8| §r";

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "report";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/report"};
    }


    @Override
    public boolean execute(String[] args) {

        if (!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;
        if (args.length < 2) {
            The5zigAPI.getAPI().messagePlayer(Hive + "Usage: /report [player] [reason]");
            return true;
        }
        if (lastOne != 0 && !shouldConfirm && (System.currentTimeMillis() - lastOne < 30000)) {

            The5zigAPI.getAPI().messagePlayer(Log.error + "You must wait 30 seconds between reports!");
            return true;
        }
        lastOne = System.currentTimeMillis();
        // RoccoDev, ItsNiklass AntiKnockback, Kill Aura
        The5zigAPI.getAPI().messagePlayer(Log.info + "Checking...");
        new Thread(() -> {
            String rawArgs = String.join(" ", args);
            String[] rawPlayers = rawArgs.replaceAll(", ", ",").split(" ");
            String players = rawPlayers[0];
            ArrayList<String> argsL = new ArrayList<>(Arrays.asList(rawPlayers));
            argsL.remove(0);
            String reason = String.join(" ", argsL);

            if(reason.contains("swear") || reason.contains("spam") || reason.contains("racis") || reason.contains("idiot")) {
                The5zigAPI.getAPI().messagePlayer(Hive + "This is not the proper way to report chat offences. Please use /chatreport instead.");
                return;
            }
            for (String s : players.split(",")) {
                ApiHiveGlobal api = new ApiHiveGlobal(s);
                // Trigger an error if needed
                api.getCorrectName();
                if (api.getError() != null) {
                    The5zigAPI.getAPI().messagePlayer(Hive + "Player §4" + s + "§c does not exist.");
                    return;
                }
                if (!api.isOnline() && !shouldConfirm) {
                    The5zigAPI.getAPI().messagePlayer(Hive + "Player §b" + s + "§3 is not online. Please run the command again to confirm the report.");
                    shouldConfirm = true;
                    return;
                } else {
                    shouldConfirm = false;
                }

            }

            try {
                URL url = new URL("http://botzig-atactest.7e14.starter-us-west-2.openshiftapps.com/report");
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection) con;
                http.setRequestMethod("POST"); // PUT is another valid option
                http.setDoOutput(true);
                Map<String, String> arguments = new HashMap<>();
                arguments.put("sender", The5zigAPI.getAPI().getGameProfile().getName());
                arguments.put("destination", players);
                arguments.put("reason", reason);
                StringJoiner sj = new StringJoiner("&");
                for (Map.Entry<String, String> entry : arguments.entrySet())
                    sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                            + URLEncoder.encode(entry.getValue(), "UTF-8"));
                byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
                int length = out.length;
                http.setFixedLengthStreamingMode(length);
                http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                http.setRequestProperty("User-Agent", Log.getUserAgent());
                http.connect();
                try (OutputStream os = http.getOutputStream()) {
                    os.write(out);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ApiHiveGlobal api = new ApiHiveGlobal(args[0]);
            String ign = api.getCorrectName();
            String networkRank = api.getNetworkTitle();
            ChatColor rankColor = api.getNetworkRankColor();

            The5zigAPI.getAPI().messagePlayer(Hive + ChatColor.YELLOW + "User: " + rankColor + players + ChatColor.YELLOW + " For: " + ChatColor.RED + reason);
            The5zigAPI.getAPI().messagePlayer(Hive + ChatColor.YELLOW + "Server: " + ChatColor.RED + api.getPlayerLocation());


        }).start();


        return true;
    }


}
