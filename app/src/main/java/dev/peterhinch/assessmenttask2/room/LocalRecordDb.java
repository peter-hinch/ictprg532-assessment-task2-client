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

public abstract class LocalRecordDb extends RoomDatabase {
  private static final String TAG = "LocalRecordDb";

  public abstract RecordDao recordDao();

  private static LocalRecordDb localRecordDb;

  // The code pattern for the room database is similar to that of a singleton.
  private static LocalRecordDb getInstance(final Context context) {
    if (localRecordDb == null) {
      localRecordDb = Room.databaseBuilder(
          context.getApplicationContext(),
          LocalRecordDb.class,
          "record_room.db").allowMainThreadQueries().build();
    }
    return localRecordDb;
  }

  // Add a Record to the local Room database.
  public static void recordCreate(final Context context, Record record) {
    try {
      LocalRecordDb db = getInstance(context);
      db.recordDao().insertRecords(record);
    } catch (Exception ex) {
      Log.d(TAG, "Exception occurred while adding record: " + ex);
    }
  }

  // Find a Record with the specified id in the local Room database.
  public static Record recordReadOne(final Context context, int id) {
    Record record = null;

    try {
      LocalRecordDb db = getInstance(context);
      record = db.recordDao().getRecordById(id);
    } catch (Exception ex) {
      Log.d(TAG, "Exception occurred while finding record: " + ex);
    }

    return record;
  }

  // Read all Records from the local Room database.
  public static List<Record> recordReadAll(Context context) {
    List<Record> recordList = null;

    try {
      LocalRecordDb db = getInstance(context);
      recordList = db.recordDao().getAllRecords();
    } catch (Exception ex) {
      Log.d(TAG, "Exception occurred while reading records: " + ex);
    }

    return recordList;
  }

  // Update a Record in the local Room database.
  public static void recordUpdate(final Context context, Record record) {
    try {
      LocalRecordDb db = getInstance(context);
      db.recordDao().updateRecords(record);
    } catch (Exception ex) {
      Log.d(TAG, "Exception occurred while updating record: " + ex);
    }
  }

  // Delete a Record from the local Room database.
  public static void recordDelete(final Context context, int recordId) {
    try {
      LocalRecordDb db = getInstance(context);
      Record recordToDelete = db.recordDao().getRecordById(recordId);
      db.recordDao().deleteRecords(recordToDelete);
    } catch (Exception ex) {
      Log.d(TAG, "Exception occurred while deleting record: " + ex);
    }
  }

  // Delete all Records from the local Room database.
  public static void deleteAllRecords(final Context context) {
    try {
      LocalRecordDb db = getInstance(context);
      db.recordDao().clearTable();
    } catch (Exception ex) {
      Log.d(TAG, "Exception occurred while deleting all records: " + ex);
    }
  }
}
