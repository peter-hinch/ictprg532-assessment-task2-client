package dev.peterhinch.assessmenttask2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import dev.peterhinch.assessmenttask2.R;

public class DetailActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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