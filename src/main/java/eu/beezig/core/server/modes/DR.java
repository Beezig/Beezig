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

import com.google.common.reflect.TypeToken;
import eu.beezig.core.Beezig;
import eu.beezig.core.advrec.AdvRecUtils;
import eu.beezig.core.data.DataPath;
import eu.beezig.core.logging.session.SessionItem;
import eu.beezig.core.server.HiveMode;
import eu.beezig.core.server.IAutovote;
import eu.beezig.core.server.IMapExtra;
import eu.beezig.core.server.monthly.IMonthly;
import eu.beezig.core.server.monthly.MonthlyField;
import eu.beezig.core.server.monthly.MonthlyService;
import eu.beezig.core.speedrun.Run;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.UUIDUtils;
import eu.beezig.core.util.speedrun.DrWorldRecords;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;
import eu.beezig.core.util.text.TextButton;
import eu.beezig.hiveapi.wrapper.player.Profiles;
import eu.beezig.hiveapi.wrapper.player.games.DrStats;
import eu.the5zig.mod.util.component.MessageComponent;
import livesplitcore.TimeSpan;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DR extends HiveMode implements IAutovote, IMonthly, IMapExtra {
    private static final Pattern TIME_REGEX = Pattern.compile("(\\d+):(\\d+)\\.(\\d+)");
    private Map<String, MapData> maps;
    private MapData currentMapData;
    private int lastSbPoints;
    private int lastSbKills;
    private int checkpoints;
    private String finishTime;
    /**
     * Cached profile, used to load personal bests
     */
    private DrStats profile;

    private String pb;
    private DrWorldRecords.WorldRecord wr;

    private boolean started;

    // Native LiveSplit
    private Run currentRun;
    private AtomicBoolean checkedForNatives = new AtomicBoolean(false);

    public String getEndTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public void calcTime() {
        if (finishTime == null || wr == null) return;
        Matcher matcher = TIME_REGEX.matcher(finishTime);
        if (matcher.matches()) {
            if(currentRun != null) currentRun.forceEnd(TimeSpan.parse(finishTime));
            int mins = Integer.parseInt(matcher.group(1), 10);
            int secs = Integer.parseInt(matcher.group(2), 10);
            int millis = Integer.parseInt(matcher.group(3), 10);
            int total = mins * 60 * 1000 + secs * 1000 + millis;
            int delta = total - wr.getMillis();
            if (delta <= 0) Beezig.api().playSound("note.pling", 1f);
            if (delta == 0) {
                Message.info(Message.translate("msg.dr.wr.tie"));
                return;
            }
            String display = DurationFormatUtils.formatDuration(Math.abs(delta), "m:ss.SSS");
            Message.info(Beezig.api().translate("msg.dr.wr." + (delta > 0 ? "loss" : "beat"), Color.accent() + display + Color.primary()));
        }
    }

    /**
     * Called by the Speedrun Timer module when it is rendered.
     * Checks if the natives are properly installed, and warns the user otherwise.
     */
    public void checkForNatives() {
        if(checkedForNatives.compareAndSet(false, true)) {
            if(!Beezig.get().isNativeSpeedrun()) {
                Message.info(Message.translate("msg.speedrun.libraries"));
                Message.info(Beezig.api().translate("msg.speedrun.libraries.size", Color.accent() + "4MB" + Color.primary()));
                TextButton btn = new TextButton("btn.speedrun.libraries.name", "btn.speedrun.libraries.desc", Color.accent());
                btn.doRunCommand("/drsplit natives");
                MessageComponent info = new MessageComponent(Message.infoPrefix());
                info.getSiblings().add(btn);
                Beezig.api().messagePlayerComponent(info, false);
            }
        }
    }

    public void initMapData() {
        try {
            maps = Beezig.get().getData().getDataMap(DataPath.DR_MAPS, new TypeToken<Map<String, MapData>>() {
            });
            if (maps == null) {
                Message.error("error.data_read");
                Beezig.logger.error("Tried to fetch maps but file wasn't found.");
            }
        } catch (Exception e) {
            Message.error("error.data_read");
            ExceptionHandler.catchException(e);
        }
    }

    @Override
    public String getIdentifier() {
        return "dr";
    }

    @Override
    public String getName() {
        return "DeathRun";
    }

    @Override
    public int getMaxVoteOptions() {
        return 6;
    }

    @Override
    public boolean isLastRandom() {
        return true;
    }

    @Override
    public void setMap(String map) {
        super.setMap(map);
        String normalized = StringUtils.normalizeMapName(map);
        MapData data = maps.get(normalized);
        if (data == null) {
            Message.error(Message.translate("error.map_not_found"));
            return;
        }
        currentMapData = data;
        if(Beezig.get().isNativeSpeedrun()) {
            try {
                currentRun = new Run(normalized, currentMapData, map);
            } catch (IOException e) {
                ExceptionHandler.catchException(e, "Run init");
            }
        }
        if (profile != null) {
            Long record = profile.getMapRecords().get(currentMapData.hive);
            if (record != null) {
                // Personal Best
                long pbSecs = record;
                pb = DurationFormatUtils.formatDuration(pbSecs * 1000, "m:ss");
            }
        }
        DrWorldRecords.getRecord(currentMapData).thenAcceptAsync(record -> wr = record)
            .exceptionally(e -> null); // Most of the times it's just map/record not found
    }

    public DR() {
        statsFetcher.setApiComputer(name -> {
            DrStats api = Profiles.dr(name).join();
            profile = api;
            GlobalStats stats = new GlobalStats();
            stats.setPoints((int) api.getPoints());
            stats.setKills((int) api.getKills());
            stats.setPlayed((int) api.getGamesPlayed());
            stats.setDeaths((int) api.getDeaths());
            stats.setVictories((int) api.getVictories());
            stats.setTitle(getTitleService().getTitle(api.getTitle()));
            return stats;
        });
        statsFetcher.setScoreboardComputer(lines -> {
            GlobalStats stats = new GlobalStats();
            stats.setPoints(lines.get("Points"));
            stats.setKills(lines.get("Kills"));
            stats.setPlayed(lines.get("Games Played"));
            stats.setDeaths(lines.get("Deaths"));
            stats.setVictories(lines.get("Victories"));
            Profiles.dr(UUIDUtils.strip(Beezig.user().getId()))
                .thenAcceptAsync(api -> {
                    profile = api;
                    stats.setTitle(getTitleService().getTitle(api.getTitle()));
                }).exceptionally(e -> {
                ExceptionHandler.catchException(e);
                Message.error(Message.translate("error.stats_fetch"));
                return null;
            });
            return stats;
        });
        getAdvancedRecords().setExecutor(this::recordsExecutor);
        getAdvancedRecords().setSlowExecutor(this::slowRecordsExecutor);
        logger.setHeaders("Points", "Map", "Kills", "Deaths", "GameID", "Timestamp", "Time");
        setGameID(Long.toString(System.currentTimeMillis(), 10));
    }

    public DrWorldRecords.WorldRecord getWorldRecord() {
        return wr;
    }

    private void recordsExecutor() {
        AdvRecUtils.addPvPStats(getAdvancedRecords());
    }

    private void slowRecordsExecutor() {
        int points = Message.getNumberFromFormat(getAdvancedRecords().getMessage("Points")).intValue();
        if (AdvRecUtils.needsAPI()) {
            AdvRecUtils.announceAPI();
            DrStats api = Profiles.dr(getAdvancedRecords().getTarget()).join();
            getAdvancedRecords().setVariables(api);
            getAdvancedRecords().setOrAddAdvanced(0, new ImmutablePair<>("Points",
                getAdvancedRecords().getMessages().get(0).getRight() +
                    AdvRecUtils.getTitle(getTitleService(), api.getTitle(), points)));
        }
    }

    @Override
    public void end() {
        super.end();
        logger.log(getPoints(), getMap(), getKills(), getDeaths(), getGameID(), System.currentTimeMillis(), finishTime);
        if (getSessionService() != null)
            Beezig.get().getTemporaryPointsManager().getCurrentSession().pushItem(new SessionItem.Builder(getIdentifier())
                .points(getPoints()).map(getMap()).custom("time", finishTime).gameStart(gameStart).kills(getKills()).deaths(getDeaths()).build());
        if (currentRun != null) currentRun.save();
    }

    public void tryStart() {
        if(started) return;
        started = true;
        if(currentRun != null) currentRun.start();
    }

    public void tryUpdatePoints(String newAmount) {
        int num = Integer.parseInt(newAmount, 10);
        if (lastSbPoints == num) return;
        addPoints(num - lastSbPoints);
        lastSbPoints = num;
    }

    public void tryUpdateKills(String newAmount) {
        int num = Integer.parseInt(newAmount, 10);
        if (lastSbKills == num) return;
        addKills(num - lastSbKills);
        lastSbKills = num;
    }

    @Override
    public CompletableFuture<? extends MonthlyService> loadProfile() {
        return new DrStats(null).getMonthlyProfile(UUIDUtils.strip(Beezig.user().getId()))
            .thenApplyAsync(m -> new MonthlyService(m, MonthlyField.KILLS, MonthlyField.DEATHS, MonthlyField.KD));
    }

    public String getPersonalBest() {
        return pb;
    }

    @Override
    public String getMapInformation() {
        return String.format("%d/%d %s", checkpoints, currentMapData == null ? 0 : currentMapData.checkpoints, Message.translate("msg.map.dr.checkpoints"));
    }

    public Run getCurrentRun() {
        return currentRun;
    }

    public boolean isStarted() {
        return started;
    }

    public MapData getCurrentMapData() {
        return currentMapData;
    }

    public void addCheckpoint() {
        checkpoints++;
        if(currentRun != null) currentRun.split();
    }

    @Override
    protected void stop() {
        if(currentRun != null) currentRun.endNow();
    }

    public static class MapData {
        public String speedrun, hive;
        public int checkpoints;
    }
}
