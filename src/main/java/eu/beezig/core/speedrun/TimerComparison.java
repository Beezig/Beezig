package eu.beezig.core.speedrun;

import eu.beezig.core.util.text.Message;

import java.util.Locale;

public enum TimerComparison {
    PERSONAL_BEST(0),
    LATEST_RUN(7),
    BEST_SEGMENTS(1),
    WORST_SEGMENTS(5),
    MEDIAN_SEGMENTS(4),
    AVERAGE_SEGMENTS(3),
    BALANCED_PB(6),
    NONE(8);

    private final int enumKey;

    public int getEnumKey() {
        return enumKey;
    }

    TimerComparison(int enumKey) {
        this.enumKey = enumKey;
    }

    public String translate() {
        return Message.translate("speedrun.cmp." + name().toLowerCase(Locale.ROOT));
    }
}
