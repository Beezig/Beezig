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

package eu.beezig.core.automessage;

import eu.beezig.core.Beezig;
import eu.beezig.core.config.Setting;
import eu.beezig.core.data.DataPath;
import eu.beezig.core.server.HiveMode;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.ActiveGame;
import eu.the5zig.mod.event.ChatEvent;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.TitleEvent;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AutoMessageManager {
    private Setting enabled;
    private boolean disablePartyChat;
    private Setting delay;
    private DataPath triggersPath;
    private Map<String, Trigger> triggers;
    private static List<String> messageQueue;
    private AtomicBoolean skipThis;
    private static AtomicBoolean skipAll = new AtomicBoolean();

    public AutoMessageManager(boolean disablePartyChat) {
        triggers = new HashMap<>();
        messageQueue = new ArrayList<>();
        enabled = getEnabledSetting();
        delay = getDelaySetting();
        triggersPath = getTriggersPath();
        this.disablePartyChat = disablePartyChat;
        skipThis = new AtomicBoolean();
    }

    public abstract Setting getEnabledSetting();
    public abstract String getMessage ();
    public abstract Setting getDelaySetting();
    public abstract DataPath getTriggersPath();

    private synchronized void handleEvent(String message, HiveMode mode, Trigger.Type... types) {
        HiveMode game = ActiveGame.get();
        if (game == null)
            return;

        if (!enabled.getBoolean() || !shouldFire())
            return;

        Trigger trigger = triggers.get(game.getIdentifier());
        if (trigger != null) {
            for (Trigger.Type t : types) {
                if (trigger.doesTrigger(ChatColor.stripColor(message), t) && !game.isAutoMessageSent(this.getClass())) {
                    game.setAutoMessageSent(this.getClass(), true);
                    if (skipThis.getAndSet(false) || skipAll.getAndSet(false))
                        continue;
                    Beezig.get().getAsyncExecutor().execute(() -> {
                        try {
                            if (((ServerHive) Beezig.api().getActiveServer()).getInPartyChat() && disablePartyChat) {
                                Beezig.get().getAsyncExecutor().schedule(() -> {
                                    if (ActiveGame.get() == mode) {
                                        Beezig.api().sendPlayerMessage("/p");
                                        messageQueue.add(getMessage());
                                    }
                                }, delay.getLong(), TimeUnit.MILLISECONDS);
                            } else {
                                Beezig.get().getAsyncExecutor().schedule(() -> {
                                        if (ActiveGame.get() == mode)
                                            Beezig.api().sendPlayerMessage(getMessage());
                                    }, delay.getLong(), TimeUnit.MILLISECONDS);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
    }

    public void initTriggers() {
        try {
            List<TriggerModeConnection> triggers = Beezig.get().getData().getDataList(triggersPath,
                    TriggerModeConnection[].class);
            triggers.forEach(t -> this.triggers.put(t.mode, t.trigger));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends queued messages that weren't sent because the player was still in party chat and re-enables party chat if
     * anything was sent.
     */
    public static void sendQueuedMessages() {
        for (String m : messageQueue) {
            Beezig.api().sendPlayerMessage(m);
        }
        if (!messageQueue.isEmpty()) {
            Beezig.api().sendPlayerMessage("/p");
        }
        messageQueue.clear();
    }

    public boolean skip() {
        boolean tmp;
        do {
            tmp = skipThis.get();
        } while (!skipThis.compareAndSet(tmp, !tmp));
        return !tmp;
    }

    public static boolean skipAll () {
        boolean tmp;
        do {
            tmp = skipAll.get();
        } while (!skipAll.compareAndSet(tmp, !tmp));
        return  !tmp;
    }

    @EventHandler
    public void onMessage(ChatEvent event) {
        handleEvent(event.getMessage(), ActiveGame.get(), Trigger.Type.CHAT);
    }

    @EventHandler
    public void onTitle(TitleEvent event) {
        // We're calling handleEvent for TITLE and SUBTITLE as this event triggers
        // for both types.
        handleEvent(event.getTitle(), ActiveGame.get(), Trigger.Type.TITLE, Trigger.Type.SUBTITLE);
    }

    /**
     * Defines whether AutoMessage should trigger (regardless of user settings).
     * This can be used to skip AutoMessage for a certain game.
     * @return whether AutoMessage should fire
     */
    public boolean shouldFire() {
        return true;
    }

    static class TriggerModeConnection {
        public String mode;
        public Trigger trigger;
    }
}