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

package eu.beezig.core.data.timv;

import com.google.common.base.Joiner;
import eu.beezig.core.Beezig;
import eu.beezig.core.config.Settings;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.modes.TIMV;
import eu.beezig.core.util.UUIDUtils;
import eu.the5zig.mod.event.ChatSendEvent;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.util.minecraft.ChatColor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TestMessagesManager {
    private static final Pattern TEST_MSG_REGEX = Pattern.compile("^(.+) test$|^test (.+)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PLAYER_LIST_REGEX = Pattern.compile("(\\w+)(?:[\\s,])*");
    private List<String> customMessages;
    private int lastMessage;
    private File configFile;

    public TestMessagesManager() {
        this.configFile = new File(Beezig.get().getBeezigDir(), "timv/testMessages.txt");
        try {
            init();
        } catch (IOException e) {
            Beezig.logger.error("Could not load TIMV test messages", e);
        }
    }

    private void init() throws IOException {
        if(!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
            customMessages = new ArrayList<>(Arrays.asList("{p} go test please", "{p} test please",
                    "{p} pls test", "{p}, would you mind testing?", "{p}, could you test please?",
                    "{p}, please go into the tester", "{p}, I'd appreciate it if you would test",
                    "{p}, how about testing?", "{p}, would you test for me?"));
            save();
            return;
        }
        customMessages = FileUtils.readLines(configFile, StandardCharsets.UTF_8);
    }

    public void add(String s) throws IOException {
        customMessages.add(s);
        save();
    }

    public void remove(int index) throws IOException {
        customMessages.remove(index);
        save();
    }

    public void save() throws IOException {
        FileUtils.writeLines(configFile, "UTF-8", customMessages);
    }

    public List<String> getCustomMessages() {
        return customMessages;
    }

    /**
     * Attempts to send a custom test message
     * @param oldMessage the old message
     * @return whether the old message should be ignored
     */
    private boolean sendCustomMessage(String oldMessage) {
        Matcher matcher = TEST_MSG_REGEX.matcher(oldMessage);
        if(!matcher.matches()) return false;
        String raw = matcher.group(1);
        Matcher playerMatcher = PLAYER_LIST_REGEX.matcher(raw);
        List<String> players = new ArrayList<>();
        List<String> onlinePlayers = Beezig.api().getServerPlayers().stream().map(UUIDUtils::getDisplayName).collect(Collectors.toList());
        while(playerMatcher.find()) {
            String name = playerMatcher.group(1);
            // Prevents stuff like "i'll test, don't test, come test" from matching
            if(!onlinePlayers.contains(ChatColor.stripColor(name))) {
                Beezig.logger.info("Test message matched but a player wasn't found, ignoring.");
                return false;
            }
            players.add(name);
        }
        if(players.size() == 0) return false;
        String display = players.size() == 1
                ? players.get(0)
                : Joiner.on(", ")
                .join(players.subList(0, players.size() - 1))
                .concat(" and ")
                .concat(players.get(players.size() - 1));
        String random = getRandomMessage();
        if(random == null) return false;
        Beezig.api().sendPlayerMessage(random.replace("{p}", display));
        return true;
    }

    private String getRandomMessage() {
        if(customMessages.size() == 0) return null;
        int random = ThreadLocalRandom.current().ints(0, customMessages.size()).distinct()
                .filter(i -> i != lastMessage).findFirst().orElse(0);
        lastMessage = random;
        return customMessages.get(random);
    }

    @EventHandler
    public void onSend(ChatSendEvent event) {
        if(ServerHive.isCurrent() && Beezig.api().getActiveServer().getGameListener().getCurrentGameMode() instanceof TIMV) {
            if(Settings.TIMV_TESTMESSAGES.get().getBoolean() && sendCustomMessage(event.getMessage())) event.setCancelled(true);
        }
    }
}
