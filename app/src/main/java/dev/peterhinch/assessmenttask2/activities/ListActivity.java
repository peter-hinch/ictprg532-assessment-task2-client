package dev.peterhinch.assessmenttask2.activities;

import static dev.peterhinch.assessmenttask2.lib.MyHash.SORT_ASC;
import static dev.peterhinch.assessmenttask2.lib.MyHash.SORT_DESC;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import dev.peterhinch.assessmenttask2.R;
import dev.peterhinch.assessmenttask2.fragments.SearchDialogFragment;
import dev.peterhinch.assessmenttask2.lib.ListRecyclerViewAdapter;
import dev.peterhinch.assessmenttask2.room.RecordDb;
import dev.peterhinch.assessmenttask2.lib.MyHash;
import dev.peterhinch.assessmenttask2.room.entities.Record;
import dev.peterhinch.assessmenttask2.viewmodels.MyHashViewModel;

public class ListActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    // Declare the ViewModel holding data from the database.
    private MyHashViewModel hashTable;

    // Declare the RecyclerView.
    ArrayList<Record> recyclerViewData;
    private RecyclerView listRecyclerView;
    private ListRecyclerViewAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // TODO - Add integration with Rest API
        //RetrofitServices.getInstance().RecordCreate(new Record("Peter", "Hinch", "2021-10-15"), this);
        //RetrofitServices.getInstance().RecordReadOne(1, this);
        //RetrofitServices.getInstance().RecordReadAll(this);
        //RetrofitServices.getInstance().RecordUpdate(1, new Record("Peter2", "Hinch2", "2021-10-16"), this);
        //RetrofitServices.getInstance().RecordDelete(1, this);

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

        // Define the data for the recycler view.
        recyclerViewData = hashTable.myHash.toList(SORT_ASC);

        // Set the data for the recycler view.
        listRecyclerView = findViewById(R.id.list_recyclerView);
        // Create and set the adapter, then set the layout manager.
        adapter = new ListRecyclerViewAdapter(recyclerViewData);
        listRecyclerView.setAdapter(adapter);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set the drag listener for item deletion.
        deleteDrag();

        // Set the click functions for the FABs.
        deleteClick();
        searchClick();
        sortAscClick();
        sortDescClick();
        addClick();

        // Set the click functions for the index buttons.
        setIndexClickListeners();
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
                listRecyclerView.getLayoutManager()))
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

    // Ascending (A-Z) sort functionality.
    private void sortAscClick() {
        // Buttons for sort and search.
        FloatingActionButton btnSortAsc = findViewById(R.id.list_fabMini_sortAsc);
        btnSortAsc.setOnClickListener(view -> {
            // TODO - Ensure current data is displayed.
            ListActivity.this.adapter.reloadList(hashTable.myHash.toList(SORT_ASC));
            updateIndexLabels(SORT_ASC);
        });
    }

    // Descending (Z-A) sort functionality.
    private void sortDescClick() {
        FloatingActionButton btnSortDesc = findViewById(R.id.list_fabMini_sortDesc);
        btnSortDesc.setOnClickListener(view -> {
            // TODO - Ensure current data is displayed.
            ListActivity.this.adapter.reloadList(hashTable.myHash.toList(SORT_DESC));
            updateIndexLabels(SORT_DESC);
        });
    }

    // Create functionality.
    private void addClick() {
        FloatingActionButton btnAdd = findViewById(R.id.list_fabMini_add);
        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(ListActivity.this, AddActivity.class);
            startActivity(intent);
        });
    }

    // Delete hint (display a simple toast message when clicking on the button).
    private void deleteClick() {
        FloatingActionButton btnDelete = findViewById(R.id.list_fabMini_delete);
        btnDelete.setOnClickListener((view -> Toast.makeText(
                getApplicationContext(),
                "Drag a record into the bin to delete.",
                Toast.LENGTH_SHORT
        ).show()));
    }

    // Delete (drag and drop) functionality.
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

                case DragEvent.ACTION_DRAG_ENTERED:
                    // Change the icon for the delete button on drag over.
                    btnDelete.setImageDrawable(AppCompatResources.getDrawable(
                            this, R.drawable.ic_baseline_delete_forever_24)
                    );
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    // Change the icon for the delete button back to original icon.
                    btnDelete.setImageDrawable(AppCompatResources.getDrawable(
                            this, R.drawable.ic_baseline_delete_24)
                    );
                    return true;

                case DragEvent.ACTION_DROP:
                    // Retrieve the ID and item position from the clip data that
                    // was transferred with the dragged item.
                    ClipData.Item listItemId = dragEvent.getClipData().getItemAt(0);
                    ClipData.Item listItemPosition = dragEvent.getClipData().getItemAt(1);
                    int recordId = Integer.parseInt(listItemId.getText().toString());
                    int itemPosition = Integer.parseInt(listItemPosition.getText().toString());

                    // Find and delete the record from the database using the ID.
                    Record recordToDelete = RecordDb.findRecordById(this, recordId);
                    RecordDb.deleteRecord(this, recordToDelete);

                    // Refresh the recycler view to reflect database change using
                    // the position.
                    recyclerViewData.remove(itemPosition);
                    adapter.notifyItemRemoved(itemPosition);

                    // Change the icon for the delete button back to original icon.
                    btnDelete.setImageDrawable(AppCompatResources.getDrawable(
                            this, R.drawable.ic_baseline_delete_24)
                    );

                    Log.d(TAG, "Record " + recordId +
                            " at position " + itemPosition + " was deleted.");
                    return true;
            }
            return false;
        });
    }

    // TODO - Create a click listener to start the edit activity.

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
}
