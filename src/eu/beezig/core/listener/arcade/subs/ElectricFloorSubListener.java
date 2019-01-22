package eu.beezig.core.listener.arcade.subs;

import eu.beezig.core.listener.arcade.ArcadeSubListener;
import eu.beezig.core.games.Arcade;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElectricFloorSubListener extends ArcadeSubListener {

    @Override
    public void onServerChat(Arcade gameMode, String message) {
        if(message.startsWith("§8▍ §3§lElectric§b§lFloor§8 ▏ §3Voting has ended! §bThe map §f")) {
            String map = "";
            String afterMsg = message.split("§8▍ §3§lElectric§b§lFloor§8 ▏ §3Voting has ended! §bThe map")[1];
            Pattern pattern = Pattern.compile(Pattern.quote("§f") + "(.*?)" + Pattern.quote("§b"));
            Matcher matcher = pattern.matcher(afterMsg);
            while (matcher.find()) {
                map = matcher.group(1);
            }
            gameMode.map = map;
        }
    }
}
