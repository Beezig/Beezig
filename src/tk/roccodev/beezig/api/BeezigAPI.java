package tk.roccodev.beezig.api;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.CommandManager;
import tk.roccodev.beezig.api.listener.AbstractForgeListener;
import tk.roccodev.beezig.games.BED;
import tk.roccodev.beezig.games.CAI;
import tk.roccodev.beezig.hiveapi.stuff.RankEnum;
import tk.roccodev.beezig.hiveapi.stuff.bed.BEDRank;
import tk.roccodev.beezig.hiveapi.stuff.timv.TIMVRank;
import tk.roccodev.beezig.settings.Setting;
import tk.roccodev.beezig.settings.SettingsFetcher;
import tk.roccodev.beezig.utils.tutorial.SendTutorial;
import tk.roccodev.beezig.utils.ws.Connector;

public class BeezigAPI {

    private static BeezigAPI instance;
    private AbstractForgeListener listenerImpl;

    public BeezigAPI() {
        instance = this;
    }

    public static BeezigAPI get() {

        if (instance == null) instance = new BeezigAPI();
        return instance;
    }

    public void registerListener(Object toRegister) {
        listenerImpl = AbstractForgeListener.fromObject(toRegister);
        BeezigMain.hasExpansion = true;
        listenerImpl.onLoad(BeezigMain.BEEZIG_VERSION, The5zigAPI.getAPI().getModVersion());
        CommandManager.commandExecutors.forEach(cmd -> listenerImpl.registerCommand(cmd));
        new Thread(() -> Connector.client.send("BeezigForgeLoad")).start();
    }

    public boolean isStaffMember() {
        return BeezigMain.isStaffChat();
    }

    public String getCAITeam() {
        if (!ActiveGame.is("cai")) return "";
        return ChatColor.stripColor(CAI.team);
    }

    public boolean getSettingValue(String setting) {
        try {
            return Setting.valueOf(setting).getValue();
        } catch (Exception e) {
            return false;
        }
    }

    public void sendTutorial(String key) {
        SendTutorial.send(key);
    }

    public String getBedwarsMode() {
        return BED.mode;
    }

    public String getRankString(String title, String mode) {
        String pkg = mode.startsWith("GNT") ? "gnt" : mode.toLowerCase();
        String name = mode.startsWith("GNT") ? "Giant" : mode.toUpperCase();
        try {
            Class clazz = Class.forName("tk.roccodev.beezig.hiveapi.stuff." + pkg + "." + name + "Rank");
            Object o = clazz.getMethod("getFromDisplay", String.class).invoke(null, title);
            if(!(o instanceof RankEnum)) return null;
            RankEnum obj = (RankEnum) o;
            if(obj instanceof BEDRank) {
                return ((BEDRank)obj).rankStringForge(title.replaceAll("\\D+", ""));
            }
            return obj.getTotalDisplay();

        } catch (Exception e) {
           return null;
        }
    }

    public String getTIMVRank(String title, long points) {
        if(title == null) return null;
        return TIMVRank.getFromDisplay(title).getTotalDisplay(points);
    }

    public boolean onPacketReceived(int packetId, String data) {
        return true; // Return false to ignore the packet
    }

    public void saveConfigData(Object data) {
        try {

            Object[] arr = (Object[]) data.getClass().getField("array").get(data);

            for (Object o : arr) {
                String enumName = (String) o.getClass().getField("enumName").get(o);
                boolean enabled = (boolean) o.getClass().getField("enabled").get(o);
                Setting.valueOf(enumName).setValueWithoutSaving(enabled);
            }

            SettingsFetcher.saveSettings();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public AbstractForgeListener getListener() {
        return listenerImpl;
    }


}
