package com.armpits.nice.calendar;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;

public class CalendarHandler {

    private static final String CALENDAR_URI = "content://com.android.calendar/calendars";
    private static final String CALENDAR_EVENT_URI = "content://com.android.calendar/events";
    private static final String CALENDAR_REMINDER_URI = "content://com.android.calendar/reminders";

    private static final String CALENDAR_USER = "nICE";
    private static final String CALENDAR_ACCOUNT = "nice@armpits.com";
    private static final String CALENDAR_ACC_TYPE = "com.armpits";

    private static int checkAndAddCalendarAccount(Context context) {
        int oldId = checkCalendarAccount(context);
        if( oldId >= 0 ){
            return oldId;
        }else{
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                return checkCalendarAccount(context);
            } else {
                return -1;
            }
        }
    }

    private static int checkCalendarAccount(Context context) {
        Cursor userCursor = context.getContentResolver().query(Uri.parse(CALENDAR_URI), null, null, null, null);
        try {
            if (userCursor == null) {
                return -1;
            }
            int count = userCursor.getCount();
            if (count > 0) {
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }

    private static long addCalendarAccount(Context context) {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDAR_USER);
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDAR_ACCOUNT);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDAR_ACC_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_USER);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDAR_USER);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(CALENDAR_URI);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDAR_USER)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDAR_ACC_TYPE)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    /**
     *
     * @param context context.
     * @param title Event title, please pass due title to this param.
     * @param description Description, please pass module title to this param.
     * @param reminderTime Time, long, milliseconds.
     */
    public static void addCalendarEvent(Context context, String title, String description, long reminderTime) {
        if (context == null) {
            return;
        }
        int calId = checkAndAddCalendarAccount(context);
        if (calId < 0) {
            return;
        }

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(reminderTime);
        long start = mCalendar.getTime().getTime();
        mCalendar.setTimeInMillis(start + 10 * 60 * 1000);
        long end = mCalendar.getTime().getTime();
        ContentValues event = new ContentValues();
        event.put("title", title);
        event.put("description", description);
        event.put("calendar_id", calId);
        event.put(CalendarContract.Events.DTSTART, start);
        event.put(CalendarContract.Events.DTEND, end);
        event.put(CalendarContract.Events.HAS_ALARM, 1);
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");
        Uri newEvent = context.getContentResolver().insert(Uri.parse(CALENDAR_EVENT_URI), event);
        if (newEvent == null) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
        values.put(CalendarContract.Reminders.MINUTES, 24 * 60); // remind 1 day before ddl
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = context.getContentResolver().insert(Uri.parse(CALENDAR_REMINDER_URI), values);
        if(uri == null) {
            return;
        }
    }

    /**
     * Delete event
     */
    public static void deleteCalendarEvent(Context context,String title) {
        if (context == null) {
            return;
        }
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDAR_EVENT_URI), null, null, null, null);
        try {
            if (eventCursor == null) {
                return;
            }
            if (eventCursor.getCount() > 0) {
                for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
                    String eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
                    if (!TextUtils.isEmpty(title) && title.equals(eventTitle)) {
                        int id = eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID));
                        Uri deleteUri = ContentUris.withAppendedId(Uri.parse(CALENDAR_EVENT_URI), id);
                        int rows = context.getContentResolver().delete(deleteUri, null, null);
                        if (rows == -1) {
                            return;
                        }
                    }
                }
            }
        } finally {
            if (eventCursor != null) {
                eventCursor.close();
            }
        }
    }
}
