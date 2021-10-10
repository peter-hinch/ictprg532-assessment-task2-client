package dev.peterhinch.assessmenttask2.room;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

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

    // Initialise data in the database.
    public static int initData(final Context context) {
        RecordDb db = getInstance(context);

        // Add a SimpleDateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("en_AU"));

        if(db.recordDao().getAllRecords().size() == 0) {
            try {
                // Add sample data.
                db.recordDao().insertRecords(new Record("Amon", "Description 01",
                        "0123 456 789", dateFormat.parse("01/01/2021")));
                db.recordDao().insertRecords(new Record("Dominik", "Description 02",
                        "0456 789 123", dateFormat.parse("02/02/2021")));
                db.recordDao().insertRecords(new Record("Marlowe", "Description 03",
                        "0789 123 456", dateFormat.parse("03/03/2021")));
                db.recordDao().insertRecords(new Record("Max", "Description 04",
                        "0123 456 789", dateFormat.parse("04/04/2021")));
                db.recordDao().insertRecords(new Record("Luke", "Description 05",
                        "0456 789 123", dateFormat.parse("05/05/2021")));
                db.recordDao().insertRecords(new Record("Jon", "Description 06",
                        "0789 123 456", dateFormat.parse("06/06/2021")));
                db.recordDao().insertRecords(new Record("Keith", "Description 07",
                        "0123 456 789", dateFormat.parse("07/07/2021")));
                db.recordDao().insertRecords(new Record("Zef", "Description 08",
                        "0456 789 123", dateFormat.parse("08/08/2021")));
                db.recordDao().insertRecords(new Record("Azeem", "Description 09",
                        "0789 123 456", dateFormat.parse("09/09/2021")));
                db.recordDao().insertRecords(new Record("Rob", "Description 10",
                        "0123 456 789", dateFormat.parse("10/10/2021")));
                db.recordDao().insertRecords(new Record("Steve", "Description 11",
                        "0456 789 123", dateFormat.parse("11/11/2021")));
                db.recordDao().insertRecords(new Record("Martyn", "Description 12",
                        "0789 123 456", dateFormat.parse("12/12/2021")));
                db.recordDao().insertRecords(new Record("Don", "Description 13",
                        "0123 456 789", dateFormat.parse("01/01/2021")));
                db.recordDao().insertRecords(new Record("Pablo", "Description 14",
                        "0456 789 123", dateFormat.parse("01/01/2021")));
                db.recordDao().insertRecords(new Record("Zackey", "Description 15",
                        "0789 123 456", dateFormat.parse("02/02/2021")));
                db.recordDao().insertRecords(new Record("Mike", "Description 16",
                        "0123 456 789", dateFormat.parse("03/03/2021")));
                db.recordDao().insertRecords(new Record("Edan", "Description 17",
                        "0456 789 123", dateFormat.parse("04/04/2021")));
                db.recordDao().insertRecords(new Record("Stacey", "Description 18",
                        "0789 123 456", dateFormat.parse("05/05/2021")));
                db.recordDao().insertRecords(new Record("Danny", "Description 19",
                        "0123 456 789", dateFormat.parse("06/06/2021")));
                db.recordDao().insertRecords(new Record("David", "Description 20",
                        "0456 789 123", dateFormat.parse("07/07/2021")));
                db.recordDao().insertRecords(new Record("Jojo", "Description 21",
                        "0789 123 456", dateFormat.parse("08/08/2021")));
                db.recordDao().insertRecords(new Record("Jay", "Description 22",
                        "0123 456 789", dateFormat.parse("09/09/2021")));
                db.recordDao().insertRecords(new Record("Isaiah", "Description 23",
                        "0456 789 123", dateFormat.parse("10/10/2021")));
                db.recordDao().insertRecords(new Record("Hugh", "Description 24",
                        "0789 123 456", dateFormat.parse("11/11/2021")));
                db.recordDao().insertRecords(new Record("Talib", "Description 25",
                        "0123 456 789", dateFormat.parse("12/12/2021")));
                db.recordDao().insertRecords(new Record("Anderson", "Description 26",
                        "0456 789 123", dateFormat.parse("01/01/2021")));
                db.recordDao().insertRecords(new Record("Iggy", "Description 27",
                        "0789 123 456", dateFormat.parse("02/02/2021")));
                db.recordDao().insertRecords(new Record("Emily", "Description 28",
                        "0123 456 789", dateFormat.parse("03/03/2021")));
                db.recordDao().insertRecords(new Record("Kanye", "Description 29",
                        "0456 789 123", dateFormat.parse("04/04/2021")));
                db.recordDao().insertRecords(new Record("Marlena", "Description 30",
                        "0789 123 456", dateFormat.parse("05/05/2021")));
            } catch (ParseException parseException) {
                Log.e(TAG, "An exception occurred when parsing date strings.", parseException);
            }
        }
        return db.recordDao().getAllRecords().size();
    }
}
