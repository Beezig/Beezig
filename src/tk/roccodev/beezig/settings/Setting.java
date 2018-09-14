package tk.roccodev.beezig.settings;

import eu.the5zig.mod.The5zigAPI;

import java.io.IOException;

public enum Setting {

    THOUSANDS_SEPARATOR(true, "Thousands Separator", "Separate thousands with a comma or a dot, depending on the system language"),
    ADVANCED_RECORDS(true, "Advanced Records", "Display more stats when running /records"),
    SHOW_NETWORK_RANK_TITLE(false, "AdvRec - Network Rank", "Advanced Records - Show the network-rank title behind username"),
    SHOW_NETWORK_RANK_COLOR(true, "AdvRec - Network Rank Color", "Advanced Records - Color the username/network-rank respective to their network-rank"),
    SHOW_RECORDS_LASTGAME(true, "AdvRec - Last Game", "Advanced Records - Show last time the player played that game"),
    SHOW_RECORDS_MONTHLYRANK(true, "AdvRec - Monthly Rank", "Advanced Records - Show players' rank on the Monthly Leaderboards by Maxthat"),
    SHOW_RECORDS_ACHIEVEMENTS(true, "AdvRec - Achievements", "Advanced Records - Show achievements"),
    SHOW_RECORDS_RANK(true, "AdvRec - Rank", "Advanced Records - Show point based rank"),
    SHOW_RECORDS_POINTSTONEXTRANK(true, "AdvRec - To next rank", "Advanced Records - Show points to next rank"),
    SHOW_RECORDS_WINRATE(true, "AdvRec - Win%", "Advanced Records - Show winrate"),
    SHOW_RECORDS_KDR(true, "AdvRec - K/D", "Advanced Records - Show Kills/Deaths"),
    SHOW_RECORDS_PPG(true, "AdvRec - PPG", "Advanced Records - Show Points per Game"),
    SHOW_RECORDS_KPG(true, "AdvRec - KPG", "Advanced Records - Show Kills per Game"),
    SHOW_RECORDS_DPG(true, "AdvRec - DPG", "Advanced Records - Show Deaths per Game"),

    DISCORD_RPC(true, "Discord Rich Presence", "Use Discord Rich Presence"),
    AUTOVOTE(true, "Autovote", "Turn the autovote feature on or off"),
    AUTOVOTE_RANDOM(true, "Autovote for Random map", "Autovote for random map if no favorites are found"),
    BRIEFING(true, "Receive Briefing", "Opt-in or opt-out for the news briefing"),
    PM_PING(false, "New PM ping", "Play a ping sound when a PM is received."),
    PM_NOTIFICATION(false, "New PM notification", "Receive a system notification when a PM is received."),
    MOD_REPORT_NOTIFICATION(true, "Report Notification", "Receive a notification when a report is made through Beezig. (Only for staff)"),
    RECEIVE_PARTY_INVITES(true, "Beezig Party Invites", "Receive global party invites through Beezig"),
    TOCCATA(true, "Toccata", "Message Toccata some Latin when he joins"),
    AUTOGG(false, "AutoGG", "Automatically say GG once the game ends. Run /autogg for details."),

    TIMV_SHOW_KRR(true, "TIMV AdvRec - KRR", "TIMV Advanced Records - Show Karma/rolepoints"),
    TIMV_SHOW_MOSTPOINTS(true, "TIMV AdvRec - Record", "TIMV Advanced Records - Show Karma record"),
    TIMV_SHOW_TRAITORRATIO(false, "TIMV AdvRec - TRatio", "TIMV Advanced Records - Show the Traitor Points / Rolepoints ratio"),
    TIMV_USE_TESTREQUESTS(true, "TIMV AdvRec - Custom Test", "Replace \" test\" with nicer phrases to avoid HAS"),

    DR_SHOW_TOTALPB(true, "DR AdvRec - Total PB", "DR Advanced Records - Show the cumulative amount of personal bests"),

    BED_SHOW_ELIMINATIONS_PER_GAME(false, "BED AdvRec - TPG", "BED Advanced Records - Show Eliminations per Game"),
    BED_SHOW_BEDS_PER_GAME(true, "BED AdvRec - BPG", "BED Advanced Records - Show Beds destroyed per Game"),
    BED_SHOW_STREAK(true, "BED AdvRec - Streak", "BED Advanced Records - Show winstreak"),

    HIDE_SHOW_SEEKER_KPG(true, "HIDE AdvRec - Seeker KPG", "HIDE Advanced Records - Show Kills per Game as Seeker"),
    HIDE_SHOW_HIDER_KPG(false, "HIDE AdvRec - Hider KPG", "HIDE Advanced Records - Show Kills per Game as Hider"),
    HIDE_SHOW_AMOUNT_UNLOCKED(true, "HIDE AdvRec - Blocks", "HIDE Advanced Records - Show amount of unlocked blocks"),

    CAI_SHOW_CATCHES_CAUGHT(true, "CAI AdvRec - Cc/Ct", "CAI Advanced Records - Show Catches/Caught Ratio"),
    CAI_SHOW_CAPTURES_GAME(true, "CAI AdvRec - CPG", "CAI Advanced Records - Show Captures/Games Ratio"),
    CAI_TITLE(true, "CAI Better Titles", "Show a better version of the titles"),
    CAI_TITLE_SHOWNAME(false, "Leader names", "Show the leader names in the titles"),

    GRAV_SHOW_FINISHRATE(true, "GRAV AdvRec - Finish%", "GRAV Advanced Records - Show Finishrate"),

    BP_JUKEBOX(true, "BP Jukebox", "BP - Listen to music while playing");



    private boolean value;
    private String briefDesc, brieferDesc;

    Setting(boolean value, String brieferDesc, String briefDesc) {
        this.value = value;
        this.briefDesc = briefDesc;
        this.brieferDesc = brieferDesc;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;

        try {
            SettingsFetcher.saveSettings();
        } catch (IOException e) {
            The5zigAPI.getLogger().info("Failed to save Settings");
            e.printStackTrace();
        }

    }

    public String getBriefDescription() {
        return briefDesc;
    }

    public void setValueWithoutSaving(boolean value) {
        this.value = value;


    }

    public String getBrieferDescription() {
        return brieferDesc;
    }
}

