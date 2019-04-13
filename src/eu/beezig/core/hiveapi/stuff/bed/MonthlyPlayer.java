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

package eu.beezig.core.hiveapi.stuff.bed;

public class MonthlyPlayer {

    private int place;
    private String uuid, username;
    private long points, kills, deaths, victories, played, beds, teams;

    public MonthlyPlayer(String uuid, String username, int place, long points, long kills, long deaths, long victories, long played, long beds,
                         long teams) {

        this.place = place;
        this.points = points;
        this.kills = kills;
        this.deaths = deaths;
        this.victories = victories;
        this.played = played;
        this.beds = beds;
        this.teams = teams;
        this.uuid = uuid;
        this.username = username;
    }

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

    public String getUUID() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }


}
