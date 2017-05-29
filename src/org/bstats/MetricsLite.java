package org.bstats;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.yaml.snakeyaml.Yaml;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.plugin.Plugin;
import tk.roccodev.zta.ZTAMain;

public class MetricsLite {

	File homeFolder = new File(System.getProperty("user.home"));
	
	
    static {
        // Maven's Relocate is clever and changes strings, too. So we have to use this little "trick" ... :D
        final String defaultPackage = new String(new byte[] { 'o', 'r', 'g', '.', 'a', 's', 't', 'a', 't', 's' });
        final String examplePackage = new String(new byte[] { 'a', 'r', 'g', '.', 'b', 's', 't', 'a', 't', 's' });
        // We want to make sure nobody just copy & pastes the example and use the wrong package names
        if (MetricsLite.class.getPackage().getName().equals(defaultPackage) || MetricsLite.class.getPackage().getName().equals(examplePackage)) {
            throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
        }
    }

    // The version of this bStats class
    public static final int B_STATS_VERSION = 1;

    // The url to which the data is sent
    private static final String URL = "https://bStats.org/submitData/bukkit";

    // Should failed requests be logged?
    private static boolean logFailedRequests;

    // The uuid of the server
    private static String serverUUID;

    // The plugin
    private final ZTAMain plugin;

    /**
     * Class constructor.
     *
     * @param plugin The plugin which stats should be submitted.
     */
    public MetricsLite(ZTAMain plugin) {
    
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null!");
        }
        this.plugin = plugin;

        // Get the config file
        File bStatsFolder = new File(homeFolder, "bStats");
        if(!bStatsFolder.exists())
			
				bStatsFolder.mkdir();
			
			
        File configFile = new File(bStatsFolder, "config.yml");
        if(!configFile.exists())
			try {
				configFile.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
      
        Yaml yaml = new Yaml();
        FileInputStream is = null;
		try {
			is = new FileInputStream(configFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Map map = (Map) yaml.load(is);
        if(map == null) map = new HashMap<String, Object>();
        if(map.containsKey("uuid") && map.get("uuid") instanceof String){
        	serverUUID = (String) map.get("uuid");
        }
        else{
        	map.put("uuid", UUID.randomUUID().toString());
        }
     	serverUUID = (String) map.get("uuid");
        // Load the data
     	try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	FileWriter fw = null;
     	try {
			fw = new FileWriter(configFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	yaml.dump(map, fw);
     	try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
            
            // Register our service
           
                // We are the first!
     			
                submitData();
            
        
    }

    /**
     * Here there was the scheduler, I removed it because there is no need.
     */
   

    /**
     * Gets the plugin specific data.
     * This method is called using Reflection.
     *
     * @return The plugin specific data.
     */
    public JSONObject getPluginData() {
        JSONObject data = new JSONObject();

        String pluginName = "5zig-TIMV";
        
        String pluginVersion = plugin.getClass().getAnnotation(Plugin.class).version();

        data.put("pluginName", pluginName); // Append the name of the plugin
        data.put("pluginVersion", pluginVersion); // Append the version of the plugin
        JSONArray customCharts = new JSONArray();
        data.put("customCharts", customCharts);

        return data;
    }

    /**
     * Gets the server specific data.
     *
     * @return The server specific data.
     */
    private JSONObject getServerData() {
        // Minecraft specific data
        int playerAmount = 1;
        int onlineMode = 1;
        String bukkitVersion = The5zigAPI.getAPI().getMinecraftVersion();
        

        // OS/Java specific data
        String javaVersion = System.getProperty("java.version");
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");
        int coreCount = Runtime.getRuntime().availableProcessors();

        JSONObject data = new JSONObject();

        data.put("serverUUID", serverUUID);

        data.put("playerAmount", playerAmount);
        data.put("onlineMode", onlineMode);
        data.put("bukkitVersion", bukkitVersion);

        data.put("javaVersion", javaVersion);
        data.put("osName", osName);
        data.put("osArch", osArch);
        data.put("osVersion", osVersion);
        data.put("coreCount", coreCount);

        return data;
    }

    /**
     * Collects the data and sends it afterwards.
     */
    private void submitData() {
        final JSONObject data = getServerData();

        JSONArray pluginData = new JSONArray();
        pluginData.add(getPluginData());
        data.put("plugins", pluginData);

        // Create a new thread for the connection to the bStats server
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Send the data
                	The5zigAPI.getLogger().info("Sending " + data.toJSONString());
                    sendData(data);
                    
                } catch (Exception e) {
                    // Something went wrong! :(
                    if (logFailedRequests) {
                        The5zigAPI.getLogger().warn("Could not submit plugin stats of " + "ZTA", e);
                    }
                }
            }
        }).start();
    }
    
   



    /**
     * Sends the data to the bStats server.
     *
     * @param data The data to send.
     * @throws Exception If the request failed.
     */
    private static void sendData(JSONObject data) throws Exception {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null!");
        }
       
        HttpsURLConnection connection = (HttpsURLConnection) new URL(URL).openConnection();

        // Compress the data to save bandwidth
        byte[] compressedData = compress(data.toString());

        The5zigAPI.getLogger().info("Connecting and sending...");
        
        // Add headers
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Connection", "close");
        connection.addRequestProperty("Content-Encoding", "gzip"); // We gzip our request
        connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
        connection.setRequestProperty("Content-Type", "application/json"); // We send our data in JSON format
        connection.setRequestProperty("User-Agent", "MC-Server/" + B_STATS_VERSION);

        // Send data
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.write(compressedData);
        outputStream.flush();
        The5zigAPI.getLogger().info(connection.getResponseMessage());
        outputStream.close();
        
        connection.getInputStream().close(); // We don't care about the response - Just send our data :)
    }

    /**
     * Gzips the given String.
     *
     * @param str The string to gzip.
     * @return The gzipped String.
     * @throws IOException If the compression failed.
     */
    private static byte[] compress(final String str) throws IOException {
        if (str == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
        gzip.write(str.getBytes("UTF-8"));
        gzip.close();
        return outputStream.toByteArray();
    }

}

