package tk.roccodev.zta.command;

public interface Command {

	
	public String getName();
	public String[] getAliases();
	
	/*
	 * 
	 * Executes the command
	 * 
	 * @return !whether the command should be skipped and the server command should be run.
	 * 
	 * Example:
	 * "seen" command can only be run in Hive. If the server is not Hive, return false, "seen" server-side will be run.
	 * 
	 */
	public boolean execute(String[] args);
	
	
	
}
