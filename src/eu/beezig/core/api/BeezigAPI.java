/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
 *
 * This file is part of Beezig.
 *
 * Beezig is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beezig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beezig.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.beezig.core.api;

import eu.beezig.core.ActiveGame;
import eu.beezig.core.BeezigMain;
import eu.beezig.core.CommandManager;
import eu.beezig.core.IHive;
import eu.beezig.core.api.listener.AbstractForgeListener;
import eu.beezig.core.games.BED;
import eu.beezig.core.games.TIMV;
import eu.beezig.core.hiveapi.stuff.RankEnum;
import eu.beezig.core.hiveapi.stuff.bed.BEDRank;
import eu.beezig.core.hiveapi.stuff.timv.TIMVRank;
import eu.beezig.core.settings.Setting;
import eu.beezig.core.settings.SettingsFetcher;
import eu.beezig.core.utils.TIMVTest;
import eu.beezig.core.utils.tutorial.SendTutorial;
import eu.beezig.core.utils.ws.Connector;
import eu.beezig.core.utils.ws.api.PacketOpcodes;
import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

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

        JSONObject packet = new JSONObject();
        packet.put("opcode", PacketOpcodes.S_BEEZIGFORGE_LOADED);
        new Thread(() -> Connector.client.sendJson(packet)).start();
    }

    public boolean isStaffMember() {
        return BeezigMain.isStaffChat();
    }

    public boolean getSettingValue(String setting) {
        try {
            return Setting.valueOf(setting).getValue();
        } catch (Exception e) {
            return false;
        }
    }

    public String getConfigPath() {
        return BeezigMain.mcFile.getAbsolutePath();
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
            Class clazz = Class.forName("eu.beezig.core.hiveapi.stuff." + pkg + "." + name + "Rank");
            Object o = clazz.getMethod("getFromDisplay", String.class).invoke(null, title);
            if (!(o instanceof RankEnum)) return null;
            RankEnum obj = (RankEnum) o;
            if (obj instanceof BEDRank) {
                return ((BEDRank) obj).rankStringForge(title.replaceAll("\\D+", ""));
            }
            return obj.getTotalDisplay();

        } catch (Exception e) {
            return null;
        }
    }

    public String getTIMVRank(String title, long points) {
        if (title == null) return null;
        return TIMVRank.getFromDisplay(title).getTotalDisplay(points);
    }

    public boolean isHive() {
        return The5zigAPI.getAPI().getActiveServer() instanceof IHive;
    }

    public List<String> getTIMVMessages() {
        return TIMV.testRequests;
    }

    public void setTIMVMessages(List<String> in) {
        TIMV.testRequests = in;
        try {
            TIMVTest.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSetting(String setting, boolean value) {
        Setting.valueOf(setting).setValue(value);
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

    public Class<AutovoteAPI> getAutovoter() {
        return AutovoteAPI.class;
    }

    public AbstractForgeListener getListener() {
        return listenerImpl;
    }


}
