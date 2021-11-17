package dev.peterhinch.assessmenttask2.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.peterhinch.assessmenttask2.room.entities.Record;

@Dao
public interface RecordDao {
  // Create
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertRecords(Record... records);

  // Update
  @Update
  void updateRecords(Record... records);

  // Delete
  @Delete
  void deleteRecords(Record... records);

  // Delete All
  @Query("DELETE FROM record")
  void clearTable();

  // Read All
  @Query("SELECT * FROM record")
  List<Record> getAllRecords();

  // Read one by ID
  @Query("SELECT * FROM record WHERE id = :recordId")
  Record getRecordById(int recordId);

  // Find record by heading
  @Query("SELECT id FROM record WHERE heading = :heading")
  int findRecordByHeading(String heading);
}
