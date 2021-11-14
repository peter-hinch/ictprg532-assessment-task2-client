package dev.peterhinch.assessmenttask2.room.entities;

import android.util.Log;

import androidx.room.TypeConverter;

import java.util.Date;

// SQLite and Room do not have a datatype that can contain a Date object by
// default. The solution for this is to create a class that converts Date to
// Long and vice versa.
// Reference: https://stackoverflow.com/questions/50313525/room-using-date-field
// Reference: https://developer.android.com/training/data-storage/room/referencing-data
public class Converters {
    private static final String TAG = "Converters";

    @TypeConverter
    public static Date fromTimestamp(Long longDate) {
        return longDate == null ? null : new Date(longDate);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
