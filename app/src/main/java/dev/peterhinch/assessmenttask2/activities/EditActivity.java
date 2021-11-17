package dev.peterhinch.assessmenttask2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Locale;

import dev.peterhinch.assessmenttask2.R;
import dev.peterhinch.assessmenttask2.room.LocalRecordDb;
import dev.peterhinch.assessmenttask2.room.entities.Record;

public class EditActivity extends AppCompatActivity {
  private final String TAG = this.getClass().getSimpleName();

  EditText editTextHeading;
  EditText editTextDescription;
  EditText editTextPhone;
  EditText editTextDate;

  // The record object to be displayed and updated.
  Record recordToUpdate = null;

  // String pattern and date format for date display.
  // Date format for date display.
  private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("en_AU"));

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit);

    // Retrieve the data passed in with the bundle.
    Bundle bundle = getIntent().getExtras();

    // Define the EditText views within the details activity.
    editTextHeading = findViewById(R.id.edit_editText_heading);
    editTextDescription = findViewById(R.id.edit_editText_description);
    editTextPhone = findViewById(R.id.edit_editText_phone);
    editTextDate = findViewById(R.id.edit_editText_date);

    // Populate the EditText views with information passed in with the bundle.
    try {
      int recordId = bundle.getInt("id");
      recordToUpdate = LocalRecordDb.recordReadOne(this, recordId);
      editTextHeading.setText(recordToUpdate.getHeading());
      editTextDescription.setText(recordToUpdate.getDescription());
      editTextPhone.setText(recordToUpdate.getPhone());
      editTextDate.setText(simpleDateFormat.format(recordToUpdate.getDate()));
    } catch (Exception ex) {
      Log.d(TAG, "There was an issue reading the record in from local " +
          "database: " + ex);
    }

    // Set the click function for the 'Update' button.
    updateClick();
  }

  // Process the 'Update' button event.
  private void updateClick() {
    Button btnUpdate = findViewById(R.id.edit_button_update);
    btnUpdate.setOnClickListener(view -> {
      Log.d(TAG, "Update button pressed.");

      editTextHeading = findViewById(R.id.edit_editText_heading);
      editTextDescription = findViewById(R.id.edit_editText_description);
      editTextPhone = findViewById(R.id.edit_editText_phone);
      editTextDate = findViewById(R.id.edit_editText_date);

      try {
        // Create a new Record object with text field strings.
        recordToUpdate.setHeading(editTextHeading.getText().toString());
        recordToUpdate.setDescription(editTextDescription.getText().toString());
        recordToUpdate.setPhone(editTextPhone.getText().toString());
        recordToUpdate.setDate(simpleDateFormat.parse(editTextDate.getText().toString()));

        // Update the record in the database.
        LocalRecordDb.recordUpdate(this, recordToUpdate);
      }
      catch(Exception ex) {
        Log.e(TAG, "Failed to update record:" + ex);
      }

      // Return to the list activity.
      Intent intent = new Intent(EditActivity.this, ListActivity.class);
      startActivity(intent);
    });
  }
}