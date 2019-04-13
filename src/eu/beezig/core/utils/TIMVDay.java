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

package eu.beezig.core.utils;

import eu.beezig.core.BeezigMain;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class TIMVDay {


    public static Long parseCalendar(Calendar c) {
        return Long.parseLong(c.get(Calendar.YEAR) + "" + (c.get(Calendar.MONTH) + 1) + c.get(Calendar.DAY_OF_MONTH));
    }

    public static String fromCalendar(Calendar c) {
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
    }


    public static boolean containsDayfile(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String fileName = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
        File path = new File(BeezigMain.mcFile + "/timv/dailykarma/" + fileName);

        return path.exists();
    }

    public static boolean containsDayfile(Date date, String mode) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String fileName = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
        File path = new File(BeezigMain.mcFile + "/" + mode.toLowerCase() + "/dailykarma/" + fileName);

        return path.exists();
    }


}
