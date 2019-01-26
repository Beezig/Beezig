package eu.beezig.core;

import eu.beezig.core.advancedrecords.AdvancedRecords;
import eu.beezig.core.listener.*;
import eu.beezig.core.notes.NotesManager;
import eu.beezig.core.utils.autogg.Triggers;
import eu.beezig.core.utils.autogg.TriggersFetcher;
import eu.beezig.core.utils.rpc.DiscordUtils;
import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameListenerRegistry;
import eu.the5zig.mod.server.ServerInstance;

import javax.xml.bind.DatatypeConverter;
import java.util.Calendar;

public class IHive extends ServerInstance {

    public static long joined;
    static GameListenerRegistry gameListener;

    public static void genericReset(String... optionalParams) {
        DiscordUtils.updatePresence("Relaxing in the Hub", "In Lobby", "lobby");
        AdvancedRecords.isRunning = false;
        AdvancedRecords.player = null;
    }

    public static void genericJoin(String... optionalParams) {
        if (NotesManager.HR1cm5z) {
            The5zigAPI.getAPI().messagePlayer(
                    new String(DatatypeConverter.parseBase64Binary("V293ISBJdHNOaWtsYXNzIHRvZGF5IHR1cm5zIA=="))
                            + (Calendar.getInstance().get(Calendar.YEAR) - 0x7D0) + new String(DatatypeConverter
                            .parseBase64Binary("ISBNYWtlIHN1cmUgdG8gd2lzaCBoaW0gYSBoYXBweSBiaXJ0aGRheSE=")));
        }
    }

    @Override
    public String getName() {
        return "HiveMC";
    }

    @Override
    public String getConfigName() {
        return "hive";
    }

    @Override
    public boolean handleServer(String host, int port) {
        if (host.toLowerCase().contains("hivemc.") || host.toLowerCase().endsWith("hive.sexy")) {
            System.out.println("Joined Hive.");
            joined = System.currentTimeMillis();
            if (BeezigMain.disabled) {
                Log.addToSendQueue("                         §4§m                              ");
                Log.addToSendQueue("");
                Log.addToSendQueue("                       §4§l!! UPDATE REQUIRED !!");
                Log.addToSendQueue("");
                Log.addToSendQueue("       §cA new Beezig update is required and the current");
                Log.addToSendQueue("                     §cversion has been disabled.");
                Log.addToSendQueue("");
                Log.addToSendQueue("                     §7To update, use the installer.");
                Log.addToSendQueue("");
                Log.addToSendQueue("                         §4§m                              ");
            } else if (BeezigMain.newUpdate) {
                Log.addToSendQueue("                         §b§m                              ");
                Log.addToSendQueue("");
                Log.addToSendQueue("                       §b§l!! UPDATE AVAILABLE !!");
                Log.addToSendQueue("");
                Log.addToSendQueue("              §bA new Beezig update is available!");
                Log.addToSendQueue("");
                Log.addToSendQueue("                     §7To update, use the installer.");
                Log.addToSendQueue("");
                Log.addToSendQueue("                         §b§m                              ");

            }
            new Thread(() -> {
                if (TriggersFetcher.shouldLoad()) {
                    System.out.println("Loading AutoGG because the AutoGG Mod was not found or is not enabled.");
                    Triggers.enabled = true;
                } else System.out.println("Didn't load AutoGG because the AutoGG Mod was found and is enabled.");
            }).start();
            try {
                DiscordUtils.init();
            } catch (Throwable e) {
                DiscordUtils.shouldOperate = false;
            } finally {
                DiscordUtils.updatePresence("Relaxing in the Hub", "In Lobby", "lobby");
            }
            return true;
        }
        return false;
    }

    @Override
    public void registerListeners() {
        gameListener = getGameListener();

        gameListener.registerListener(new TIMVListener());
        gameListener.registerListener(new HiveListener());
        gameListener.registerListener(new DRListener());
        gameListener.registerListener(new BEDListener());
        gameListener.registerListener(new GiantListener());
        gameListener.registerListener(new HIDEListener());
        gameListener.registerListener(new CAIListener());
        gameListener.registerListener(new SKYListener());
        gameListener.registerListener(new MIMVListener());
        gameListener.registerListener(new GRAVListener());
        gameListener.registerListener(new BPListener());
        gameListener.registerListener(new SGNListener());
        gameListener.registerListener(new LABListener());
        gameListener.registerListener(new ArcadeListener());

    }

}
