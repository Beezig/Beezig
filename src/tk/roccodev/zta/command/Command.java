package tk.roccodev.zta.command;

public interface Command {

	
	public String getName();
	public String[] getAliases();
	public void execute(String[] args);
	
	
}
