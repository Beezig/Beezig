package tk.roccodev.beezig.hiveapi.stuff.timv;

public class TIMVMap {

	private String name;
	private int enderchests;
	
	
	public String getName() {
		return name;
	}
	public int getEnderchests() {
		return enderchests;
	}
	public TIMVMap(String name, long enderchests) {
		
		this.name = name;
		this.enderchests = Math.toIntExact(enderchests);
	}
	
	
	
	
	
}
