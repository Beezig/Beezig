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

package eu.beezig.core.advrec;

import eu.beezig.core.Beezig;
import eu.beezig.core.config.Settings;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.DateUtils;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;
import eu.beezig.hiveapi.wrapper.player.GameStats;
import eu.the5zig.mod.server.IPatternResult;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class AdvancedRecords {
    public static final String API_PREFIX = "§7§lⓘ§r";
    private static final String FOOTER_FORMAT = "                                %s§m                        §r                  ";
    private List<Pair<String, String>> messages;
    private List<AdvancedMessage> advancedMessages;
    private boolean listening = false;
    private Mode mode;
    private String target;
    private Callable<Void> executor;
    private Callable<Void> slowExecutor;
    private Instant apiCache;

    public AdvancedRecords() {
        messages = new ArrayList<>();
        advancedMessages = new ArrayList<>();
        refreshMode();
    }

    public void setVariables(GameStats stats) {
        this.apiCache = DateUtils.toInstant(stats.getCachedAt());
    }

    public String getTarget() {
        return target;
    }

    public List<Pair<String, String>> getMessages() {
        return messages;
    }

    public List<AdvancedMessage> getAdvancedMessages() {
        return advancedMessages;
    }

    public void setExecutor(Runnable executor) {
        this.executor = () -> {
            executor.run();
            return null;
        };
    }

    public void setSlowExecutor(Runnable slowExecutor) {
        this.slowExecutor = () -> {
            slowExecutor.run();
            return null;
        };
    }

    /**
     * Attempts to parse the message for Advanced Records.
     * @return whether the message should be ignored
     */
    public boolean parseMessage(String key, IPatternResult matches) {
        if(!Settings.ADVANCED_RECORDS.get().getBoolean()) return false;
        if(!listening && "advrec.start".equals(key)) {
            listening = true;
            target = matches.get(0);
            return true;
        }
        else if(listening && "advrec.statistic".equals(key)) {
            messages.add(new ImmutablePair<>(matches.get(0), modifyValue(matches.get(1))));
            return true;
        }
        else if(listening && "advrec.url".equals(key)) return true;
        else if(listening && "advrec.end".equals(key)) {
            listening = false;
            execute();
            return true;
        }
        return false;
    }

    private String modifyValue(String old) {
        return Settings.THOUSANDS_SEPARATOR.get().getBoolean() ? Message.formatNumber(Integer.parseInt(old, 10)) : old;
    }

    public String modifyValue(int old) {
        return Settings.THOUSANDS_SEPARATOR.get().getBoolean() ? Message.formatNumber(old) : Integer.toString(old, 10);
    }

    /**
     * If mode is SECOND_CHAT, adds the message to the "Advanced" list. Otherwise it replaces one of the normal messages.
     * @param index the normal message to replace
     * @param message the new message
     */
    public void setOrAddAdvanced(int index, Pair<String, String> message, boolean api) {
        if(mode == Mode.SECOND_CHAT) advancedMessages.add(new AdvancedMessage(message, api));
        else messages.set(index, message);
    }

    public void setOrAddAdvanced(int index, Pair<String, String> message) {
        setOrAddAdvanced(index, message, false);
    }

    public void addAdvanced(Pair<String, String> message, boolean api) {
        advancedMessages.add(new AdvancedMessage(message, api));
    }

    public void addAdvanced(Pair<String, String> message) {
        addAdvanced(message, false);
    }

    private void execute() {
        Beezig.get().getAsyncExecutor().execute(() -> {
            try {
                if (executor != null) executor.call();
                if (mode == Mode.SECOND_CHAT) sendMessages(false);
                if (slowExecutor != null) slowExecutor.call();
                if (mode == Mode.SECOND_CHAT) sendAdvanced();
                else sendMessages(true);
            } catch (Exception ex) {
                ExceptionHandler.catchException(ex, "Exception in advrec");
                Message.error(Message.translate("error.advrec"));
            }
            messages.clear();
            advancedMessages.clear();
        });
    }

    /**
     * @return whether the footer should be a generic "stats might take a while to update" message
     */
    private boolean shouldDisplayGeneric() {
        return apiCache == null && advancedMessages.stream().anyMatch(msg -> msg.api);
    }

    private String getGenericMessage() {
        return StringUtils.linedCenterText(Color.primary(), API_PREFIX + Color.accent() + " " + Message.translate("advrec.update"));
    }

    private void sendAdvanced() {
        if(mode == Mode.SECOND_CHAT) {
            String header = StringUtils.linedCenterText(Color.primary(), Color.primary() + Beezig.api().translate("advrec.header", Color.accent() + target + Color.primary()));
            Beezig.api().messagePlayerInSecondChat(header);
            advancedMessages.forEach(p -> sendMessage(p.format()));
            if(shouldDisplayGeneric()) Beezig.api().messagePlayerInSecondChat(getGenericMessage());
            else if(apiCache == null) Beezig.api().messagePlayerInSecondChat(String.format(FOOTER_FORMAT, Color.primary()));
            else sendLastUpdated(apiCache, true, true);
        }
        else advancedMessages.forEach(p -> sendMessage(p.format()));
    }

    private void sendMessages(boolean includeAdvanced) {
        String header = StringUtils.linedCenterText(Color.primary(), Color.primary() + Beezig.api().translate("advrec.header", Color.accent() + target + Color.primary()));
        Beezig.api().messagePlayer(header);
        messages.forEach(p -> Beezig.api().messagePlayer(formatMessage(p.getLeft(), p.getRight())));
        if(includeAdvanced) sendAdvanced();
        if(includeAdvanced && shouldDisplayGeneric()) Beezig.api().messagePlayer(getGenericMessage());
        else if(apiCache == null || !includeAdvanced) Beezig.api().messagePlayer(String.format(FOOTER_FORMAT, Color.primary()));
        else sendLastUpdated(apiCache, true, false);
    }

    private void sendMessage(String msg) {
        if(mode == Mode.SECOND_CHAT) Beezig.api().messagePlayerInSecondChat(msg);
        else Beezig.api().messagePlayer(msg);
    }

    private static String formatMessage(String key, String value) {
        return String.format(" %s%s: %s%s", Color.primary(), key, Color.accent(), value);
    }

    public String getMessage(String key) {
        Pair<String, String> pair = messages.stream().filter(p -> key.equals(p.getLeft())).findAny().orElse(null);
        return pair == null ? null : pair.getRight();
    }

    public static void sendLastUpdated(Instant cached, boolean includePrefix, boolean secondChat) {
        String lastUpdate = String.format("%s (%s)", Message.formatTime(cached), StringUtils.getTimeAgo(cached.toEpochMilli()));
        String display = StringUtils.linedCenterText(Color.primary(), (includePrefix ? API_PREFIX + " " : "") +
            Color.primary() + Beezig.api().translate("advrec.last_updated", Color.accent() + lastUpdate));
        if(secondChat) Beezig.api().messagePlayerInSecondChat(display);
        else Beezig.api().messagePlayer(display);
    }

    public void refreshMode() {
        mode = (Mode) Settings.ADVREC_MODE.get().getValue();
    }

    public enum Mode {
        NORMAL,
        SECOND_CHAT
    }

    private static class AdvancedMessage {
        Pair<String, String> message;
        boolean api;

        public AdvancedMessage(Pair<String, String> message, boolean api) {
            this.message = message;
            this.api = api;
        }

        String format() {
            return (api ? " " + API_PREFIX : "") + AdvancedRecords.formatMessage(message.getLeft(), message.getRight());
        }
    }
}
