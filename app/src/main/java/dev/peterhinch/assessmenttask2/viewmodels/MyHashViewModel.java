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
            myHash.filter(filterQuery);
        }
        // Adjust to suit display order.
        recyclerViewRecords = myHash.toList(order);
        Log.d(TAG, "RecyclerView is holding " + recyclerViewRecords.size() + " values.");
        return recyclerViewRecords;
    }

    public void recordDelete(int recordPosition) {
        recyclerViewRecords.remove(recordPosition);
        Log.d(TAG, "RecyclerView is holding " + recyclerViewRecords.size() + " values.");
    }

    private void loadRecords(Context context) {
        // Retrieve data from the database and hash that data.
        try {
            //RetrofitServices.getInstance().recordReadAll(this);
            recyclerViewRecords = (ArrayList<Record>) LocalRecordDb.recordReadAll(context);
            myHash = new MyHash();
            myHash.buildHashTable(recyclerViewRecords);
         } catch (Exception ex) {
            Log.d(TAG, "Exception occurred while reading in records: " + ex);
        }
    }
}
