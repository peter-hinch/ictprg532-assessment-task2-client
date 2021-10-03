package dev.peterhinch.assessmenttask2.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

import dev.peterhinch.assessmenttask2.R;
import dev.peterhinch.assessmenttask2.database.RecordDb;
import dev.peterhinch.assessmenttask2.lib.MyHash;
import dev.peterhinch.assessmenttask2.models.Record;
import dev.peterhinch.assessmenttask2.viewmodels.MyHashViewModel;

public class ListActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    // Declare the ViewModel holding data from the database.
    private MyHashViewModel hashTable;

    // Declare the RecyclerView.
    private RecyclerView recyclerViewList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Create the ViewModelProvider for MyHashViewModel
        hashTable = new ViewModelProvider(this).get(MyHashViewModel.class);
        if (hashTable.myHash == null) {
            // Retrieve data from the database and hash that data.
            ArrayList<Record> allRecords = RecordDb.getInstance().readRecords();
            hashTable.myHash = new MyHash();
            hashTable.myHash.buildHashTable(allRecords);
        } else {
            Log.d(TAG, "ViewModel created.");
        }

        // Set the data for the recycler view.
        recyclerViewList = findViewById(R.id.list_recyclerView);
        // Create and set the adapter, then set the layout manager.
        ListRecyclerViewAdapter adapter =
                new ListRecyclerViewAdapter(hashTable.myHash.toList(false));
        recyclerViewList.setAdapter(adapter);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));

        // Set the click functions for the index buttons.
        setIndexClickListeners();
    }

    // Use the key provided to calculate the offset for the recycler view.
    private void navBtnClick(int key) {
        if (key < 0 || key > 26) {
            return;
        }

        // Calculate the offset based on the key provided.
        int offset = hashTable.myHash.calcOffsetByKey(key);
        Log.d(TAG, "Offset for key: " + key + " is: " + offset);

        // Use the offset to scroll the view to that location.
        ((LinearLayoutManager) Objects.requireNonNull(
                recyclerViewList.getLayoutManager()))
                .scrollToPositionWithOffset(offset, 0);
    }

    // Create a click listener for each of the index buttons programmatically:
    // Loop through all buttons by chaining getResources().getIdentifier() to
    // determine the id value for each. Buttons have been renamed numerically
    // to simplify the iteration.
    // Reference: https://stackoverflow.com/questions/22639218/how-to-get-all-buttons-ids-in-one-time-on-android
    private void setIndexClickListeners() {
        for(int i = 0; i <= 26; i++) {
            int buttonId = getResources().getIdentifier("list_button_" + i, "id", getPackageName());
            int buttonKey = i;
            findViewById(buttonId).setOnClickListener(v -> ListActivity.this.navBtnClick(buttonKey));
        }
    }
}