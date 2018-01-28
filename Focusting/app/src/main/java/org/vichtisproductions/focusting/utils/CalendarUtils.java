package org.vichtisproductions.focusting.utils;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

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
        // TODO - Here be the real calendar event handlers
        // TODO - Find out what event is going on right now
        // TODO - Get # of Attendees from the ongoing event(s)
        // THIS EXCLUDES RECURRING EVENTS
        Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI
                .buildUpon();
        ContentUris.appendId(eventsUriBuilder, timeNow);
        ContentUris.appendId(eventsUriBuilder, endOfToday);
        Uri eventsUri = eventsUriBuilder.build();
        Cursor cursor = null;
        cursor = mContext.getContentResolver().query(eventsUri, columns, null, null, CalendarContract.Instances.DTSTART + " ASC");

        return attendees;
    }
}
