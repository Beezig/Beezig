/*
 * Copyright (C) 2017-2020 Beezig Team
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
import eu.beezig.core.autovote.AutovoteMap;
import eu.beezig.core.config.Settings;
import eu.beezig.core.logging.session.SessionItem;
import eu.beezig.core.server.HiveMode;
import eu.beezig.core.server.monthly.IMonthly;
import eu.beezig.core.server.monthly.MonthlyService;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.UUIDUtils;
import eu.beezig.core.util.speedrun.GravWorldRecords;
import eu.beezig.core.util.text.Message;
import eu.beezig.hiveapi.wrapper.player.Profiles;
import eu.beezig.hiveapi.wrapper.player.games.GravStats;
import eu.the5zig.mod.server.GameState;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class GRAV extends HiveMode implements IMonthly {

    private boolean won;
    // Keeps track of the other players' stage completions, to assign points based on the place
    private Map<Integer, Integer> stageCompletions = new HashMap<>();
    private int currentStage;
    private boolean afterFirst; // Whether currentStage is initialized, set either on map selection or on first map
    private AtomicLong lastConfirmed = new AtomicLong(0L);
    private String[] finalMaps = new String[5];
    private String nextMap;

    // Record caches
    private Map<String, Long> mapPbs = new HashMap<>();
    private Map<String, GravWorldRecords.WorldRecord> mapWrs = new HashMap<>();
    private String currentMapPb;
    private GravWorldRecords.WorldRecord currentMapWr;
    private CompletableFuture<Void> recordDownloadTask;

    public GRAV()
    {
        statsFetcher.setApiComputer(name -> {
            GravStats api = Profiles.grav(name).join();
            GlobalStats stats = new GlobalStats();
            stats.setPoints((int) api.getPoints());
            stats.setPlayed((int) api.getGamesPlayed());
            stats.setVictories((int) api.getVictories());
            stats.setTitle(getTitleService().getTitle(api.getTitle()));
            return stats;
        });
        statsFetcher.setScoreboardComputer(lines -> {
            GlobalStats stats = new GlobalStats();
            stats.setPoints(lines.get("Points"));
            stats.setPlayed(lines.get("Games Played"));
            stats.setVictories(lines.get("Victories"));
            Profiles.grav(UUIDUtils.strip(Beezig.user().getId()))
                    .thenAccept(api -> stats.setTitle(getTitleService().getTitle(api.getTitle())));
            return stats;
        });
        getAdvancedRecords().setExecutor(this::recordsExecutor);
        getAdvancedRecords().setSlowExecutor(this::slowRecordsExecutor);
        logger.setHeaders("Points", "Victory?", "Timestamp");
        setGameID(Long.toString(System.currentTimeMillis(), 10));
    }

    private void recordsExecutor() {
        if(Settings.ADVREC_WINRATE.get().getBoolean()) {
            int victories = Message.getNumberFromFormat(getAdvancedRecords().getMessage("Victories")).intValue();
            int played = Message.getNumberFromFormat(getAdvancedRecords().getMessage("Games Played")).intValue();
            double wr = played == 0 ? 0 : victories * 100D / (double) played;
            getAdvancedRecords().addAdvanced(new ImmutablePair<>("Win Rate", Message.ratio(wr) + "%"));
        }
    }

    private void slowRecordsExecutor() {
        int points = Message.getNumberFromFormat(getAdvancedRecords().getMessage("Points")).intValue();
        if (AdvRecUtils.needsAPI()) {
            AdvRecUtils.announceAPI();
            GravStats api = Profiles.grav(getAdvancedRecords().getTarget()).join();
            getAdvancedRecords().setVariables(api);
            getAdvancedRecords().setOrAddAdvanced(0, new ImmutablePair<>("Points",
                    getAdvancedRecords().getMessages().get(0).getRight() +
                            AdvRecUtils.getTitle(getTitleService(), api.getTitle(), points)));
        }
    }

    public String getCurrentMapPb() {
        return currentMapPb;
    }

    public GravWorldRecords.WorldRecord getCurrentMapWr() {
        return currentMapWr;
    }

    @Override
    protected void onModeJoin() {
        super.onModeJoin();
        downloadRecords();
    }

    public void setWon() {
        this.won = true;
    }

    private void updateRecords() {
        String map = finalMaps[currentStage].toUpperCase(Locale.ROOT).replace(" ", "_");
        if(mapWrs != null) currentMapWr = mapWrs.get(map);
        if(mapPbs == null) return;
        Long pb = mapPbs.get(map);
        if(pb != null) currentMapPb = DurationFormatUtils.formatDuration(pb, "m:ss.SSS");
    }

    public void stageDone() {
        if(currentStage < 5) {
            setMap(finalMaps[currentStage]);
            if (currentStage < 4) {
                nextMap = finalMaps[currentStage + 1];
                if (Settings.GRAV_NEXT_MAP.get().getBoolean())
                    Message.info(Beezig.api().translate("msg.grav.next_map", nextMap));
            }
            if(recordDownloadTask != null) {
                if(recordDownloadTask.isDone()) updateRecords();
                else recordDownloadTask = recordDownloadTask.thenRunAsync(this::updateRecords).exceptionally(e -> {
                    ExceptionHandler.catchException(e, "GRAV records load");
                    return null;
                });
            }
        }
        if(currentStage == 0) {
            currentStage++;
            return;
        }
        int place = stageCompletions.getOrDefault(currentStage, 1) - 1;
        if(currentStage == 5) addPoints(Math.max(10, 70 - 12 * place));
        else addPoints(Math.max(10, 20 - 2 * place));
        currentStage++;
    }

    public void addCompletion(int stage) {
        stageCompletions.put(stage, stageCompletions.getOrDefault(stage, 0) + 1);
    }

    @Override
    public void end() {
        super.end();
        logger.log(getPoints(), won, System.currentTimeMillis());
        if(getSessionService() != null)
            Beezig.get().getTemporaryPointsManager().getCurrentSession().pushItem(new SessionItem.Builder(getIdentifier())
                    .points(getPoints()).victory(won).gameStart(gameStart).build());
    }

    public Map<String, GravWorldRecords.WorldRecord> getCachedWrs() {
        return mapWrs;
    }

    @Override
    public String getIdentifier() {
        return "grav";
    }

    @Override
    public String getName() {
        return "Gravity";
    }

    @Override
    public CompletableFuture<? extends MonthlyService> loadProfile() {
        return new GravStats(null).getMonthlyProfile(UUIDUtils.strip(Beezig.user().getId()))
            .thenApplyAsync(MonthlyService::new);
    }

    // GRAV Autovoting
    private HashMap<String, AutovoteMap> maps = new HashMap<>();
    private boolean autovoteRun;

    public HashMap<String, AutovoteMap> getMaps() {
        return maps;
    }

    public boolean hasAutovoteRun() {
        return autovoteRun;
    }

    public void runAutovote() throws Exception {
        if(autovoteRun) return;
        if(!Settings.AUTOVOTE.get().getBoolean()) return;
        autovoteRun = true;
        List<String> favs = getAutovoteManager().getFavoriteMaps(getIdentifier());
        if(favs.isEmpty()) return;
        Map<Integer, Long> parsed = maps.entrySet().stream()
            .filter(e -> favs.contains(e.getKey())).collect(Collectors.groupingByConcurrent(e -> e.getValue().getIndex(), Collectors.counting()));
        if(parsed.isEmpty()) return;
        int index = parsed.entrySet().stream().max(Comparator.comparingLong(Map.Entry::getValue)).map(Map.Entry::getKey).orElse(1);
        Beezig.api().sendPlayerMessage("/v " + index);
        Message.info(Beezig.api().translate("msg.autovote.grav", Color.accent() + index + Color.primary()));
    }

    /**
     * @return whether Beezig should ask for confirmation
     */
    public boolean confirmDisconnect() {
        long now = System.currentTimeMillis();
        return !won && getState() == GameState.GAME && lastConfirmed.getAndSet(now) < now - 5000L;
    }

    public void setFinalMaps(String[] maps) {
        System.arraycopy(maps, 0, finalMaps, 0, 5);
        setMap(maps[0]);
        if(recordDownloadTask.isDone()) updateRecords();
        else recordDownloadTask = recordDownloadTask.thenRunAsync(this::updateRecords).exceptionally(e -> {
            ExceptionHandler.catchException(e, "GRAV records load");
            return null;
        });
    }

    private void downloadRecords() {
        CompletableFuture<Void> wrs = GravWorldRecords.getRecords().thenAcceptAsync(records -> this.mapWrs = records);
        CompletableFuture<Void> pbs = Profiles.grav(UUIDUtils.strip(Beezig.user().getId())).thenAcceptAsync(p -> this.mapPbs = p.getMapRecords());
        recordDownloadTask = CompletableFuture.allOf(wrs, pbs).exceptionally(e -> {
            ExceptionHandler.catchException(e, "GRAV records load");
            return null;
        });
    }

    public String getMap(int i) {
        return i > 0 && i < 5 ? finalMaps[i] : "";
    }

    public String getNextMap() {
        return nextMap;
    }

    public int getCurrentStage() {
        return currentStage;
    }
}
