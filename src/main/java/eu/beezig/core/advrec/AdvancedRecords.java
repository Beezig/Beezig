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
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.StringUtils;
import eu.the5zig.mod.server.IPatternResult;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.List;

public class AdvancedRecords {
    private List<Pair<String, String>> messages;
    private boolean listening = false;
    private Mode mode;
    private String target;
    private Runnable executor;

    public AdvancedRecords() {
        messages = new LinkedList<>();
        mode = (Mode) Settings.ADVREC_MODE.get().getValue();
    }

    public String getTarget() {
        return target;
    }

    public List<Pair<String, String>> getMessages() {
        return messages;
    }

    public void setExecutor(Runnable executor) {
        this.executor = executor;
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
            return mode == Mode.NORMAL;
        }
        else if(listening && "advrec.statistic".equals(key)) {
            messages.add(new ImmutablePair<>(matches.get(0), modifyValue(matches.get(1))));
            return mode == Mode.NORMAL;
        }
        else if(listening && "advrec.url".equals(key)) return mode == Mode.NORMAL;
        else if(listening && "advrec.end".equals(key)) {
            listening = false;
            execute();
            return mode == Mode.NORMAL;
        }
        return false;
    }

    private String modifyValue(String old) {
        return Settings.THOUSANDS_SEPARATOR.get().getBoolean() ? Message.formatNumber(Integer.parseInt(old, 10)) : old;
    }

    public String modifyValue(int old) {
        return Settings.THOUSANDS_SEPARATOR.get().getBoolean() ? Message.formatNumber(old) : Integer.toString(old, 10);
    }

    private void execute() {
        Beezig.get().getAsyncExecutor().execute(() -> {
            if(executor != null) executor.run();
            sendMessages();
            messages.clear();
        });
    }

    private void sendMessages() {
        sendMessage(StringUtils.linedCenterText(Color.primary(), Color.primary() + Beezig.api().translate("advrec.header", Color.accent() + target + Color.primary())));
        messages.forEach(p -> sendMessage(formatMessage(p.getLeft(), p.getRight())));
        sendMessage(String.format("                      %s§m                        §r                  ", Color.primary()));
    }

    private void sendMessage(String msg) {
        if(mode == Mode.SECOND_CHAT) Beezig.api().messagePlayerInSecondChat(msg);
        else Beezig.api().messagePlayer(msg);
    }

    private String formatMessage(String key, String value) {
        return String.format(" %s%s: %s%s", Color.primary(), key, Color.accent(), value);
    }

    public String getMessage(String key) {
        Pair<String, String> pair = messages.stream().filter(p -> key.equals(p.getLeft())).findAny().orElse(null);
        return pair == null ? null : pair.getRight();
    }

    public enum Mode {
        NORMAL,
        SECOND_CHAT
    }
}
