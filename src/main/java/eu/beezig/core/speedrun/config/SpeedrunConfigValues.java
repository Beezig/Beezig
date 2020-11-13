package eu.beezig.core.speedrun.config;

import eu.beezig.core.util.text.Message;

import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("ImmutableEnumChecker")
public enum SpeedrunConfigValues {
    COLOR_DEFAULT(SpeedrunConfig::getDefaultColor, (cfg, v) -> cfg.setDefault((int)(long) v)),
    COLOR_AHEAD_GAINING(SpeedrunConfig::getAheadGainingTime, (cfg, v) -> cfg.setAheadGainingTime((int)(long) v)),
    COLOR_AHEAD_LOSING(SpeedrunConfig::getAheadLosingTime, (cfg, v) -> cfg.setAheadLosingTime((int)(long) v)),
    COLOR_BEHIND_GAINING(SpeedrunConfig::getBehindGainingTime, (cfg, v) -> cfg.setBehindGainingTime((int)(long) v)),
    COLOR_BEHIND_LOSING(SpeedrunConfig::getBehindLosingTime, (cfg, v) -> cfg.setBehindLosingTime((int)(long) v)),
    COLOR_BEST_SEGMENT(SpeedrunConfig::getBestSegment, (cfg, v) -> cfg.setBestSegment((int)(long) v)),
    COLOR_NOT_RUNNING(SpeedrunConfig::getNotRunning, (cfg, v) -> cfg.setNotRunning((int)(long) v)),
    COLOR_PAUSED(SpeedrunConfig::getPaused, (cfg, v) -> cfg.setPaused((int)(long) v)),
    COLOR_PERSONAL_BEST(SpeedrunConfig::getPersonalBest, (cfg, v) -> cfg.setPersonalBest((int)(long) v)),
    COLOR_CURRENT_SEGMENT(SpeedrunConfig::getCurrentSegment, (cfg, v) -> cfg.setCurrentSegment((int)(long) v));

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
