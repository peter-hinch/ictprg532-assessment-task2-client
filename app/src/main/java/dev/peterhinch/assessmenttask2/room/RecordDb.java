package dev.peterhinch.assessmenttask2.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.Date;

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

    // Initialise data in the database.
    public static int initData(final Context context) {
        RecordDb db = getInstance(context);
        
        if(db.recordDao().getAllRecords().size() == 0) {
            db.recordDao().insertRecords(
                    // Add sample data.
                    // TODO - Dates are currently not being entered correctly, find a solution to convert dates to milliseconds.
                    new Record("Amon", "Description 01", "0123 456 789", new Date(2021)),
                    new Record("Dominik", "Description 02", "0456 789 123", new Date(2021/2/2)),
                    new Record("Marlowe", "Description 03", "0789 123 456", new Date(2021/3/3)),
                    new Record("Max", "Description 04", "0123 456 789", new Date(2021/4/4)),
                    new Record("Luke", "Description 05", "0456 789 123", new Date(2021/5/5)),
                    new Record("Jon", "Description 06", "0789 123 456", new Date(2021/6/6)),
                    new Record("Keith", "Description 07", "0123 456 789", new Date(2021/7/7)),
                    new Record("Zef", "Description 08", "0456 789 123", new Date(2021/8/8)),
                    new Record("Azeem", "Description 09", "0789 123 456", new Date(2021/9/9)),
                    new Record("Rob", "Description 10", "0123 456 789", new Date(2021/10/10)),
                    new Record("Steve", "Description 11", "0456 789 123", new Date(2021/11/11)),
                    new Record("Martyn", "Description 12", "0789 123 456", new Date(2021/12/12)),
                    new Record("Don", "Description 13", "0123 456 789", new Date(2021)),
                    new Record("Pablo", "Description 14", "0456 789 123", new Date(2021/2/2)),
                    new Record("Zackey", "Description 15", "0789 123 456", new Date(2021/3/3)),
                    new Record("Mike", "Description 16", "0123 456 789", new Date(2021/4/4)),
                    new Record("Edan", "Description 17", "0456 789 123", new Date(2021/5/5)),
                    new Record("Stacey", "Description 18", "0789 123 456", new Date(2021/6/6)),
                    new Record("Danny", "Description 19", "0123 456 789", new Date(2021/7/7)),
                    new Record("David", "Description 20", "0456 789 123", new Date(2021/8/8)),
                    new Record("Jojo", "Description 21", "0789 123 456", new Date(2021/9/9)),
                    new Record("Jay", "Description 22", "0123 456 789", new Date(2021/10/10)),
                    new Record("Isaiah", "Description 23", "0456 789 123", new Date(2021/11/11)),
                    new Record("Hugh", "Description 24", "0789 123 456", new Date(2021/12/12)),
                    new Record("Talib", "Description 25", "0123 456 789", new Date(2021)),
                    new Record("Anderson", "Description 26", "0456 789 123", new Date(2021/2/2)),
                    new Record("Iggy", "Description 27", "0789 123 456", new Date(2021/3/3)),
                    new Record("Emily", "Description 28", "0123 456 789", new Date(2021/4/4)),
                    new Record("Kanye", "Description 29", "0456 789 123", new Date(2021/5/5)),
                    new Record("Marlena", "Description 30", "0789 123 456", new Date(2021/6/6))
            );
        }
        return db.recordDao().getAllRecords().size();
    }
}
