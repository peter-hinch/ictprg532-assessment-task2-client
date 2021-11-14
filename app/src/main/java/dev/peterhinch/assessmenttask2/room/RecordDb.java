package dev.peterhinch.assessmenttask2.room;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.List;

import dev.peterhinch.assessmenttask2.room.entities.Converters;
import dev.peterhinch.assessmenttask2.room.entities.Record;
import dev.peterhinch.assessmenttask2.room.dao.RecordDao;

@Database(
        entities = {Record.class},
        version = 1,
        exportSchema = false
)
// This is where any necessary type converters can be declared (e.g. Date to Long)
// Reference: https://developer.android.com/training/data-storage/room/referencing-data
@TypeConverters({Converters.class})

public abstract class RecordDb extends RoomDatabase {
    private static final String TAG = "RecordDb";

    public abstract RecordDao recordDao();

    private static RecordDb recordDb;

    // The code pattern for the room database is similar to that of a singleton.
    public static RecordDb getInstance(final Context context) {
        if (recordDb == null) {
            recordDb = Room.databaseBuilder(
                    context.getApplicationContext(),
                    RecordDb.class,
                    "record_room.db").allowMainThreadQueries().build();
        }
        return recordDb;
    }

    // Initialise data in the local Room database.
    public static int initData(final Context context, List<Record> records) {
        RecordDb db = getInstance(context);

        try {

        }
        catch (Exception ex) {
            Log.e(TAG, "Unable to initialise database.");
        }

        return db.recordDao().getAllRecords().size();
    }

    // Add a Record to the local Room database.
    public static void addRecord(final Context context, Record record) {
        RecordDb db = getInstance(context);
        db.recordDao().insertRecords(record);
    }

    // Update a Record in the local Room database.
    public static void updateRecord(final Context context, Record record) {
        RecordDb db = getInstance(context);
        db.recordDao().updateRecords(record);
    }

    // Find a Record with the specified id in the local Room database.
    public static Record findRecordById(final Context context, int id) {
        Record record;
        RecordDb db = getInstance(context);
        record = db.recordDao().getRecordById(id);
        return record;
    }

    // Delete a Record from the local Room database.
    public static void deleteRecord(final Context context, Record record) {
        RecordDb db = getInstance(context);
        db.recordDao().deleteRecords(record);
    }

    // Delete all Records from the local Room database.
    public static void deleteAllRecords(final Context context) {
        RecordDb db = getInstance(context);
        db.recordDao().clearTable();
    }
}
