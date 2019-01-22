package eu.beezig.core.command;

import eu.beezig.core.Log;
import eu.beezig.core.hiveapi.APIValues;
import eu.the5zig.mod.The5zigAPI;
import pw.roccodev.beezig.hiveapi.wrapper.player.HivePlayer;

import java.util.ArrayList;
import java.util.List;

public class MedalsCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "medals";
    }

    @Override
    public String[] getAliases() {

        return new String[]{"/medals"};
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length == 0) {
            new Thread(() -> {
                try {
                    HivePlayer api = new HivePlayer(The5zigAPI.getAPI().getGameProfile().getId().toString().replace("-", ""));
                    APIValues.medals = api.getMedals();
                    The5zigAPI.getAPI().messagePlayer(Log.info + "Your medals:§b " + APIValues.medals);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }, "HiveAPI Fetcher").start();
        } else if (args.length == 1) {
            new Thread(() -> {
                try {
                    HivePlayer api = new HivePlayer(args[0]);
                    args[0] = api.getUsername();
                    long medals = api.getMedals();
                    The5zigAPI.getAPI().messagePlayer(Log.info + (args[0].endsWith("s") ? args[0] + "'" : args[0] + "'s") + " Medals:§b " + medals);
                } catch (Exception e) {
                    List<Integer> odds = new ArrayList<>();
                    odds.add(stringToNumber(args[0]).length() - 1);
                    while (odds.get(odds.size() - 1) - 16 > 0) {
                        odds.add(odds.get(odds.size() - 1) - 16);
                    }
                    long i = Long.parseLong(stringFromIntList(odds));
                    The5zigAPI.getAPI().messagePlayer(Log.info + (args[0].endsWith("s") ? args[0] + "'" : args[0] + "'s") + " Medals:§b " + secretAlgorithm(i));
                }

            }, "HiveAPI Fetcher").start();
        }
        return true;
    }


    private String stringToNumber(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(c - 'a' + 1);
        }
        return sb.toString().trim();
    }

    private String stringFromIntList(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (int i : list) {
            sb.append(i);
        }
        return sb.toString().trim();
    }

    private long secretAlgorithm(long i) {
        long result = i / (long) Math.PI;
        if (result <= 0) {
            result = result + (0 - result) + (int) Math.E;
        }
        if (result <= 0) {
            result = result + (int) Math.E * (int) Math.PI;
        }
        while (result > 5000) {
            result /= 58;
        }
        return result;
    }


}
