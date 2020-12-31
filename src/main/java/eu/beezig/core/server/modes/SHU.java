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
import eu.beezig.core.api.BeezigForge;
import eu.beezig.core.config.Settings;
import eu.beezig.core.server.HiveMode;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.server.modes.shu.*;
import eu.beezig.core.util.text.Message;
import eu.the5zig.mod.server.GameListenerRegistry;
import eu.the5zig.mod.server.GameState;

import java.util.List;
import java.util.Locale;

public class SHU extends HiveMode {
    private String game;

    @Override
    public String getIdentifier() {
        return "shu";
    }

    @Override
    public String getName() {
        return "Arcade Shuffle";
    }

    @Override
    protected void onModeJoin() {
        super.onModeJoin();
        if(Settings.AUTOVOTE_SHUFFLE.get().getBoolean() && BeezigForge.isSupported()) {
            try {
                List<String> maps = getAutovoteManager().getFavoriteMaps("shu");
                if(!maps.isEmpty() && BeezigForge.isSupported()) BeezigForge.get().autovoteShuffle(maps);
            } catch (Exception e) {
                Message.error(Message.translate("error.data_read"));
                Beezig.logger.error(e);
            }
        }
        if(BeezigForge.isSupported()) BeezigForge.get().setCurrentGame(null);
    }

    @Override
    protected boolean supportsTemporaryPoints() {
        return false;
    }

    public void joinGame(String game) {
        this.game = game.trim();
        String id = null;
        switch (this.game) {
            case "Splegg":
                id = "SP";
                break;
            case "BatteryDash":
                id = "BD";
                break;
            case "ElectricFloor":
                id = "EF";
                break;
            case "Cranked":
                id = "CR";
                break;
            case "Sploop":
                id = "SPL";
                break;
            case "Instagib":
                id = "IG";
                break;
            case "One in the Chamber":
                id = "OITC";
                break;
            case "Slaparoo":
                id = "SLAP";
                break;
            case "Music Masters":
                id = "MM";
                break;
            case "Restaurant Rush":
                id = "RR";
                break;
            case "EF: Turbo":
                id = "EFT";
                break;
        }
        if(id != null) {
            ServerHive.current().getListener().updateLobby(id.toLowerCase(Locale.ROOT));
            HiveMode newMode = (HiveMode) ServerHive.current().getGameListener().getCurrentGameMode();
            if(newMode != null) {
                newMode.setState(GameState.GAME);
            }
        }
    }

    public String getGame() {
        return game;
    }

    public static void registerListeners(GameListenerRegistry registry) {
        registry.registerListener(new CR.CRListener());
        registry.registerListener(new EF.EfListener());
        registry.registerListener(new SLAP.SlapListener());
        registry.registerListener(new IG.IgListener());
        registry.registerListener(new OITC.OitcListener());
    }
}
