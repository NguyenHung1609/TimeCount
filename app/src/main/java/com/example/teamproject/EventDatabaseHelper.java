package com.example.teamproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EventDatabaseHelper extends SQLiteOpenHelper {

    public static final String COLUMN_TYPE = "dateType";
    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DATE_TYPE = "dateType";
    public static final String COLUMN_IMAGE_URI = "imageUri";

    public EventDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_EVENTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                COLUMN_DATE_TYPE + " TEXT, " +
                COLUMN_IMAGE_URI + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    public long insertEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, event.getTitle());
        values.put(COLUMN_DATE, event.getDate());
        values.put(COLUMN_DATE_TYPE, event.getDateType());
        values.put(COLUMN_IMAGE_URI, event.getImageUri());
        long id = db.insert(TABLE_EVENTS, null, values);
        db.close();
        return id;
    }

    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENTS, null, null, null, null, null, COLUMN_DATE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                String dateType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_TYPE));
                String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URI));

                long millis = 0;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date dateObj = sdf.parse(date);
                    if (dateObj != null) millis = dateObj.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Event event = new Event(id, title, date, dateType, imageUri, millis);
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventList;
    }

    //Xóa SK
    public void deleteEventById(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, COLUMN_ID + "=?", new String[]{String.valueOf(eventId)});
        db.close();
    }
}
