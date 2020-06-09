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

package eu.beezig.core.logging;

import eu.beezig.core.Beezig;
import eu.beezig.core.data.GameTitles;
import eu.beezig.core.server.HiveMode;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * A manager class for daily and session points
 */
public class TemporaryPointsManager {
    private Map<String, DailyService> dailyServices = new HashMap<>();

    public DailyService getDailyForMode(HiveMode mode) {
        DailyService service = dailyServices.get(mode.getIdentifier());
        if(service != null) service.checkUpdate();
        return service;
    }

    public void init() throws ReflectiveOperationException {
        initDailyServices();
    }

    private void initDailyServices() throws ReflectiveOperationException {
        Beezig.logger.info("Loading daily points...");
        Map<CompletableFuture<DailyService>, String> futures = new HashMap<>();
        for(Class modeClass : GameTitles.modes) {
            HiveMode inst = (HiveMode) modeClass.newInstance();
            futures.put(parseLogForMode(inst), inst.getIdentifier());
        }
        CompletableFuture.allOf(futures.keySet().toArray(new CompletableFuture[0])).join();
        for(Map.Entry<CompletableFuture<DailyService>, String> entry : futures.entrySet()) {
            dailyServices.put(entry.getValue(), entry.getKey().join());
        }
        Beezig.logger.info("Daily points loaded.");
    }

    private CompletableFuture<DailyService> parseLogForMode(HiveMode mode) {
        CompletableFuture<DailyService> future = new CompletableFuture<>();
        Beezig.get().getAsyncExecutor().submit(() -> {
            if(!(mode instanceof ILoggable)) {
                future.complete(null);
                return;
            }
            File logFile = mode.getLogger().getFile();
            if(!logFile.exists()) {
                future.complete(new DailyService(0));
                return;
            }
            ILoggable loggable = (ILoggable) mode;
            int ptsIndex = loggable.getPointsIndex();
            int timestampIndex = loggable.getTimestampIndex();
            int todayYear = Calendar.getInstance().get(Calendar.YEAR);
            int todayDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
            try(ReverseCsvReader reader = new ReverseCsvReader(logFile, ",")) {
                int points = 0;
                String[] record;
                while((record = reader.getNextRecord()) != null) {
                    if(timestampIndex < record.length && ptsIndex < record.length) {
                        try {
                            long timestamp = Long.parseLong(record[timestampIndex]);
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(timestamp);
                            if(cal.get(Calendar.YEAR) != todayYear || cal.get(Calendar.DAY_OF_YEAR) != todayDay) break;
                            points += Integer.parseInt(record[ptsIndex]);
                        }
                        catch(NumberFormatException ex) {
                            Beezig.logger.error("Couldn't format timestamp or points string", ex);
                        }
                    }
                }
                future.complete(new DailyService(points));
            } catch (IOException e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}
