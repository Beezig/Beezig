package eu.beezig.core.briefing.lergin;

public enum StaffChangeType {


    MODERATOR_REMOVE("§cRetiring Moderators"),
    MODERATOR_ADD("§cNew Moderators"),
    SENIOR_MODERATOR_REMOVE("§4Retiring Sr. Moderators"),
    SENIOR_MODERATOR_ADD("§4New Sr. Moderators"),
    NECTAR_REMOVE("§6Retiring Builders"),
    NECTAR_ADD("§6New Builders"),
    DEVELOPER_REMOVE("§7Retiring Developers"),
    DEVELOPER_ADD("§7New Developers"),
    OWNER_REMOVE("§eRetiring Owners"),
    OWNER_ADD("§eNew Owners");


    private String display;

    StaffChangeType(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }


}
