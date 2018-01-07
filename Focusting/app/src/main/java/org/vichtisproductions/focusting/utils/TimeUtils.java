package org.vichtisproductions.focusting.utils;

import java.util.concurrent.TimeUnit;

/**
 * Created by Renier on 2016/04/27.
 */
public class TimeUtils {

    public static String getTimeStringFromMillis(long millis, boolean includeLeadingZeros, boolean includeHoursIfZero, boolean includeMinutesIfZero, boolean limitTwoTimesOnly) {
        String stringFormat = includeLeadingZeros ? "%02d" : "%d";

        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        if (hours > 0) {
            millis -= TimeUnit.HOURS.toMillis(hours);
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        if (minutes > 0) {
            millis -= TimeUnit.MINUTES.toMillis(minutes);
        }

        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sotBuilder = new StringBuilder();

        if (hours > 0 || includeHoursIfZero) {
            sotBuilder.append(String.format(stringFormat, hours) + ":");
        } else {
            sotBuilder.append("   ");
        }

        if (minutes > 0 || includeMinutesIfZero) {
            sotBuilder.append(String.format(stringFormat, minutes) + ":");
        } else {
            sotBuilder.append("   ");
        }

        if ((!limitTwoTimesOnly) || (hours == 0 && limitTwoTimesOnly)) {
            sotBuilder.append(String.format(stringFormat, seconds));
        }

        return sotBuilder.toString();
    }

    public static String getReadableTimeStringFromMillis(long millis, boolean includeLeadingZeros, boolean includeHoursIfZero, boolean includeMinutesIfZero, boolean limitTwoTimesOnly) {
        String stringFormat = includeLeadingZeros ? "%02d" : "%d";

        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        if (hours > 0) {
            millis -= TimeUnit.HOURS.toMillis(hours);
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        if (minutes > 0) {
            millis -= TimeUnit.MINUTES.toMillis(minutes);
        }

        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sotBuilder = new StringBuilder();

        if (hours > 0 || includeHoursIfZero) {
            sotBuilder.append(String.format(stringFormat, hours) + " h");
        }

        if (hours > 0 && (minutes > 0 || includeMinutesIfZero)) {
            sotBuilder.append(" ");
        }

        if (minutes > 0 || includeMinutesIfZero) {
            sotBuilder.append(String.format(stringFormat, minutes) + " m");
        }

        if ((hours > 0 || !includeHoursIfZero)  && (minutes > 0 || !includeMinutesIfZero) && (seconds > 00) && !limitTwoTimesOnly) {
            sotBuilder.append(" ");
        }

        if ((!limitTwoTimesOnly) || (hours == 0 && limitTwoTimesOnly)) {
            if (minutes > 0 || includeMinutesIfZero) {
                sotBuilder.append(" ");
            }
            sotBuilder.append(String.format(stringFormat, seconds) + " s");
        }

        return sotBuilder.toString();
    }
    public static long getHowManyHoursAgo(long comparisonTime) {
        return TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - comparisonTime);

    }
}
