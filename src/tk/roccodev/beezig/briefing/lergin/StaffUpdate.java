package tk.roccodev.beezig.briefing.lergin;

public class StaffUpdate {

	private StaffChangeType type;
	private String staffName;
	
	public StaffChangeType getType() {
		return type;
	}
	public String getStaffName() {
		return staffName;
	}
	public StaffUpdate(StaffChangeType type, String staffName) {
		this.type = type;
		this.staffName = staffName;
	}
	
	
	
	
}
