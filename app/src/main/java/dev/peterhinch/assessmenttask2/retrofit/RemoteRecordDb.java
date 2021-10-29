package dev.peterhinch.assessmenttask2.retrofit;

import java.util.List;

import dev.peterhinch.assessmenttask2.room.entities.Record;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RemoteRecordDb {
    @POST("Records")
    Call<Record> RecordCreate(@Body Record record);

    @GET("Records")
    Call<List<Record>> RecordAll();

    @GET("Records/{id}")
    Call<Record> Record(@Path("id") int id);

    @PUT("Records/{id}")
    Call<Void> RecordUpdate(@Path("id") int id, @Body Record record);

    @DELETE("Records/{id}")
    Call<Record>  RecordDelete(@Path("id") int id);
}
