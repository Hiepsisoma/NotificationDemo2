package vn.framgia.phamhung.notificaitondemo2;

import java.util.concurrent.TimeUnit;

public class TimeUtil {
    public static String convertMilisecondToFormatTime(long msec) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(msec) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(msec)),
                TimeUnit.MILLISECONDS.toSeconds(msec) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(msec)));
    }
}
