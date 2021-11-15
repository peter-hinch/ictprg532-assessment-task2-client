package dev.peterhinch.assessmenttask2.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import dev.peterhinch.assessmenttask2.lib.MyHash;
import dev.peterhinch.assessmenttask2.room.LocalRecordDb;
import dev.peterhinch.assessmenttask2.room.entities.Record;

public class MyHashViewModel extends ViewModel {

    private final String TAG = this.getClass().getSimpleName();

    // Constants for sort direction.
    public static final boolean SORT_ASC = false;
    public static final boolean SORT_DESC = true;

    public MyHash myHash;
    private ArrayList<Record> recyclerViewRecords;

    public ArrayList<Record> getRecords(Context context, boolean order, String filterQuery) {
        if (myHash == null) {
            recyclerViewRecords = new ArrayList<>();
            loadRecords(context);
        }
        // Filter to match search query.
        if (!filterQuery.equals("")) {
            recyclerViewRecords = myHash.filter(filterQuery);
        }
        // Adjust to suit display order.
        recyclerViewRecords = myHash.toList(order);
        return recyclerViewRecords;
    }

    public void reHashRecords() {
        myHash = new MyHash();
        myHash.buildHashTable(recyclerViewRecords);
    }

    public void recordUpdate(int recordPosition, Record updatedRecord) {
        recyclerViewRecords.remove(recordPosition);
        recyclerViewRecords.add(recordPosition, updatedRecord);
        reHashRecords();
    }

    public void recordDelete(int recordPosition) {
        recyclerViewRecords.remove(recordPosition);
        reHashRecords();
    }

    private void loadRecords(Context context) {
        // Retrieve data from the database and hash that data.
        try {
            recyclerViewRecords = (ArrayList<Record>) LocalRecordDb.recordReadAll(context);
            reHashRecords();
         } catch (Exception ex) {
            Log.d(TAG, "Exception occurred while reading in records: " + ex);
        }
    }
}
