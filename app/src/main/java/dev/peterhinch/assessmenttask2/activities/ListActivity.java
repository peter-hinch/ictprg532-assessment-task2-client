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
    }
}