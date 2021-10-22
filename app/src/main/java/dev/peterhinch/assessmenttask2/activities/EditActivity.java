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
import dev.peterhinch.assessmenttask2.room.RecordDb;
import dev.peterhinch.assessmenttask2.room.entities.Record;

public class EditActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Set the click function for the 'Update' button.
        updateClick();
    }

    // Process the 'Update' button event.
    private void updateClick() {
        Button btnUpdate = findViewById(R.id.edit_button_update);
        btnUpdate.setOnClickListener(view -> {
            Log.d(TAG, "Update button pressed.");

            EditText editTextHeading = (EditText)findViewById(R.id.edit_editText_heading);
            EditText editTextDescription = (EditText)findViewById(R.id.edit_editText_description);
            EditText editTextPhone = (EditText)findViewById(R.id.edit_editText_phone);
            EditText editTextDate = (EditText)findViewById(R.id.edit_editText_date);

            // Add a SimpleDateFormat
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("en_AU"));

            try {
                // Create a new Record object with text field strings.
                Record updatedRecord = new Record(
                        editTextHeading.getText().toString(),
                        editTextDescription.getText().toString(),
                        editTextPhone.getText().toString(),
                        dateFormat.parse(editTextDate.getText().toString())
                );

                // Update the record in the database.
                RecordDb.updateRecord(this, updatedRecord);
            }
            catch(Exception ex) {
                Log.e(TAG, "Failed to update record.");
            }

            // Return to the list activity.
            Intent intent = new Intent(EditActivity.this, ListActivity.class);
            startActivity(intent);
        });
    }
}