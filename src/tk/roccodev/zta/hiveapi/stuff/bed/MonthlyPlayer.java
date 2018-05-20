package tk.roccodev.zta.hiveapi.stuff.bed;

public class MonthlyPlayer {

	private int place;
	private long points, kills, deaths, victories, played, beds, teams;
	
	public int getPlace() {
		return place;
	}
	public long getPoints() {
		return points;
	}
	public long getKills() {
		return kills;
	}
	public long getDeaths() {
		return deaths;
	}
	public long getVictories() {
		return victories;
	}
	public long getPlayed() {
		return played;
	}
	public long getBeds() {
		return beds;
	}
	public long getTeams() {
		return teams;
	}
	public MonthlyPlayer(int place, long points, long kills, long deaths, long victories, long played, long beds,
			long teams) {
		
		this.place = place;
		this.points = points;
		this.kills = kills;
		this.deaths = deaths;
		this.victories = victories;
		this.played = played;
		this.beds = beds;
		this.teams = teams;
	}
	
	
	
	
}
