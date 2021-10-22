package dev.peterhinch.assessmenttask2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dev.peterhinch.assessmenttask2.R;
import dev.peterhinch.assessmenttask2.room.RecordDb;
import dev.peterhinch.assessmenttask2.room.entities.Record;

public class AddActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Set the click function for the Add button
        addClick();
    }

    // TODO - Add create functionality
    private void addClick(){
        Button btnAdd = findViewById(R.id.add_button_add);
        btnAdd.setOnClickListener(view -> {
            Log.d(TAG, "Add button pressed.");

            EditText editTextHeading = (EditText)findViewById(R.id.add_editText_heading);
            EditText editTextDescription = (EditText)findViewById(R.id.add_editText_description);
            EditText editTextPhone = (EditText)findViewById(R.id.add_editText_phone);
            EditText editTextDate = (EditText)findViewById(R.id.add_editText_date);

            // Add a SimpleDateFormat
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("en_AU"));

            try {
                // Create a new Record object with text field strings.
                Record newRecord = new Record(
                        editTextHeading.getText().toString(),
                        editTextDescription.getText().toString(),
                        editTextPhone.getText().toString(),
                        dateFormat.parse(editTextDate.getText().toString())
                );

                // Add the new record to the database.
                RecordDb.addRecord(this, newRecord);
            }
            catch(Exception ex) {
                Log.e(TAG, "Failed to add a new record.");
            }

            // Return to the list activity.
            Intent intent = new Intent(AddActivity.this, ListActivity.class);
            startActivity(intent);
        });
    }
}