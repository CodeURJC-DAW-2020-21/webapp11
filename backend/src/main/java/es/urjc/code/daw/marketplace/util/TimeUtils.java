package es.urjc.code.daw.marketplace.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {

    public static Date firstDayOfCurrentWeek() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }

    public static Date sumDaysToDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Date sumsMillisToDate(Date date, int millis) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, millis);
        return calendar.getTime();
    }

    public static Date removeSecondsFromDate(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, -seconds);
        return calendar.getTime();
    }

    public static Date now() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
        return calendar.getTime();
    }

}
