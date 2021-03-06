/*
 * Copyright (C) 2017-2021 Beezig Team
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
import eu.beezig.core.logging.session.SessionItem;
import eu.beezig.core.server.HiveMode;
import eu.beezig.core.server.IAutovote;
import eu.beezig.core.server.IDynamicMode;
import eu.beezig.core.server.IWinstreak;
import eu.beezig.core.server.monthly.IMonthly;
import eu.beezig.core.server.monthly.MonthlyField;
import eu.beezig.core.server.monthly.MonthlyService;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.UUIDUtils;
import eu.beezig.core.util.text.Message;
import eu.beezig.hiveapi.wrapper.player.Profiles;
import eu.beezig.hiveapi.wrapper.player.games.BedStats;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.concurrent.CompletableFuture;

public class BED extends HiveMode implements IAutovote, IMonthly, IDynamicMode, IWinstreak {

    private int bedsDestroyed;
    private String mode;
    private boolean won;
    private int ironSummoner = 1, goldSummoner, diamondSummoner;

    public int getDiamondSummoner() {
        return diamondSummoner;
    }

    public int getGoldSummoner() {
        return goldSummoner;
    }

    public int getIronSummoner() {
        return ironSummoner;
    }

    public void upgradeSummoner(String name) {
        if("Iron Ingot".equals(name)) ironSummoner++;
        else if("Gold Ingot".equals(name)) goldSummoner++;
        else if("Diamond".equals(name)) diamondSummoner++;
    }

    public int getBedsDestroyed() {
        return bedsDestroyed;
    }

    public void setBedsDestroyed(int bedsDestroyed) {
        this.bedsDestroyed = bedsDestroyed;
    }

    public BED() {
        statsFetcher.setApiComputer(name -> {
            BedStats api = Profiles.bed(name).join();
            GlobalStats stats = new GlobalStats();
            stats.setPoints((int) api.getPoints());
            stats.setKills((int) api.getKills());
            stats.setDeaths((int) api.getDeaths());
            stats.setVictories((int) api.getVictories());
            stats.setPlayed((int) api.getGamesPlayed());
            stats.setTitle(getTitleService().getTitle(api.getTitle(), Math.toIntExact(api.getPoints())));
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
                    .thenAcceptAsync(api -> stats.setTitle(getTitleService().getTitle(api.getTitle(), Math.toIntExact(api.getPoints()))))
                .exceptionally(e -> {
                ExceptionHandler.catchException(e);
                Message.error(Message.translate("error.stats_fetch"));
                return null;
            });
            return stats;
        });
        getAdvancedRecords().setExecutor(this::recordsExecutor);
        getAdvancedRecords().setSlowExecutor(this::slowRecordsExecutor);
        logger.setHeaders("Points", "Mode", "Map", "Kills", "Deaths", "Beds", "Victory?", "Timestamp", "GameID");
    }

    @Override
    public void won() {
        addPoints(100);
        won = true;
        winstreakService.increment();
    }

    private void recordsExecutor() {
        AdvRecUtils.addPvPStats(getAdvancedRecords());
    }

    private void slowRecordsExecutor() {
            int points = Message.getNumberFromFormat(getAdvancedRecords().getMessage("Points")).intValue();
            if (AdvRecUtils.needsAPI()) {
                AdvRecUtils.announceAPI();
                BedStats api = Profiles.bed(getAdvancedRecords().getTarget()).join();
                getAdvancedRecords().setVariables(api);
                getAdvancedRecords().setOrAddAdvanced(0, new ImmutablePair<>("Points",
                        getAdvancedRecords().getMessages().get(0).getRight() + AdvRecUtils.getTitle(getTitleService(), api.getTitle(), points)));
            }
    }

    @Override
    public String getName() {
        return "BedWars";
    }

    @Override
    public void end() {
        super.end();
        logger.log(getPoints(), mode, getMap(), getKills(), getDeaths(), bedsDestroyed,
                won, System.currentTimeMillis(), getGameID());
        if(getSessionService() != null)
            Beezig.get().getTemporaryPointsManager().getCurrentSession().pushItem(new SessionItem.Builder(getIdentifier())
                    .points(getPoints()).map(getMap()).gameStart(gameStart).kills(getKills()).deaths(getDeaths())
                    .custom("beds", Integer.toString(bedsDestroyed, 10)).build());
    }

    @Override
    public String getIdentifier() {
        return "bed";
    }

    @Override
    public int getMaxVoteOptions() {
        return 6;
    }

    @Override
    public boolean isLastRandom() {
        return true;
    }

    public String getMode() {
        return mode;
    }

    @Override
    public void setModeFromLobby(String lobby) {
        if("bedd".equals(lobby)) {
            mode = "Duos";
        } else if("bedt".equals(lobby)) {
            mode = "Teams";
        } else if("bedx".equals(lobby)) {
            mode = "LTM";
        } else mode = "Solo";
    }

    @Override
    public CompletableFuture<? extends MonthlyService> loadProfile() {
        return new BedStats(null).getMonthlyProfile(UUIDUtils.strip(Beezig.user().getId()))
                .thenApplyAsync(m -> new MonthlyService(m, MonthlyField.KILLS, MonthlyField.DEATHS, MonthlyField.KD));
    }
}
