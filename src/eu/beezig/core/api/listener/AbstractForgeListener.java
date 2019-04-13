/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
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

package eu.beezig.core.api.listener;

import eu.beezig.core.api.ArrayContainer;

public interface AbstractForgeListener {

    static AbstractForgeListener fromObject(Object from) {
        return new AbstractForgeListener() {
            @Override
            public void onLoad(String pluginVersion, String zigVersion) {
                ClassUtils.invokeMethod(from, ClassUtils.findMethod(from.getClass(), "onLoad", String.class, String.class), pluginVersion, zigVersion);
            }

            @Override
            public void onDisplaySettingsGui(Object[] settings) {
                ClassUtils.invokeMethod(from, ClassUtils.findMethod(from.getClass(), "onDisplaySettingsGui", Object.class), new ArrayContainer(settings));
            }

            @Override
            public void setActiveGame(String game) {
                ClassUtils.invokeMethod(from, ClassUtils.findMethod(from.getClass(), "setActiveGame", String.class), game);
            }

            @Override
            public void registerCommand(Object commandExecutor) {
                ClassUtils.invokeMethod(from, ClassUtils.findMethod(from.getClass(), "registerCommand", Object.class), commandExecutor);
            }

            @Override
            public void displayFriendJoin(String player) {
                ClassUtils.invokeMethod(from, ClassUtils.findMethod(from.getClass(), "displayFriendJoin", String.class), player);
            }

            @Override
            public void displayAutovoteGui() {
                ClassUtils.invokeMethod(from, ClassUtils.findMethod(from.getClass(), "displayAutovoteGui"));
            }

            @Override
            public void displayTIMVTestGui() {
                ClassUtils.invokeMethod(from, ClassUtils.findMethod(from.getClass(), "displayTIMVTestGui"));
            }

            @Override
            public void displayReportGui(String player) {
                ClassUtils.invokeMethod(from, ClassUtils.findMethod(from.getClass(), "displayReportGui", String.class), player);
            }
        };
    }

    void onLoad(String pluginVersion, String zigVersion);

    void onDisplaySettingsGui(Object[] settings);

    void setActiveGame(String game);

    void registerCommand(Object commandExecutor);

    void displayFriendJoin(String player);

    void displayAutovoteGui();

    void displayTIMVTestGui();

    void displayReportGui(String player);

}
