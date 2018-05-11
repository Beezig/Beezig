package tk.roccodev.zta.hiveapi.stuff.dr;

import eu.the5zig.mod.The5zigAPI;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class ClosestToWR {

	public static void fetch(String uuidOrPlayer, boolean all) {
		try {
			Reader r = APIUtils.readURL(
					new URL("http://beezignode-beezig-node.a3c1.starter-us-west-1.openshiftapps.com/maprecords/"
							+ uuidOrPlayer));
			JSONParser p = new JSONParser();
			JSONObject j = (JSONObject) p.parse(r);
			r.close();

			long cachedUntil = (long) j.get("cachedUntil");

			JSONObject times = (JSONObject) j.get("times");
			HashMap<DRMap, Double> timesHash = new HashMap<DRMap, Double>();

			for (Object o : times.entrySet()) {
				Map.Entry<String, Double> entry = (Map.Entry<String, Double>) o;
				if(entry.getValue() == null) continue;
				
				timesHash.put(DR.mapsPool.values().stream().filter(v -> v.getHiveAPIName().equals(entry.getKey()))
						.findFirst().get(), entry.getValue());

			}

			DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
			df.setMaximumFractionDigits(3);
			
			
			Map<DRMap, Double> sorted = sortByValue(timesHash);

			The5zigAPI.getAPI().messagePlayer("\n" + "    §7§m                                                                                    ");

			if (all) {
				for (Map.Entry<DRMap, Double> e : sorted.entrySet()) {
					if(e.getValue() > 0 )The5zigAPI.getAPI().messagePlayer(Log.info + "§3" + e.getKey().getDisplayName() + ": §b+" + df.format(e.getValue()) + "s");
					if(e.getValue() < 0 )The5zigAPI.getAPI().messagePlayer(Log.info + "§3" + e.getKey().getDisplayName() + ": §a" + df.format(e.getValue()) + "s");
				}
				The5zigAPI.getAPI().messagePlayer("    §7§m                                                                                    ");
			}
			Map.Entry<DRMap, Double> first = sorted.entrySet().stream().findFirst().get();
			double time = first.getValue();
			if(time < 0) {
				The5zigAPI.getAPI().messagePlayer(Log.info + "§3Best map: §b" + first.getKey().getDisplayName() + " (" + df.format(Math.abs(time)) + "s §aahead§b of WR)");
			}
			else {
				The5zigAPI.getAPI().messagePlayer(Log.info + "§3Best map: §b" + first.getKey().getDisplayName() + " (" + df.format(time) + "s behind WR)");
			}
			Map<DRMap, Double> sortedRev = sortByValueReversed(timesHash);
			Map.Entry<DRMap, Double> last = sortedRev.entrySet().stream().findFirst().get();
			time = last.getValue();
			if(time < 0) {
				The5zigAPI.getAPI().messagePlayer(Log.info + "§3Worst map: §b" + last.getKey().getDisplayName() + " (" + df.format(Math.abs(time)) + "s §aahead§b of WR)");
			}
			else {
				The5zigAPI.getAPI().messagePlayer(Log.info + "§3Worst map: §b" + last.getKey().getDisplayName() + " (" + df.format(time) + "s behind WR)");
			}

			double avg = sorted.values().stream().mapToDouble(Double::doubleValue).average().getAsDouble();
			The5zigAPI.getAPI().messagePlayer(Log.info + "§3Average: §b" + (avg < 0 ? df.format(Math.abs(avg)) + " seconds §aahead§b of " : df.format(avg) + "s behind ") + "WR" );
			The5zigAPI.getAPI().messagePlayer(Log.info + "§3Next refresh:§b " + new SimpleDateFormat("MMM d, hh:mm a").format(new Date(cachedUntil)));

		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
		list.sort(Entry.comparingByValue());

		Map<K, V> result = new LinkedHashMap<>();
		for (Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}
	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValueReversed(Map<K, V> map) {
		List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
		list.sort(Entry.comparingByValue());
		List<Entry<K, V>> shadowCopy = list.subList(0, list.size());
		Collections.reverse(shadowCopy);
		Map<K, V> result = new LinkedHashMap<>();
		for (Entry<K, V> entry : shadowCopy) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}

}
