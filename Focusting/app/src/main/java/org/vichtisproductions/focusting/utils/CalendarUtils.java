package org.vichtisproductions.focusting.utils;

import java.util.Random;

/**
 * Created by lauripal on 28.01.2018.
 */

public class CalendarUtils {

    public static int getAttendeeCount() {
        Random r = new Random();
        int min = 1;
        int max = 3;
        int attendees = r.nextInt(max - min + 1) + min;
        return attendees;
    }
}
