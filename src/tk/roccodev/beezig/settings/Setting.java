package tk.roccodev.beezig.settings;

import eu.the5zig.mod.The5zigAPI;

import java.io.IOException;

public enum Setting {

    SHOW_NETWORK_RANK_TITLE(false, "AdvRec - Network Rank", "Advanced Records - Show the network-rank title behind username"),
    SHOW_NETWORK_RANK_COLOR(true,"AdvRec - Network Rank Color", "Advanced Records - Color the username/network-rank respective to their network-rank"),
    SHOW_RECORDS_LASTGAME(true, "AdvRec - Last Game", "Advanced Records - Show last time the player played that game"),
    SHOW_RECORDS_MONTHLYRANK(true, "AdvRec - Monthly Rank", "Advanced Records - Show players' rank on the Monthly Leaderboards by Maxthat"),
    SHOW_RECORDS_ACHIEVEMENTS(true, "AdvRec - Achievements", "Advanced Records - Show achievements"),
    SHOW_RECORDS_RANK(true, "AdvRec - Rank", "Advanced Records - Show point based rank"),
    DISCORD_RPC(true, "Discord Rich Presence", "Use Discord Rich Presence"),
    AUTOVOTE(true, "Autovote", "Turn the autovote feature on or off"),
    AUTOVOTE_RANDOM(true, "Autovote for Random map", "Autovote for random map if no favorites are found"),
    BRIEFING(true, "Receive Briefing", "Opt-in or opt-out for the news briefing"),
    PM_PING(false, "New PM ping", "Play a ping sound when a PM is received."),
    PM_NOTIFICATION(false, "New PM notification", "Receive a system notification when a PM is received."),
    MOD_REPORT_NOTIFICATION(true, "Report Notification", "Receive a notification when a report is made through Beezig. (Only for staff)"),
    RECEIVE_PARTY_INVITES(true,"Beezig Party Invites",  "Receive global party invites through Beezig"),
    TOCCATA(true, "Toccata", "Message Toccata some Latin when he joins"),
    AUTOGG(false, "AutoGG", "Automatically say GG once the game ends. Run /autogg for details."),

    TIMV_SHOW_KRR(true, "TIMV AdvRec - KRR", "TIMV Advanced Records - Show Karma/rolepoints"),
    TIMV_SHOW_MOSTPOINTS(true, "TIMV AdvRec - Record", "TIMV Advanced Records - Show Karma record"),
    TIMV_SHOW_KARMA_TO_NEXT_RANK(false, "TIMV AdvRec - To next rank", "TIMV Advanced Records - Show karma to next rank"),
    TIMV_SHOW_TRAITORRATIO(false, "TIMV AdvRec - TRatio", "TIMV Advanced Records - Show the Traitor Points / Rolepoints ratio"),
    TIMV_USE_TESTREQUESTS(true, "TIMV AdvRec - Custom Test", "Replace \" test\" with nicer phrases to avoid HAS"),

    DR_SHOW_POINTSPERGAME(true, "DR AdvRec - PPG", "DR Advanced Records - Show the avg. points per game"),
    DR_SHOW_RUNNERWINRATE(true, "DR AdvRec - Win%", "DR Advanced Records - Show the winrate as a runner"),
    DR_SHOW_DEATHSPERGAME(true, "DR AdvRec - Avg Deaths", "DR Advanced Records - Show the avg. deaths as runner"),
    DR_SHOW_POINTS_TO_NEXT_RANK(false, "DR AdvRec - To next rank", "DR Advanced Records - Show points to next rank"),
    DR_SHOW_KILLSPERGAME(true, "DR AdvRec - KPG", "DR Advanced Records - Show the avg. kills as death"),
    DR_SHOW_TOTALPB(true, "DR AdvRec - Total PB", "DR Advanced Records - Show the cumulative amount of personal bests"),

    BED_SHOW_POINTS_TO_NEXT_RANK(true,"BED AdvRec - To next rank", "BED Advanced Records - Show points to next rank"),
    BED_SHOW_ELIMINATIONS_PER_GAME(false, "BED AdvRec - TPG", "BED Advanced Records - Show Eliminations per Game"),
    BED_SHOW_BEDS_PER_GAME(true, "BED AdvRec - BPG",  "BED Advanced Records - Show Beds destroyed per Game"),
    BED_SHOW_DEATHS_PER_GAME(false, "BED AdvRec - DPG", "BED Advanced Records - Show Deaths per Game"),
    BED_SHOW_KILLS_PER_GAME(false, "BED AdvRec - KPG",  "BED Advanced Records - Show Kills per Game"),
    BED_SHOW_POINTS_PER_GAME(true, "BED AdvRec - PPG", "BED Advanced Records - Show Points gained per Game"),
    BED_SHOW_KD(true, "BED AdvRec - KDR", "BED Advanced Records - Show Kills/Deaths"),
    BED_SHOW_WINRATE(true, "BED AdvRec - Win%", "BED Advanced Records - Show Winrate"),

    Giant_SHOW_WINRATE(true, "GNT AdvRec - Win%", "Giant Advanced Records - Show Winrate"),
    Giant_SHOW_KD(true, "GNT AdvRec - KDR", "Giant Advanced Records - Show Kills/Deaths"),
    Giant_SHOW_PPG(true, "GNT AdvRec - PPG", "Giant Advanced Records - Show the average Points per Game"),

