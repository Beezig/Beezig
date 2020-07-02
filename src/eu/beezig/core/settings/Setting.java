/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
 *
 * This file is part of Beezig.
 *
 * Beezig is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beezig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beezig.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.beezig.core.settings;

import eu.the5zig.mod.The5zigAPI;

import java.io.IOException;

public enum Setting {

    THOUSANDS_SEPARATOR(true, "Thousands Separator", "Separate thousands with a comma or a dot, depending on the system language", "LEVER"),
    ADVANCED_RECORDS(true, "Advanced Records", "Display more stats when running /records", "SIGN"),
    IGNORE_WARNINGS(false, "Ignore Warnings", "Ignore Briefing/WR and BeezigForge warnings.", "LEVER"),
    PARTY_MEMBERS(true, "Show party members", "Show party members upon receiving an invite.", "LEVER"),
    PARTY_FRIEND(true, "Party joining friends", "Makes the friend join message clickable so you can invite them to your party", "LEVER"),
    SHOW_NETWORK_RANK_TITLE(false, "AdvRec - Network Rank", "Advanced Records - Show the network-rank title behind username", "SIGN"),
    SHOW_NETWORK_RANK_COLOR(true, "AdvRec - Network Rank Color", "Advanced Records - Color the username/network-rank respective to their network-rank", "SIGN"),
    SHOW_RECORDS_LASTGAME(true, "AdvRec - Last Game", "Advanced Records - Show last time the player played that game", "SIGN"),
    SHOW_RECORDS_MONTHLYRANK(true, "AdvRec - Monthly Rank", "Advanced Records - Show players' rank on the Monthly Leaderboards", "SIGN"),
    SHOW_RECORDS_ACHIEVEMENTS(true, "AdvRec - Achievements", "Advanced Records - Show achievements", "SIGN"),
    SHOW_RECORDS_RANK(true, "AdvRec - Rank", "Advanced Records - Show point based rank", "SIGN"),
    SHOW_RECORDS_POINTSTONEXTRANK(true, "AdvRec - To next rank", "Advanced Records - Show points to next rank", "SIGN"),
    SHOW_RECORDS_WINRATE(true, "AdvRec - Win%", "Advanced Records - Show winrate", "SIGN"),
    SHOW_RECORDS_KDR(true, "AdvRec - K/D", "Advanced Records - Show Kills/Deaths", "SIGN"),
    SHOW_RECORDS_PPG(true, "AdvRec - PPG", "Advanced Records - Show Points per Game", "SIGN"),
    SHOW_RECORDS_KPG(true, "AdvRec - KPG", "Advanced Records - Show Kills per Game", "SIGN"),
    SHOW_RECORDS_DPG(true, "AdvRec - DPG", "Advanced Records - Show Deaths per Game", "SIGN"),

    STAFF_CHAT(true, "Quick staff chat", "Talk in staff chat by putting ~ before your message.", "PAPER"),
    DISCORD_RPC(true, "Discord Rich Presence", "Use Discord Rich Presence", "PAPER"),
    AUTOVOTE(true, "Autovote", "Turn the autovote feature on or off", "MAP"),
    AUTOVOTE_RANDOM(true, "Autovote for Random map", "Autovote for random map if no favorites are found", "MAP"),
    BRIEFING(true, "Receive Briefing", "Opt-in or opt-out for the news briefing", "PAPER"),
    PM_PING(false, "New PM ping", "Play a ping sound when a PM is received.", "JUKEBOX"),
    PM_NOTIFICATION(false, "New PM notification", "Receive a system notification when a PM is received.", "JUKEBOX"),
    MOD_REPORT_NOTIFICATION(true, "Report Notification", "Receive a notification when a report is made through Beezig. (Only for staff)", "RED_ROSE"),
    RECEIVE_PARTY_INVITES(true, "Beezig Party Invites", "Receive global party invites through Beezig", "BREWING_STAND_ITEM"),
    TOCCATA(true, "Toccata", "Message Toccata some Latin when he joins", "EGG"),
    AUTOGG(false, "AutoGG", "Automatically say GG once the game ends. Run /autogg for details.", "BOOK_AND_QUILL"),

    TIMV_SHOW_KRR(true, "TIMV AdvRec - KRR", "TIMV Advanced Records - Show Karma/rolepoints", "STICK"),
    TIMV_SHOW_MOSTPOINTS(true, "TIMV AdvRec - Record", "TIMV Advanced Records - Show Karma record", "STICK"),
    TIMV_SHOW_TRAITORRATIO(false, "TIMV AdvRec - TRatio", "TIMV Advanced Records - Show the Traitor Points / Rolepoints ratio", "STICK"),
    TIMV_USE_TESTREQUESTS(true, "TIMV AdvRec - Custom Test", "Replace \" test\" with nicer phrases to avoid HAS", "STICK"),

    DR_SHOW_TOTALPB(true, "DR AdvRec - Total PB", "DR Advanced Records - Show the cumulative amount of personal bests", "LAVA_BUCKET"),

    BED_SHOW_ELIMINATIONS_PER_GAME(false, "BED AdvRec - TPG", "BED Advanced Records - Show Eliminations per Game", "BED"),
    BED_SHOW_BEDS_PER_GAME(true, "BED AdvRec - BPG", "BED Advanced Records - Show Beds destroyed per Game", "BED"),
    BED_SHOW_STREAK(true, "BED AdvRec - Streak", "BED Advanced Records - Show winstreak", "BED"),

    HIDE_SHOW_SEEKER_KPG(true, "HIDE AdvRec - Seeker KPG", "HIDE Advanced Records - Show Kills per Game as Seeker", "WORKBENCH"),
    HIDE_SHOW_HIDER_KPG(false, "HIDE AdvRec - Hider KPG", "HIDE Advanced Records - Show Kills per Game as Hider", "WORKBENCH"),
    HIDE_SHOW_AMOUNT_UNLOCKED(true, "HIDE AdvRec - Blocks", "HIDE Advanced Records - Show amount of unlocked blocks", "WORKBENCH"),

    GRAV_SHOW_FINISHRATE(true, "GRAV AdvRec - Finish%", "GRAV Advanced Records - Show Finishrate", "GOLD_BOOTS"),

    BP_JUKEBOX(false, "BP Jukebox", "BP - Listen to music while playing", "RECORD_3");


    private boolean value;
    private String briefDesc, brieferDesc, labyIcon;

    Setting(boolean value, String brieferDesc, String briefDesc, String labyIcon) {
        this.value = value;
        this.briefDesc = briefDesc;
        this.brieferDesc = brieferDesc;
        this.labyIcon = labyIcon;
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

    public String getLabyIcon() {
        return labyIcon;
    }
}

