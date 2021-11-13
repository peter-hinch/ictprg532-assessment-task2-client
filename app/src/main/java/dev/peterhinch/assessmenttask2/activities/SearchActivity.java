package dev.peterhinch.assessmenttask2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import dev.peterhinch.assessmenttask2.R;

public class SearchActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    EditText editTextQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Context context = getApplicationContext();

        // Define the editText view for the search query.
        editTextQuery = findViewById(R.id.search_editText_searchQuery);

        // Set the click function for the 'Search' and 'Cancel' buttons.
        searchClick(context);
        cancelClick(context);
    }

    private void searchClick(Context context) {
        Button btnSearch = findViewById(R.id.search_button_search);
        btnSearch.setOnClickListener(view -> {
            Log.d(TAG, "Search button pressed.");
            String query = editTextQuery.getText().toString();
            Intent intent = new Intent(context, ListActivity.class);

            // Create a bundle to pass the search query back to the list activity.
            Bundle bundle = new Bundle();
            if (query.equals("")) {
                bundle.putString("query", query);
            } else {
                bundle.remove("query");
            }
            intent.putExtras(bundle);

            context.startActivity(intent);
        });
    }

    private void cancelClick(Context context) {
        Button btnCancel = findViewById(R.id.search_button_cancel);
        btnCancel.setOnClickListener(view -> {
            Log.d(TAG, "Cancel button pressed.");
            Intent intent = new Intent(context, ListActivity.class);

            // Create a bundle to ensure no search query is passed back to the
            // list activity.
            Bundle bundle = new Bundle();
            bundle.remove("query");
            intent.putExtras(bundle);

            context.startActivity(intent);
        });
    }
}