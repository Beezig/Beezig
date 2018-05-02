package tk.roccodev.zta.hiveapi.stuff.dr;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.DoubleStream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.Log;
import tk.roccodev.zta.games.DR;
import tk.roccodev.zta.hiveapi.wrapper.APIUtils;

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
			
			if (all) {
				for (Map.Entry<DRMap, Double> e : sorted.entrySet()) {
					The5zigAPI.getAPI().messagePlayer("§3" + e.getKey().getDisplayName() + ": §b" + df.format(e.getValue()));
				}
			}
			Map.Entry<DRMap, Double> first = sorted.entrySet().stream().findFirst().get();
			double time = first.getValue();
			if(time < 0) {
				The5zigAPI.getAPI().messagePlayer(Log.info + "§3Best map: §b" + first.getKey().getDisplayName() + " (" + df.format(Math.abs(time)) + " seconds §aahead§b of WR)");
			}
			else {
				The5zigAPI.getAPI().messagePlayer(Log.info + "§3Best map: §b" + first.getKey().getDisplayName() + " (" + df.format(time) + " seconds §cbehind§b WR)");
			}
			double avg = sorted.values().stream().mapToDouble(Double::doubleValue).average().getAsDouble();
			The5zigAPI.getAPI().messagePlayer(Log.info + "§3Average: " + (avg < 0 ? df.format(avg) + " seconds §aahead§b of " : df.format(avg) + " seconds §cbehind§b ") + "WR" );
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

}
