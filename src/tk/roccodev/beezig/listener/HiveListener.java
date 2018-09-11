package tk.roccodev.beezig.listener;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.games.GNT;
import tk.roccodev.beezig.games.GNTM;
import tk.roccodev.beezig.games.TIMV;
import tk.roccodev.beezig.hiveapi.HiveAPI;
import tk.roccodev.beezig.utils.rpc.DiscordUtils;

public class HiveListener extends AbstractGameListener<GameMode> {


    @Override
    public Class<GameMode> getGameMode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean matchLobby(String lobby) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onGameModeJoin(GameMode gameMode) {


    }


    @Override
    public boolean onServerChat(GameMode gameMode, String message) {
        if (message.contains("§eGold Medal Awarded!")) {
            HiveAPI.medals++;
        } else if (ChatColor.stripColor(message).contains("▍ Tokens ▏ You earned")) {

            String[] data = ChatColor.stripColor(message).replaceAll("▍ Tokens ▏ You earned", "").split("tokens");
            int tokens = Integer.parseInt(data[0].trim());

            HiveAPI.tokens += tokens;

        } else if (message.contains("§b EXTRA tokens this round!")) {
            //§8▍ §3§lBed§b§lWars§8 ▏ §bThanks to the §dultimate§b member §dGryffin§b you gained §a25§b EXTRA tokens this round!
            //idk if thats how it works
            HiveAPI.tokens += Integer.parseInt(ChatColor.stripColor(message.split("EXTRA")[0].split("you gained ")[1].trim()));
        }
        return false;
    }

    @Override
    public void onMatch(GameMode gameMode, String key, IPatternResult match) {
        System.out.println(gameMode == null);
        if (gameMode != null && gameMode.getState() != GameState.FINISHED) {
            return;
        }
        if (key != null)
            System.out.println(key);
        if (key.equals(TIMV.joinMessage)) {
            getGameListener().switchLobby("TIMV");

            The5zigAPI.getLogger().info("Connected to TIMV! -Hive");
            DiscordUtils.updatePresence("Investigating in Trouble in Mineville", "In Lobby", "game_timv");
        }

        if (key.equals("timv.welcome")) {
            getGameListener().switchLobby("TIMV");

            The5zigAPI.getLogger().info("Connected to TIMV! -Hive");
            DiscordUtils.updatePresence("Investigating in Trouble in Mineville", "In Lobby", "game_timv");
        } else if (key.equals("dr.welcome")) {
            getGameListener().switchLobby("DR");

            The5zigAPI.getLogger().info("Connected to DR! -Hive");
            DiscordUtils.updatePresence("Parkouring in DeathRun", "In Lobby", "game_dr");
        } else if (key.equals("bed.welcome") || (key.equals("bed.spectator") || key.equals("bed.fallback") && gameMode == null)) {
            getGameListener().switchLobby("BED");

            The5zigAPI.getLogger().info("Connected to BED/BEDT! -Hive");
            DiscordUtils.updatePresence("Housekeeping in BedWars", "In Lobby", "game_bedwars");
        } else if (key.equals("gntm.welcome")) {


            The5zigAPI.getLogger().info("Connected to GNTM! -Hive");
            DiscordUtils.updatePresence("Slaying SkyGiants:Mini", "In Lobby", "game_giant");


            GiantListener.listener.setGameMode(GNTM.class, GNTM.instance);
            The5zigAPI.getLogger().info(GNTM.instance.getClass());
            getGameListener().switchLobby("GNTM");
        } else if (key.equals("gnt.welcome")) {


            The5zigAPI.getLogger().info("Connected to GNT! -Hive");
            DiscordUtils.updatePresence("Slaying SkyGiants", "In Lobby", "game_giant");
            The5zigAPI.getLogger().info(GNT.instance.getClass());

            GiantListener.listener.setGameMode(GNT.class, GNT.instance);

            getGameListener().switchLobby("GNT");
        } else if (key.equals("hide.welcome")) {
            getGameListener().switchLobby("HIDE");
            The5zigAPI.getLogger().info("Connected to HIDE! -Hive");
            DiscordUtils.updatePresence("Playing Hide & Seek", "In Lobby", "game_hide");
        } else if (key.equals("cai.welcome")) {
            getGameListener().switchLobby("CAI");
            The5zigAPI.getLogger().info("Connected to CAI! -Hive");
            DiscordUtils.updatePresence("Battling in Cowboys and Indians", "In Lobby", "game_cai");
        } else if (key.equals("sky.welcome")) {
            getGameListener().switchLobby("SKY");
            The5zigAPI.getLogger().info("Connected to SKY! - Hive");
            DiscordUtils.updatePresence("Fighting in SkyWars", "In Lobby", "game_skywars");

        } else if (key.equals("mimv.welcome")) {
            getGameListener().switchLobby("MIMV");
            The5zigAPI.getLogger().info("Connected to MIMV! - Hive");
            DiscordUtils.updatePresence("Investigating in Murder in Mineville", "In Lobby", "game_mimv");

        } else if (key.equals("grav.welcome")) {
            getGameListener().switchLobby("GRAV");
            The5zigAPI.getLogger().info("Connected to GRAV! - Hive");
            DiscordUtils.updatePresence("Freefalling in Gravity", "In Lobby", "game_grav");

        } else if (key.equals("bp.welcome")) {
            getGameListener().switchLobby("BP");
            The5zigAPI.getLogger().info("Connected to BP! - Hive");
            DiscordUtils.updatePresence("Dancing in BlockParty", "Startup", "game_bp");

        } else if (key.equals("sgn.welcome")) {
            getGameListener().switchLobby("SGN");
            The5zigAPI.getLogger().info("Connected to SGN! - Hive");
            DiscordUtils.updatePresence("Battling in SG2", "In Lobby", "game_sgn");
        }

    }


}
