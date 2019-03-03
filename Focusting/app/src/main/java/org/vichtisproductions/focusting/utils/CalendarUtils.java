package org.vichtisproductions.focusting.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CalendarUtils {

    private static Set<Long> getCurrentlyRunningEventIds(final Context context) {
        String currentTime = String.valueOf(System.currentTimeMillis());

        String[] projection = new String[]{
                CalendarContract.Instances.EVENT_ID};

        String selection = "( " +
                CalendarContract.Instances.BEGIN + " <= ? ) AND ( " +
                CalendarContract.Instances.END + " >= ?)";
        String[] selectionArgs = new String[]{currentTime, currentTime};

        // Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, 0);
        ContentUris.appendId(builder, Long.MAX_VALUE);

        ContentResolver resolver = context.getContentResolver();
        final Cursor cursor = resolver.query(builder.build(), projection, selection, selectionArgs, null);

        Set<Long> ids = new HashSet<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(CalendarContract.Instances.EVENT_ID));
                ids.add(id);
            }
            cursor.close();
        }

        return ids;
    }

    private static Map<Long, Integer> getAttendeesForEvents(Set<Long> eventIds, final Context context) {
        final ContentResolver resolver = context.getContentResolver();

        String[] projection = new String[]{
                CalendarContract.Attendees.EVENT_ID};

        Map<Long, Integer> mapping = new HashMap<>();

        for (Long eventId : eventIds) {
            Cursor cursor = CalendarContract.Attendees.query(resolver, eventId, projection);
            if (cursor != null) {

                int attendeeCount = cursor.getCount();
                // When there's no attendee list, then the host is the only one participating
                if (attendeeCount == 0) {
                    attendeeCount = 1;
                }
                mapping.put(eventId, attendeeCount);

                cursor.close();
            }
        }

        return mapping;
    }

    public static int getAttendeeCount(Context mContext) {
        int attendeesMax = 0;

        Set<Long> eventIds = getCurrentlyRunningEventIds(mContext);
        Map<Long, Integer> eventAttendeeMap = getAttendeesForEvents(eventIds, mContext);

        // Find the current event that has the most attendees
        for (int attendees : eventAttendeeMap.values()) {
            if (attendees > attendeesMax) {
                attendeesMax = attendees;
            }
        }

        return attendeesMax;
    }
}
