package dev.peterhinch.assessmenttask2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import dev.peterhinch.assessmenttask2.R;

public class DetailActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Retrieve the data passed in with the bundle.
        Bundle bundle = getIntent().getExtras();

        // Define the EditText views within the details activity.
        EditText editTextHeading = (EditText)findViewById(R.id.detail_editText_heading);
        EditText editTextDescription = (EditText)findViewById(R.id.detail_editText_description);
        EditText editTextPhone = (EditText)findViewById(R.id.detail_editText_phone);
        EditText editTextDate = (EditText)findViewById(R.id.detail_editText_date);

        // Populate the EditText views with information passed in with the bundle.
        editTextHeading.setText(bundle.getString("heading"));
        editTextDescription.setText(bundle.getString("description"));
        editTextPhone.setText(bundle.getString("phone"));
        editTextDate.setText(bundle.getString("date"));

        // Set the click function for the 'Back' button.
        backClick();
    }

    // Process the 'Back' button event.
    private void backClick() {
        Button btnBack = findViewById(R.id.detail_button_back);
        btnBack.setOnClickListener(view -> {
            Log.d(TAG, "Back button pressed.");

            // Return to the list activity.
            Intent intent = new Intent(DetailActivity.this, ListActivity.class);
            startActivity(intent);
        });
    }
}