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

package eu.beezig.core.speedrun.config;

import eu.beezig.core.speedrun.TimerComparison;
import eu.beezig.core.util.text.Message;

import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("ImmutableEnumChecker")
public enum SpeedrunConfigValues {
    MODULES(SpeedrunConfig::getModules, (cfg, v) -> cfg.setModules((String[]) v)),
    COLOR_BACKGROUND(SpeedrunConfig::getBackgroundColor, (cfg, v) -> cfg.setBackgroundColor((int)(long) v)),
    COLOR_PREFIX(SpeedrunConfig::getPrefixColor, (cfg, v) -> cfg.setPrefixColor((int)(long) v)),
    COLOR_DEFAULT(SpeedrunConfig::getDefaultColor, (cfg, v) -> cfg.setDefault((int)(long) v)),
    COLOR_AHEAD_GAINING(SpeedrunConfig::getAheadGainingTimeColor, (cfg, v) -> cfg.setAheadGainingTimeColor((int)(long) v)),
    COLOR_AHEAD_LOSING(SpeedrunConfig::getAheadLosingTimeColor, (cfg, v) -> cfg.setAheadLosingTimeColor((int)(long) v)),
    COLOR_BEHIND_GAINING(SpeedrunConfig::getBehindGainingTimeColor, (cfg, v) -> cfg.setBehindGainingTimeColor((int)(long) v)),
    COLOR_BEHIND_LOSING(SpeedrunConfig::getBehindLosingTimeColor, (cfg, v) -> cfg.setBehindLosingTimeColor((int)(long) v)),
    COLOR_BEST_SEGMENT(SpeedrunConfig::getBestSegmentColor, (cfg, v) -> cfg.setBestSegmentColor((int)(long) v)),
    COLOR_NOT_RUNNING(SpeedrunConfig::getNotRunningColor, (cfg, v) -> cfg.setNotRunningColor((int)(long) v)),
    COLOR_PAUSED(SpeedrunConfig::getPausedColor, (cfg, v) -> cfg.setPausedColor((int)(long) v)),
    COLOR_PERSONAL_BEST(SpeedrunConfig::getPersonalBestColor, (cfg, v) -> cfg.setPersonalBestColor((int)(long) v)),
    COLOR_CURRENT_SEGMENT(SpeedrunConfig::getCurrentSegmentColor, (cfg, v) -> cfg.setCurrentSegmentColor((int)(long) v)),
    SCALE(SpeedrunConfig::getScale, (cfg, v) -> cfg.setScale((int)(long) v)),
    RAINBOW_BEST(SpeedrunConfig::isRainbowBestSegment, (cfg, v) -> cfg.setRainbowBestSegment((boolean) v)),
    COMPARISON(SpeedrunConfig::getComparison, (cfg, v) -> cfg.setComparison(TimerComparison.valueOf(v.toString())));

    private final Function<SpeedrunConfig, Object> getValueFunc;
    private final BiConsumer<SpeedrunConfig, Object> setValueFunc;

    SpeedrunConfigValues(Function<SpeedrunConfig, Object> getValueFunc, BiConsumer<SpeedrunConfig, Object> setValueFunc) {
        this.getValueFunc = getValueFunc;
        this.setValueFunc = setValueFunc;
    }

    public void setValue(SpeedrunConfig cfg, Object value) {
        if(value instanceof Number) value = ((Number) value).longValue();
        setValueFunc.accept(cfg, value);
    }

    public Object get(SpeedrunConfig cfg) {
        return getValueFunc.apply(cfg);
    }

    public String translateName() {
        return Message.translate("speedrun.setting." + name().toLowerCase(Locale.ROOT));
    }

    public String translateDescription() {
        return Message.translate("speedrun.setting." + name().toLowerCase(Locale.ROOT) + ".desc");
    }
}
