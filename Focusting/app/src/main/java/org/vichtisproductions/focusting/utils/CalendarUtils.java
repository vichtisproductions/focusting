package org.vichtisproductions.focusting.utils;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Random;

public class CalendarUtils {

    public static int getAttendeeCount() {
        Random r = new Random();
        int min = 1;
        int max = 3;

        // TODO - Here be the real calendar event handlers
        // TODO - Find out what event is going on right now
        // TODO - Get # of Attendees from the ongoing event(s)
        /*
        // Read all calendars
        String[] projection =
                new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE};
        Cursor calCursor =
                getContentResolver().
                        query(CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                CalendarContract.Calendars.VISIBLE + " = 1",
                                null,
                                CalendarContract.Calendars._ID + " ASC");
        if (calCursor.moveToFirst()) {
            do {
                long id = calCursor.getLong(0);
                String displayName = calCursor.getString(1);
                // ...
            } while (calCursor.moveToNext());
        }
        */

        return r.nextInt(max - min + 1) + min;
    }
}
