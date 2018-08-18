package tk.roccodev.beezig.api;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.util.minecraft.ChatColor;
import tk.roccodev.beezig.ActiveGame;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.api.listener.AbstractForgeListener;
import tk.roccodev.beezig.games.CAI;
import tk.roccodev.beezig.settings.Setting;
import tk.roccodev.beezig.settings.SettingsFetcher;
import tk.roccodev.beezig.utils.ws.Connector;

public class BeezigAPI {

    private Object registeredListener;
    private AbstractForgeListener listenerImpl;
    private static BeezigAPI instance;

    public void registerListener(Object toRegister) {
        registeredListener = toRegister;
        listenerImpl = AbstractForgeListener.fromObject(registeredListener);
        BeezigMain.hasExpansion = true;
        listenerImpl.onLoad(BeezigMain.BEEZIG_VERSION, The5zigAPI.getAPI().getModVersion());
        new Thread(() -> {
            Connector.client.send("BeezigForgeLoad");
        }).start();
    }


    public static BeezigAPI get() {

        if(instance == null) instance = new BeezigAPI();
        return instance;
    }

    public boolean isStaffMember() {
        return BeezigMain.isStaffChat();
    }

    public String getCAITeam() {
        if(!ActiveGame.is("cai")) return "";
        return ChatColor.stripColor(CAI.team);
    }

    public boolean getSettingValue(String setting) {
        try {
            return Setting.valueOf(setting).getValue();
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean onPacketReceived(int packetId, String data) {
        return true; // Return false to ignore the packet
    }

    public void saveConfigData(Object data) {
        try {

            Object[] arr = (Object[]) data.getClass().getField("array").get(data);

            for(Object o : arr) {
                String enumName = (String) o.getClass().getField("enumName").get(o);
                boolean enabled = (boolean) o.getClass().getField("enabled").get(o);
                Setting.valueOf(enumName).setValueWithoutSaving(enabled);
            }

            SettingsFetcher.saveSettings();

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public AbstractForgeListener getListener() {
        return listenerImpl;
    }

    public BeezigAPI() {
        instance = this;
    }






}
