package dev.peterhinch.assessmenttask2.retrofit;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import dev.peterhinch.assessmenttask2.room.LocalRecordDb;
import dev.peterhinch.assessmenttask2.room.entities.Record;

public class ApiInterface
        implements RetrofitServices.ResultsHandler {
    private final String TAG = this.getClass().getSimpleName();

    public void recordCreate(Record newRecord) {
        try {

        } catch (Exception ex) {
            Log.d("ApiInterface", "Exception occurred while creating record: " + ex);
        }
    }

    public void recordReadOne(int recordId) {
        try {

        } catch (Exception ex) {
            Log.d("ApiInterface", "Exception occurred while reading record: " + ex);
        }
    }

    public void recordReadAll() {
        // Retrieve data from the database and hash that data.
        try {
            RetrofitServices.getInstance().recordReadAll(this);
        } catch (Exception ex) {
            Log.d("ApiInterface", "Exception occurred while reading in records: " + ex);
        }
    }

    public void recordUpdate(int recordId, Record updatedRecord) {
        try {

        } catch (Exception ex) {
            Log.d("ApiInterface", "Exception occurred while updating record: " + ex);
        }
    }

    public void recordDelete(Context context, int recordId) {
        try {
            RetrofitServices.getInstance().recordDelete(recordId, this);
        } catch (Exception ex) {
            Log.d("ApiInterface", "Exception occurred while deleting record: " + ex);
        }
    }

    @Override
    public void createOnResponseHandler(Record returnedRecord) {
        Log.d(TAG, returnedRecord + " received by ListActivity CreateOnResponseHandler.");
    }

    @Override
    public void readOneOnResponseHandler(Record returnedRecord) {
        Log.d(TAG, returnedRecord + " received by ListActivity ReadOneOnResponseHandler.");
    }

    @Override
    public void readAllOnResponseHandler(@NonNull List<Record> returnedRecordList) {
        Log.d(TAG, returnedRecordList.size() + " records received by ListActivity ReadAllOnResponseHandler.");
        for (Record r : returnedRecordList) {
            LocalRecordDb.recordCreate(null, r);
        }
    }

    @Override
    public void updateOnResponseHandler() {
        Log.d(TAG, "Update received by ListActivity UpdateOnResponseHandler.");
    }

    @Override
    public void deleteOnResponseHandler(Record returnedRecord) {
        Log.d(TAG, returnedRecord + " received by ListActivity DeleteOnResponseHandler.");
    }

    @Override
    public void onFailureHandler(Throwable throwable) {
        if (throwable instanceof IOException) {
            Log.d(TAG, "Failure received by OnFailureHandler: There is an issue with the connection.");
        } else {
            Log.d(TAG, "Failure received by OnFailureHandler: " + throwable.toString());
        }
    }
}
