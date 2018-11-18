package tk.roccodev.beezig.listener.arcade.subs;

import tk.roccodev.beezig.games.Arcade;
import tk.roccodev.beezig.listener.arcade.ArcadeSubListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpleggSubListener extends ArcadeSubListener {

    @Override
    public void onServerChat(Arcade gameMode, String message) {
        if(message.startsWith("§8▍ §bSplegg§8 ▏ §3Voting has ended! §bThe map §f")) {
            String map = "";
            String afterMsg = message.split("§8▍ §bSplegg§8 ▏ §3Voting has ended! §bThe map")[1];
            Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
            Matcher matcher = pattern.matcher(afterMsg);
            while (matcher.find()) {
                map = matcher.group(1);
            }
            gameMode.map = map;

        }
    }
}
