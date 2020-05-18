/*
 * Copyright (C) 2019 Beezig Team
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

package eu.beezig.core.server.modes;

import eu.beezig.core.Beezig;
import eu.beezig.core.advrec.AdvRecUtils;
import eu.beezig.core.server.HiveMode;
import eu.beezig.core.util.UUIDUtils;
import eu.beezig.core.util.text.Message;
import eu.beezig.hiveapi.wrapper.player.Profiles;
import eu.beezig.hiveapi.wrapper.player.games.BedStats;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class BED extends HiveMode {

    public BED() {
        statsFetcher.setScoreboardTitle("Your BED[DTX]? Stats");
        statsFetcher.setApiComputer(name -> {
            BedStats api = Profiles.bed(name).join();
            GlobalStats stats = new GlobalStats();
            stats.setPoints((int) api.getPoints());
            stats.setKills((int) api.getKills());
            stats.setDeaths((int) api.getDeaths());
            stats.setVictories((int) api.getVictories());
            stats.setPlayed((int) api.getGamesPlayed());
            stats.setTitle(getTitleService().getTitle(api.getTitle()));
            return stats;
        });
        statsFetcher.setScoreboardComputer(lines -> {
            GlobalStats stats = new GlobalStats();
            stats.setPoints(lines.get("Points"));
            stats.setKills(lines.get("Kills"));
            stats.setDeaths(lines.get("Deaths"));
            stats.setVictories(lines.get("Victories"));
            stats.setPlayed(lines.get("Games Played"));
            Profiles.bed(UUIDUtils.strip(Beezig.user().getId()))
                    .thenAcceptAsync(api -> stats.setTitle(getTitleService().getTitle(api.getTitle())));
            return stats;
        });
        getAdvancedRecords().setExecutor(this::recordsExecutor);
    }

    private void recordsExecutor() {
        AdvRecUtils.addPvPStats(getAdvancedRecords());
        int points = Message.getNumberFromFormat(getAdvancedRecords().getMessage("Points")).intValue();
        if(AdvRecUtils.needsAPI()) {
            AdvRecUtils.announceAPI();
            BedStats api = Profiles.bed(getAdvancedRecords().getTarget()).join();
            getAdvancedRecords().getMessages().set(0, new ImmutablePair<>("Points",
                    getAdvancedRecords().getMessages().get(0).getRight() + AdvRecUtils.getTitle(getTitleService(), api.getTitle(), points)));
        }
    }

    @Override
    public String getName() {
        return "BedWars";
    }

    @Override
    public void end() {

    }

    @Override
    public String getIdentifier() {
        return "bed";
    }
}
