package eu.beezig.core.util;

import java.time.Instant;
import java.util.Date;

public class DateUtils {
    @SuppressWarnings("JdkObsolete")
    public static Instant toInstant(Date date) {
        return date.toInstant();
    }

    @SuppressWarnings("JdkObsolete")
    public static int instantCompare(Date date1, Date date2) {
        return date1.toInstant().compareTo(date2.toInstant());
    }

    @SuppressWarnings("JdkObsolete")
    public static Date toDate(Instant instant) {
        return Date.from(instant);
    }
}
