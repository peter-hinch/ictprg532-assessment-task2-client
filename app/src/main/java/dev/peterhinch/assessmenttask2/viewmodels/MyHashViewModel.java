package dev.peterhinch.assessmenttask2.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.peterhinch.assessmenttask2.lib.MyHash;
import dev.peterhinch.assessmenttask2.retrofit.RetrofitServices;
import dev.peterhinch.assessmenttask2.room.RecordDb;
import dev.peterhinch.assessmenttask2.room.entities.Record;

public class MyHashViewModel extends ViewModel
        implements RetrofitServices.ResultsHandler  {

    private final String TAG = this.getClass().getSimpleName();
    public MyHash myHash;

    // Constants for sort direction.
    public static final boolean SORT_ASC = false;
    public static final boolean SORT_DESC = true;

    private ArrayList<Record> localRecordList;
    private ArrayList<Record> serverRecordList;

    public ArrayList<Record> getRecords(Context context, boolean order, String filterQuery) {
        if (localRecordList == null) {
            localRecordList = new ArrayList<>();
            loadRecords(context);
        }
        // Filter to match search query.
        if (!filterQuery.equals("")) {
            myHash.filter(filterQuery);
        }
        // Adjust to suit display order.
        localRecordList = myHash.toList(order);
        return localRecordList;
    }

    public void addRecord(Context context, Record newRecord) {
        RecordDb.addRecord(context, newRecord);
    }

    public void updateRecord(Context context, int index, Record updatedRecord) {
        RecordDb.updateRecord(context, updatedRecord);
    }

    public void removeRecord(Context context, int index) {
        //RecordDb.deleteRecord(context, record);
    }

    public void clearRecords(Context context) {
        RecordDb.deleteAllRecords(context);
    }

    private void loadRecords(Context context) {
        // Retrieve data from the database and hash that data.
        try {
            RetrofitServices.getInstance().RecordReadAll(this);
            localRecordList = (ArrayList<Record>) RecordDb.readAllRecords(context);
         } catch (Exception ex) {
            Log.d(TAG, "Exception occurred while reading in records: " + ex);
        }
        myHash = new MyHash();
        myHash.buildHashTable(localRecordList);
    }

    @Override
    public void CreateOnResponseHandler(Record returnedRecord) {
        Log.d(TAG, returnedRecord + " received by ListActivity CreateOnResponseHandler.");
    }

    @Override
    public void ReadOneOnResponseHandler(Record returnedRecord) {
        Log.d(TAG, returnedRecord + " received by ListActivity ReadOneOnResponseHandler.");
    }

    @Override
    public void ReadAllOnResponseHandler(@NonNull List<Record> returnedRecordList) {
        Log.d(TAG, returnedRecordList.size() + " records received by ListActivity ReadAllOnResponseHandler.");
        for (Record r : returnedRecordList) {
            RecordDb.addRecord(null, r);
        }
    }

    @Override
    public void UpdateOnResponseHandler() {
        Log.d(TAG, "Update received by ListActivity UpdateOnResponseHandler.");
    }

    @Override
    public void DeleteOnResponseHandler(Record returnedRecord) {
        Log.d(TAG, returnedRecord + " received by ListActivity DeleteOnResponseHandler.");
    }

    @Override
    public void OnFailureHandler(Throwable throwable) {
        if (throwable instanceof IOException) {
            Log.d(TAG, "Failure received by OnFailureHandler: There is an issue with the connection.");
        } else {
            Log.d(TAG, "Failure received by OnFailureHandler: " + throwable.toString());
        }
    }
}
