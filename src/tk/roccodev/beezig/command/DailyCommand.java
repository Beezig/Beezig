package tk.roccodev.beezig.command;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DailyCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "daily";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/daily"};
    }


    @Override
    public boolean execute(String[] args) {


        String mode = args.length > 0 ? args[0] : ActiveGame.current();

        new Thread(() -> {
            try {
                Class clazz = Class.forName("tk.roccodev.beezig.games." + mode.toUpperCase());
                Field f = clazz.getSimpleName().equals("TIMV") ? clazz.getDeclaredField("dailyKarmaName") : clazz.getDeclaredField("dailyPointsName");
                f.setAccessible(true);
                ArrayList<String> lines = new ArrayList<>(
                        new ArrayList<>(Files.readAllLines(Paths.get(new File(BeezigMain.mcFile + "/" + mode.toLowerCase() + "/dailyPoints/" + f.get(null)).getPath()))));
                The5zigAPI.getAPI().messagePlayer(Log.info + "Daily Points: §b" + lines.get(0));
            } catch (IOException | ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "No daily data found.");
                e.printStackTrace();
            }
        }).start();


        return true;
    }


}
