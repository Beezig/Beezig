package tk.roccodev.zta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.zta.command.Command;

public class CommandManager {
	
	public static Set<Command> commandExecutors = new HashSet<Command>();
	
	
	/*
	 * Dispatches a command.
	 * 
	 * @return whether the command has been found
	 * 
	 * 
	 */
	public static boolean dispatchCommand(String str){
		
		String[] data = str.split(" ");
		String alias = data[0];
		Command cmdFound = null;
		for(Command cmd : commandExecutors){
			for(String s : cmd.getAliases()){
				if(s.equalsIgnoreCase(alias)) {
					cmdFound = cmd;
					break;
				}
			}
		}
		if(cmdFound == null) return false;
		
		List<String> dataList = new ArrayList<String>(Arrays.asList(data));
		dataList.remove(0); //Remove alias
		
		
		try{
		cmdFound.execute(dataList.toArray(new String[dataList.size()]));
		}catch(Exception e){
			e.printStackTrace();
			The5zigAPI.getAPI().messagePlayer(Log.error + "An error occurred while attempting to perform this command.");
		}
		return true;
	}
	
	public static void registerCommand(Command cmd){
		commandExecutors.add(cmd);
	}

}
