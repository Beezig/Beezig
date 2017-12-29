package tk.roccodev.zta.autovote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

import tk.roccodev.zta.ZTAMain;

public class AutovoteUtils {

	
	private static Yaml yml = new Yaml();
	private static HashMap<String, Object> ymlObject;
	
	
	public static List<String> getMapsForMode(String mode){
		List<String> tr = new ArrayList<String>();
		if(get(mode) == null)
			return tr;
		
		tr.addAll(((List<String>)get(mode)).stream().map(s -> s.replaceAll("\\{c\\}", ":")).collect(Collectors.toList()));
		return tr;
	}
	
	
	public static void load(){
		InputStream input = null;
		try {
			input = new FileInputStream(new File(ZTAMain.mcFile + "/autovote.yml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ymlObject = (HashMap<String, Object>) yml.load(input);
		if(ymlObject == null) {
			ymlObject = new HashMap<String, Object>();
			dump();
		}
		
		try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void set(String key, Object value){
		if(value instanceof Collection) value = ((Collection<?>)value).stream().map(o -> {
			if(!(o instanceof String)) return o;
			
			return ((String)o).replaceAll(":", "\\{c\\}");
			
		}).collect(Collectors.toList());
		
		
		ymlObject.put(key, value);
	}
	
	public static Object get(String key){
		return ymlObject.get(key);
	}
	
	
	public static void dump(){
		FileWriter wr = null;
		try {
			wr = new FileWriter(new File(ZTAMain.mcFile + "/autovote.yml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		yml.dump(ymlObject, wr);
		
		
		
		
		try {
			wr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