    HIDE_SHOW_WINRATE(true, "HIDE AdvRec - Win%", "HIDE Advanced Records - Show Winrate"),
    HIDE_SHOW_SEEKER_KPG(true, "HIDE AdvRec - Seeker KPG", "HIDE Advanced Records - Show Kills per Game as Seeker"),
    HIDE_SHOW_HIDER_KPG(false, "HIDE AdvRec - Hider KPG", "HIDE Advanced Records - Show Kills per Game as Hider"),
    HIDE_SHOW_POINTSPG(true, "HIDE AdvRec - PPG", "HIDE Advanced Records - Show Points per Game"),
    HIDE_SHOW_AMOUNT_UNLOCKED(true, "HIDE AdvRec - Blocks", "HIDE Advanced Records - Show amount of unlocked blocks"),
    HIDE_SHOW_POINTS_TO_NEXT_RANK(true, "HIDE AdvRec - To next rank", "HIDE Advanced Records - Show points to next rank"),

    CAI_SHOW_WINRATE(true, "CAI AdvRec - Win%", "CAI Advanced Records - Show Winrate"),
    CAI_SHOW_POINTSPG(true,"CAI AdvRec - PPG", "CAI Advanced Records - Show Points per Game"),
    CAI_SHOW_POINTS_TO_NEXT_RANK(true, "CAI AdvRec - To next rank", "CAI Advanced Records - Show points to next rank"),
    CAI_SHOW_CATCHES_CAUGHT(true, "CAI AdvRec - Cc/Ct",  "CAI Advanced Records - Show Catches/Caught Ratio"),
    CAI_SHOW_CAPTURES_GAME(true, "CAI AdvRec - CPG", "CAI Advanced Records - Show Captures/Games Ratio"),

    SKY_SHOW_POINTS_TO_NEXT_RANK(true, "SKY AdvRec - To next rank", "SKY Advanced Records - Show points to next rank"),
    SKY_SHOW_WINRATE(true, "SKY AdvRec - Win%", "SKY Advanced Records - Show Winrate"),
    SKY_SHOW_KD(true, "SKY AdvRec - KDR", "SKY Advanced Records - Show Kills/Deaths"),
    SKY_SHOW_KPG(true, "SKY AdvRec - KPG", "SKY Advanced Records - Show Kills per game"),
    SKY_SHOW_PPG(true, "SKY AdvRec - PPG", "SKY Advanced Records - Show Points per game"),

    GRAV_SHOW_POINTS_TO_NEXT_RANK(true, "GRAV AdvRec - To next rank", "GRAV Advanced Records - Show points to next rank"),
    GRAV_SHOW_FINISHRATE(false, "GRAV AdvRec - Finish%", "GRAV Advanced Records - Show Finishrate"),
    GRAV_SHOW_PPG(true, "GRAV AdvRec - PPG", "GRAV Advanced Records - Show Points per game"),

    MIMV_SHOW_POINTS_TO_NEXT_RANK(true, "MIMV AdvRec - To next rank", "MIMV Advanced Records - Show points to next rank"),
    MIMV_SHOW_WINRATE(true, "MIMV AdvRec - Win%", "MIMV Advanced Records - Show Winrate"),
    MIMV_SHOW_KD(true, "MIMV AdvRec - KDR", "MIMV Advanced Records - Show Kills/Deaths"),
    MIMV_SHOW_KPG(true, "MIMV AdvRec - KPG", "MIMV Advanced Records - Show Kills per game"),
    MIMV_SHOW_PPG(true, "MIMV AdvRec - Karma/Game", "MIMV Advanced Records - Show Karma per game"),

    BP_SHOW_POINTS_TO_NEXT_RANK(true, "BP AdvRec - To next rank", "BP Advanced Records - Show points to next rank"),
    BP_SHOW_WINRATE(true, "BP AdvRec - Win%", "BP Advanced Records - Show Winrate"),
    BP_SHOW_PPG(true, "BP AdvRec - PPG", "BP Advanced Records - Show Points per game"),
    BP_JUKEBOX(true, "BP Jukebox", "BP - Listen to music while playing"),

    SGN_SHOW_POINTS_TO_NEXT_RANK(true, "SG2 AdvRec - To next rank", "SG2 Advanced Records - Show points to next rank"),
    SGN_SHOW_WINRATE(true, "SG2 AdvRec - Win%", "SG2 Advanced Records - Show Winrate"),
    SGN_SHOW_PPG(true, "SG2 AdvRec - PPG",  "SG2 Advanced Records - Show Points per game"),
    SGN_SHOW_KD(true, "SG2 AdvRec - KDR", "SG2 Advanced Records - Show Kills/Deaths"),

    LAB_SHOW_POINTS_TO_NEXT_RANK(true, "LAB AdvRec - To next rank", "LAB Advanced Records - Show points to next rank"),
    LAB_SHOW_WINRATE(true, "LAB AdvRec - Win%", "LAB Advanced Records - Show Winrate"),
    LAB_SHOW_PPG(true, "LAB AdvRec - PPG", "LAB Advanced Records - Show Points per game");


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

