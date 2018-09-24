package org.vichtisproductions.focusting.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import timber.log.Timber;

public class CalendarUtils {

    public static int getAttendeeCount(Context mContext) {
        // TODO - Here be the real calendar event handlers - FIX THIS
        // Find out what event is going on right now
        // Get # of Attendees from the ongoing event(s)
        // long timeNow= System.currentTimeMillis();
        // Timber.d("Time now: " + String.valueOf(timeNow));
        int attendeesMax=0;
        /*
        String[] projection = new String[]{
                CalendarContract.Instances.EVENT_ID,
                CalendarContract.Instances.BEGIN,
                CalendarContract.Instances.END};

        String selection = "(( " +
                CalendarContract.Instances.BEGIN + " <= ? ) AND ( " +
                CalendarContract.Instances.END + " >= ?))";
        String[] selectionArgs = new String[] { timeNow, timeNow };
        String selection = "((" + CalendarContract.Instances.BEGIN + " >= ?) AND (" + CalendarContract.Instances.END + " <= ? ) AND (" + CalendarContract.Instances.AVAILABILITY + " == ?))";
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, timeNow);
        ContentUris.appendId(builder, timeNow);
        Uri instancesUri = builder.build();
        Cursor cursor = null;
        ContentResolver cr = mContext.getContentResolver();
        Timber.d("Got here - at least.");
        cursor = cr.query(instancesUri, projection, null, null, CalendarContract.Instances.DTSTART + " ASC");
        Timber.d("Query done.");
        cursor.moveToFirst();
        Timber.d("Moved to first.");
        Timber.d("Event #: " + String.valueOf(cursor.getCount()));
        if (cursor.getCount() > 0) {
            Timber.d("Checking attendees...");
            while (cursor.moveToNext()) {
                int eventID = cursor.getInt(0);
                Timber.d("Event id: " + String.valueOf(eventID));
                String[] attendeeProjection= new String[]{
                        CalendarContract.Attendees.EVENT_ID,
                        CalendarContract.Attendees.ATTENDEE_EMAIL,
                        CalendarContract.Attendees.ATTENDEE_TYPE,
                        CalendarContract.Attendees.ATTENDEE_STATUS};
                String attendeeSelection = CalendarContract.Attendees.ATTENDEE_STATUS + " != " + 3 + ")";
                final String query = "(" + CalendarContract.Attendees.EVENT_ID + " = " + eventID + ")";
                final Cursor attendeecursor = mContext.getContentResolver().query(CalendarContract.Attendees.CONTENT_URI, attendeeProjection, attendeeSelection, null, null);
                Timber.d("CalendarUtils - Attendees: " + String.valueOf(attendeecursor.getCount()));
                if (attendeecursor.getCount() > 0) { attendeesMax = attendeecursor.getCount(); }
            }

            */
        Timber.d("Now inside Calendarutils.getAttendeeCount");
        Calendar timeNow = Calendar.getInstance();
        Timber.d("Time now is: " + timeNow.getTimeInMillis());

        // TODO - DO YOU WANT TO STORE CALENDAR EVENT INSTANCE ID TO FIREBARE
        String[] projection = new String[]{
                CalendarContract.Instances._ID,
                CalendarContract.Instances.BEGIN,
                CalendarContract.Instances.END,
                CalendarContract.Instances.EVENT_ID};
        String query = "(" + CalendarContract.Instances.BEGIN + " <= " + timeNow.getTimeInMillis() + " ) AND ( " + CalendarContract.Instances.END + " >= " + timeNow.getTimeInMillis() + ")";
        Timber.d(query);
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(CalendarContract.Instances.CONTENT_URI, projection, query, null, null);
        Timber.d("Content resolver specified");
        // TODO - Here should be a calendar security permission checker
        /* cursor = cr.query(
                CalendarContract.Events.CONTENT_URI,
                projection,
                query,
                null,
                null); */
        Timber.d("Cursor specified");
        Timber.d("Cursor count is: " + String.valueOf(cursor.getCount()));
        if (cursor!=null&&cursor.getCount()>0&&cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                Timber.d("...Inside cursor loop...");
                final String eventID = cursor.getString(0);
                final String[] attendeeProjection = new String[]{
                        CalendarContract.Attendees._ID,
                        CalendarContract.Attendees.EVENT_ID,
                        CalendarContract.Attendees.ATTENDEE_NAME,
                        CalendarContract.Attendees.ATTENDEE_EMAIL,
                        CalendarContract.Attendees.ATTENDEE_TYPE,
                        CalendarContract.Attendees.ATTENDEE_RELATIONSHIP,
                        CalendarContract.Attendees.ATTENDEE_STATUS
                };
                final String query2 = "(" + CalendarContract.Attendees.EVENT_ID + " = " + eventID + ")";
                final Cursor attendeecursor = mContext.getContentResolver().query(CalendarContract.Attendees.CONTENT_URI, attendeeProjection, query2, null, null);
                if (attendeecursor.getCount() > 0) { attendeesMax = attendeecursor.getCount(); }
            }
        } else {
            return 0;
        }

        Random r = new Random();
        int min = 1;
        int max = 3;

        Timber.d("Got to the bottom, returning "+attendeesMax);
        return attendeesMax;
    }
}
