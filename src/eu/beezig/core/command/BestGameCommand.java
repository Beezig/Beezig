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

package eu.beezig.core.command;

import eu.beezig.core.IHive;
import eu.beezig.core.Log;
import eu.beezig.core.hiveapi.wrapper.APIUtils;
import eu.beezig.core.utils.URLs;
import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;
import pw.roccodev.beezig.hiveapi.wrapper.mojang.UsernameToUuid;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map;

public class BestGameCommand implements Command {

    private DecimalFormat df = new DecimalFormat();

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "bestgame";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/bestgame", "/bg", "/maingame"};
    }

    @Override
    public boolean execute(String[] args) {

        if (!(The5zigAPI.getAPI().getActiveServer() instanceof IHive)) return false;


        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);


        new Thread(() -> {
            String player = args.length == 0
                    ? The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", "")
                    : UsernameToUuid.getUUID(args[0]);

            if (player == null) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "Player not found.");
                return;
            }

            boolean displayAll = args.length > 1;
            The5zigAPI.getAPI().messagePlayer(Log.info + "Calculating, this will take a long time...");
            try {
                JSONObject data = APIUtils.getObject(APIUtils.readURL(new URL(URLs.MAIN_URL + "/bestgame/" + player)));
                JSONObject modes = (JSONObject) data.get("data");
                Date nextReset = new Date((long) data.get("cache"));

                Map.Entry<String, Double> best = null;
                Map.Entry<String, Double> worst = null;

                for (Object o : modes.entrySet()) {
                    Map.Entry<String, Double> entry = parseEntry((Map.Entry<String, Object>) o);
                    if (entry == null) continue;
                    if (entry.getValue() == null) continue;
                    if (best == null || entry.getValue() > best.getValue())
                        best = entry;

                    if (worst == null || entry.getValue() < worst.getValue())
                        worst = entry;

                    if (displayAll) {
                        String display = parseValue(entry.getValue());
                        if (display == null) continue;

                        The5zigAPI.getAPI().messagePlayer(Log.info + "§a§3" + entry.getKey() + ": §b" + display);
                    }
                }
                The5zigAPI.getAPI().messagePlayer(Log.info + "Most played: §b" + best.getKey() + ": §b" + parseValue(best.getValue()));
                The5zigAPI.getAPI().messagePlayer(Log.info + "Least played: §b" + worst.getKey() + ": §b" + parseValue(worst.getValue()));
                The5zigAPI.getAPI().messagePlayer(Log.info + "Next refresh:§b " + new SimpleDateFormat("MMM d, hh:mm a").format(nextReset));


            } catch (MalformedURLException e) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "An error occurred. Refer to console for details.");
                e.printStackTrace();
            }
        }).start();

        return true;
    }

    private String parseValue(Object in) {
        if (in == null) return null;
        double d = (double) in;
        if (d == 0) return "§8Never Played";
        if (d < 100) {
            double consider = 100 - d;
            String color;

            if (consider > 90) color = "§7";
            else if (consider > 80) color = "§4";
            else if (consider > 70) color = "§c";
            else if (consider > 60) color = "§6";
            else if (consider > 30) color = "§e";
            else color = "§a";

            return color + df.format(100 - d) + "% worse";
        } else return "§2" + df.format(d - 100) + "% better";

    }

    private Map.Entry<String, Double> parseEntry(Map.Entry<String, Object> in) {
        if (in == null) return null;
        if (in.getValue() == null) return null;
        double value = ((Number) in.getValue()).doubleValue() * 100;
        return new AbstractMap.SimpleEntry<>(in.getKey(), value);
    }
}
