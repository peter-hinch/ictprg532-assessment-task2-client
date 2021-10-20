package dev.peterhinch.assessmenttask2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import dev.peterhinch.assessmenttask2.R;

public class AddActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Set the click function for the Add button
        addClick();
    }

    private void addClick(){
        Button btnAdd = findViewById(R.id.add_button_add);
        btnAdd.setOnClickListener(view -> {
            Log.d(TAG, "Add button pressed.");
        });
    }
}