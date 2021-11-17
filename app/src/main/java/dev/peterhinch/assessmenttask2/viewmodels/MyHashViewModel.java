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
import dev.peterhinch.assessmenttask2.room.LocalRecordDb;
import dev.peterhinch.assessmenttask2.room.entities.Record;

public class MyHashViewModel extends ViewModel
    implements RetrofitServices.ResultsHandler {

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
    // Adjust to suit display order.
    recyclerViewRecords = myHash.toList(order);

    // Filter to match search query.
    if ((filterQuery != null) && (!filterQuery.equals(""))) {
      recyclerViewRecords = myHash.filter(filterQuery);
    }

    return recyclerViewRecords;
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
      try {
        RetrofitServices.getInstance().recordReadAll(this);
      } catch (Exception ex) {
        Log.d(TAG, "Problem when attempting to read records from remote " +
            "database: " + ex);
      }
      reHashRecords();
    } catch (Exception ex) {
      Log.d(TAG, "Exception occurred while reading in records: " + ex);
    }
  }

  public void reHashRecords() {
    myHash = new MyHash();
    myHash.buildHashTable(recyclerViewRecords);
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
    Log.d(TAG, returnedRecordList.size() +
        " records received by ListActivity ReadAllOnResponseHandler.");
    recyclerViewRecords.clear();
    for (Record r : returnedRecordList) {
      recyclerViewRecords.add(r);
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
      Log.d(TAG, "Failure received by OnFailureHandler: There is an issue " +
          "with the connection.");
    } else {
      Log.d(TAG, "Failure received by OnFailureHandler: " + throwable.toString());
    }
  }
}
