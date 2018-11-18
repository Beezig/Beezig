package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;
import tk.roccodev.beezig.IHive;
import tk.roccodev.beezig.Log;

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
            The5zigAPI.getAPI().messagePlayer(Log.info + "Usage: /report [player] [reason]");
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

            if (reason.contains("swear") || reason.contains("spam") || reason.contains("racis")) {
                The5zigAPI.getAPI().messagePlayer(Log.info + "This is not the proper way to report chat offences. Please use /chatreport instead.");
                return;
            }
            for (String s : players.split(",")) {

               HivePlayer api = new HivePlayer(s, true);

                try {
                    api.getUsername();  // Trigger an error if needed
                } catch(Exception e) {
                    The5zigAPI.getAPI().messagePlayer(Log.error + "Player §4" + s + "§c does not exist.");
                    return;
                }
                if (!api.getStatus().isOnline() && !shouldConfirm) {
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Player §b" + s + "§3 is not online. Please run the command again to confirm the report.");
                    shouldConfirm = true;
                    return;
                } else {
                    shouldConfirm = false;
                }
            }


            try {
                URL url = new URL("https://app-beezigreportserver.wedeploy.io/report");
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
            The5zigAPI.getAPI().messagePlayer(Log.info + "Succesfully submitted report. Please wait for a moderator to take action.");

        }).start();


        return true;
    }


}
