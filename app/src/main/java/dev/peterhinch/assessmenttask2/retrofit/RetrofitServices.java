package dev.peterhinch.assessmenttask2.retrofit;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import dev.peterhinch.assessmenttask2.room.entities.Record;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServices {
  private final String TAG = this.getClass().getSimpleName();

  // Static variable to define retrofitServicesInstance, which will be a singleton.
  private static RetrofitServices retrofitServicesInstance = null;
  private static RemoteRecordDb service;

  // Private constructor only accessible from this class.
  private RetrofitServices() {
    // GsonBuilder .setDateFormat() is used to specify the date format.
    // Reference: https://futurestud.io/tutorials/retrofit-2-adding-customizing-the-gson-converter
    Gson gson = new GsonBuilder()
        .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss")
        .create();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://192.168.65.1:44327/api/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
    service = retrofit.create(RemoteRecordDb.class);
  }

  // Static method to retrieve a RetrofitServices instance.
  public static RetrofitServices getInstance() {
    if (retrofitServicesInstance == null) {
      retrofitServicesInstance = new RetrofitServices();
    }
    return retrofitServicesInstance;
  }

  // Create.
  public void recordCreate(@NonNull Record record, final ResultsHandler resultsHandler) {
    // Prepare an API call.
    record.setId(0);
    Call<Record> recordCreate = service.RecordCreate(record);
    // Call the API.
    recordCreate.enqueue(new Callback<Record>() {
      // Call API - with successful results.
      @Override
      public void onResponse(@NonNull Call<Record> call, @NonNull Response<Record> response) {
        try {
          Record record = response.body();
          resultsHandler.createOnResponseHandler(record);
        }
        catch (Exception ex) {
          Log.e(TAG, "An exception occurred: " + ex);
        }
      }
      // Call API - with failed results.
      @Override
      public void onFailure(@NonNull Call<Record> call, @NonNull Throwable t) {
        resultsHandler.onFailureHandler(t);
      }
    });
  }

  // Read one.
  public void recordReadOne(int id, final ResultsHandler resultsHandler) {
    // Prepare an API call.
    Call<Record> recordReadOne = service.Record(id);
    // Call the API.
    recordReadOne.enqueue(new Callback<Record>() {
      // Call API - with successful results.
      @Override
      public void onResponse(@NonNull Call<Record> call, @NonNull Response<Record> response) {
        try {
          Record record = response.body();
          resultsHandler.readOneOnResponseHandler(record);
        }
        catch (Exception ex) {
          Log.e(TAG, "An exception occurred: " + ex);
        }
      }
      // Call API - with failed results.
      @Override
      public void onFailure(@NonNull Call<Record> call, @NonNull Throwable t) {
        resultsHandler.onFailureHandler(t);
      }
    });
  }

  // Read all.
  public void recordReadAll(final ResultsHandler resultsHandler) {
    // Prepare an API call.
    Call<List<Record>> recordReadAll = service.RecordAll();
    // Call the API.
    recordReadAll.enqueue(new Callback<List<Record>>() {
      // Call API - with successful results.
      @Override
      public void onResponse(@NonNull Call<List<Record>> call,
                             @NonNull Response<List<Record>> response) {
        try {
          List<Record> recordList = response.body();
          resultsHandler.readAllOnResponseHandler(recordList);
        }
        catch (Exception ex) {
          Log.e(TAG, "An exception occurred: " + ex);
        }
      }
      // Call API - with failed results.
      @Override
      public void onFailure(@NonNull Call<List<Record>> call, @NonNull Throwable t) {
        resultsHandler.onFailureHandler(t);
      }
    });
  }

  // Update.
  public void recordUpdate(int id, @NonNull Record record, final ResultsHandler resultsHandler) {
    // Prepare an API call.
    record.setId(id);
    Call<Void> recordUpdate = service.RecordUpdate(id, record);
    // Call the API.
    recordUpdate.enqueue(new Callback<Void>() {
      // Call API - with successful results.
      @Override
      public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
        try {
          resultsHandler.updateOnResponseHandler();
        }
        catch (Exception ex) {
          Log.e(TAG, "An exception occurred: " + ex);
        }
      }
      // Call API - with failed results.
      @Override
      public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
        resultsHandler.onFailureHandler(t);
      }
    });
  }

  // Delete.
  public void recordDelete(int id, final ResultsHandler resultsHandler) {
    // Prepare an API call.
    Call<Record> recordDelete = service.RecordDelete(id);
    // Call the API.
    recordDelete.enqueue(new Callback<Record>() {
      // Call API - with successful results.
      @Override
      public void onResponse(@NonNull Call<Record> call, @NonNull Response<Record> response) {
        try {
          Record record= response.body();
          resultsHandler.deleteOnResponseHandler(record);
        }
        catch (Exception ex) {
          Log.e(TAG, "An exception occurred: " + ex);
        }
      }
      // Call API - with successful results.
      @Override
      public void onFailure(@NonNull Call<Record> call, @NonNull Throwable t) {
        resultsHandler.onFailureHandler(t);
      }
    });
  }

  // Provide an interface for interaction with other classes.
  public interface ResultsHandler {
    void createOnResponseHandler(Record record);
    void readOneOnResponseHandler(Record record);
    void readAllOnResponseHandler(List<Record> recordList);
    void updateOnResponseHandler();
    void deleteOnResponseHandler(Record record);
    void onFailureHandler(Throwable throwable);
  }
}
