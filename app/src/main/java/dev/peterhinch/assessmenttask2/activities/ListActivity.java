package dev.peterhinch.assessmenttask2.activities;

import static android.view.DragEvent.ACTION_DROP;
import static dev.peterhinch.assessmenttask2.lib.MyHash.SORT_ASC;
import static dev.peterhinch.assessmenttask2.lib.MyHash.SORT_DESC;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import dev.peterhinch.assessmenttask2.R;
import dev.peterhinch.assessmenttask2.fragments.SearchDialogFragment;
import dev.peterhinch.assessmenttask2.room.RecordDb;
import dev.peterhinch.assessmenttask2.lib.MyHash;
import dev.peterhinch.assessmenttask2.room.entities.Record;
import dev.peterhinch.assessmenttask2.viewmodels.MyHashViewModel;

public class ListActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    // Declare the ViewModel holding data from the database.
    private MyHashViewModel hashTable;

    // Declare the RecyclerView.
    private RecyclerView recyclerViewList;
    private ListRecyclerViewAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Initialise the database
        RecordDb.initData(this);

        // Create the ViewModelProvider for MyHashViewModel
        hashTable = new ViewModelProvider(this).get(MyHashViewModel.class);
        if (hashTable.myHash == null) {
            // Retrieve data from the database and hash that data.
            ArrayList<Record> allRecords = (ArrayList<Record>) RecordDb
                    .getInstance(this).recordDao().getAllRecords();
            hashTable.myHash = new MyHash();
            hashTable.myHash.buildHashTable(allRecords);
        } else {
            Log.d(TAG, "ViewModel created.");
        }

        // Set the data for the recycler view.
        recyclerViewList = findViewById(R.id.list_recyclerView);
        // Create and set the adapter, then set the layout manager.
        adapter = new ListRecyclerViewAdapter(hashTable.myHash.toList(false));
        recyclerViewList.setAdapter(adapter);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));

        // Set the click functions for the index buttons.
        setIndexClickListeners();

        // Set the click functions for the sorting FABs.
        searchClick();
        sortAscClick();
        sortDescClick();
        addClick();
        deleteDrag();
    }

    // Use the key provided to calculate the offset for the recycler view.
    // TODO - Calculate correct offset for list in descending order.
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

    // TODO - Add Search functionality.
    private void searchClick() {
        FloatingActionButton btnSearchSubmit = findViewById(R.id.list_fabMini_search);
        btnSearchSubmit.setOnClickListener(view -> {
            Log.d(TAG, "Search FAB press");
            new SearchDialogFragment().show(
                    getSupportFragmentManager(), SearchDialogFragment.TAG);
        });
    }

    // Ascending (A-Z) sort functionality
    private void sortAscClick() {
        // Buttons for sort and search.
        FloatingActionButton btnSortAsc = findViewById(R.id.list_fabMini_sortAsc);
        btnSortAsc.setOnClickListener(view -> {
            ListActivity.this.adapter.reloadList(hashTable.myHash.toList(SORT_ASC));
            updateIndexLabels(SORT_ASC);
        });
    }

    // Descending (Z-A) sort functionality.
    private void sortDescClick() {
        FloatingActionButton btnSortDesc = findViewById(R.id.list_fabMini_sortDesc);
        btnSortDesc.setOnClickListener(view -> {
            ListActivity.this.adapter.reloadList(hashTable.myHash.toList(SORT_DESC));
            updateIndexLabels(SORT_DESC);
        });
    }

    // Create functionality
    private void addClick() {
        FloatingActionButton btnAdd = findViewById(R.id.list_fabMini_add);
        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(ListActivity.this, AddActivity.class);
            startActivity(intent);
        });
    }

    // TODO - Add update (swipe item) functionality

    // TODO - Add delete (drag and drop) functionality
    private void deleteDrag() {
        FloatingActionButton btnDelete = findViewById(R.id.list_fabMini_delete);
        btnDelete.setOnDragListener((view, dragEvent) -> {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determine whether this view can accept the dragged data.
                    if (dragEvent.getClipDescription().hasMimeType(
                            ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        Log.d(TAG, "View is able to accept dragged data.");
                        return true;
                    }
                    Log.w(TAG, "View is not able to accept dragged data.");
                    return false;
                case DragEvent.ACTION_DROP:
                    // Get the item containing the dragged data.
                    ClipData.Item listItem = dragEvent.getClipData().getItemAt(0);
                    // Retrieve the text data from the item.
                    CharSequence dragData = listItem.getText();
                    Log.d(TAG, dragData + " was dropped IN THE BIN");
                    return true;
            }
            return false;
        });
    }

    private void updateIndexLabels(boolean reverse) {
        // Create a Sting[] for the button labels using the string array defined
        // in strings.xml .
        String[] indexLabels = getResources().getStringArray(R.array.list_buttons_asc);

        for(int i = 0; i < indexLabels.length; i++) {
            // Obtain the button id to change
            int buttonId = getResources().getIdentifier("list_button_" + i, "id", getPackageName());
            Button indexButton = findViewById(buttonId);

            if (!reverse) {
                indexButton.setText(indexLabels[i]);
            } else {
                indexButton.setText(indexLabels[(indexLabels.length - 1) - i]);
            }
        }
    }

    // Create a click listener for each of the index buttons programmatically:
    // Loop through all buttons by chaining getResources().getIdentifier() to
    // determine the id value for each. Buttons have been renamed numerically
    // to simplify the iteration.
    // Reference: https://stackoverflow.com/questions/22639218/how-to-get-all-buttons-ids-in-one-time-on-android
    private void setIndexClickListeners() {
        // Set the number of buttons to be set according to the string array
        // defined in strings.xml .
        int indexLabelsQty = getResources().getStringArray(R.array.list_buttons_asc).length;
        for(int i = 0; i < indexLabelsQty; i++) {
            int buttonId = getResources().getIdentifier("list_button_" + i, "id", getPackageName());
            int buttonKey = i;
            findViewById(buttonId).setOnClickListener(v -> ListActivity.this.navBtnClick(buttonKey));
        }
    }

    // Create a listener to react to drag events.
    // Reference: https://developer.android.com/guide/topics/ui/drag-drop
    protected class myDragEventListener implements View.OnDragListener {
        // This is the method called when dispatching a drag event to the listener.
        public boolean onDrag(View v, DragEvent event) {
            // Define a variable to store the action type for incoming events.
            final int action = event.getAction();

            // Switch case to handle events.
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determine whether this view can accept the dragged data.
                    if (event.getClipDescription().hasMimeType(
                            ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        Log.d(TAG, "View is able to accept dragged data.");
                        return true;
                    }
                    Log.w(TAG, "View is not able to accept dragged data.");
                    return false;

                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d(TAG, "Drag entered.");
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    Log.d(TAG, "Drag location.");
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d(TAG, "Drag exited.");
                    return true;

                case ACTION_DROP:
                    // Get the item containing the dragged data.
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    // Retrieve the text data from the item.
                    CharSequence dragData = item.getText();
                    Log.d(TAG, "Dragged data is " + dragData);
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    if (event.getResult()) {
                        Log.d(TAG, "Drag ended: The drop was handled.");
                    } else {
                        Log.d(TAG, "Drag ended: The drop was unsuccessful.");
                    }
                    return true;

                default:
                    Log.e(TAG, "Unknown action type received by OnDragListener.");
            }
            return false;
        }
    }
}