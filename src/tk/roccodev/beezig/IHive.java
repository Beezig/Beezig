package tk.roccodev.beezig;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.GameListenerRegistry;
import eu.the5zig.mod.server.ServerInstance;
import tk.roccodev.beezig.listener.*;
import tk.roccodev.beezig.notes.NotesManager;
import tk.roccodev.beezig.utils.autogg.Trigger;
import tk.roccodev.beezig.utils.autogg.Triggers;
import tk.roccodev.beezig.utils.autogg.TriggersFetcher;
import tk.roccodev.beezig.utils.rpc.DiscordUtils;

import javax.xml.bind.DatatypeConverter;
import java.util.Calendar;

public class IHive extends ServerInstance {

    static GameListenerRegistry gameListener;
    public static long joined;


    public static void genericReset(String... optionalParams) {
        DiscordUtils.updatePresence("Relaxing in the Hub", "In Lobby", "lobby");
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
            new Thread(() -> {
                if(TriggersFetcher.shouldLoad()) {
                    System.out.println("Loading AutoGG because the AutoGG Mod was not found or is not enabled.");
                    Triggers.enabled = true;
                    for (Trigger trigger : Triggers.triggers) {
                        System.out.println(trigger.getTrigger() + " / " + trigger.isEnabled());
                    }
                }
                else System.out.println("Didn't load AutoGG because the AutoGG Mod was found and is enabled.");
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
        // TODO Auto-generated method stub
        getGameListener().registerListener(new TIMVListener());
        getGameListener().registerListener(new HiveListener());
        getGameListener().registerListener(new DRListener());
        getGameListener().registerListener(new BEDListener());
        getGameListener().registerListener(new GiantListener());
        getGameListener().registerListener(new HIDEListener());
        getGameListener().registerListener(new CAIListener());
        getGameListener().registerListener(new SKYListener());
        getGameListener().registerListener(new MIMVListener());
        getGameListener().registerListener(new GRAVListener());
        getGameListener().registerListener(new BPListener());
        getGameListener().registerListener(new SGNListener());
        getGameListener().registerListener(new LABListener());

    }

}
