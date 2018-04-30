package tk.roccodev.zta.settings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.ZTAMain;

public class SettingsFetcher {

	public static Properties props;

	public static void loadSettings() throws IOException{
		props = new Properties();
		FileInputStream fis = new FileInputStream(ZTAMain.mcFile.getAbsolutePath() + "/settings.properties");
		props.load(fis);
		fis.close();
		for(Object os : props.keySet()){
			
			if(os instanceof String){
				String s = (String) os;
				The5zigAPI.getLogger().info("Loading " + s + ":" + props.getProperty(s));
				try{
					Setting sett = Setting.valueOf(s.toUpperCase());
					
						sett.setValueWithoutSaving(Boolean.valueOf(props.getProperty(s)));
					
					
				}
				catch(IllegalArgumentException e){
				}
			}
		}
	}
	
	public static void saveSetting(Setting sett) throws IOException{
		The5zigAPI.getLogger().info("Saving...");
		if(props == null) props = new Properties();
		props.setProperty(sett.name().toUpperCase(), sett.getValue() + "");
		FileOutputStream fos = new FileOutputStream(ZTAMain.mcFile.getAbsolutePath() + "/settings.properties");
		props.store(fos, "");
		
	}
	
	public static void saveSettings() throws IOException{
		for(Setting s : Setting.values()){
			saveSetting(s);
		}
	}
	

}
