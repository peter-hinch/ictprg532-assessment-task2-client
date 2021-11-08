package dev.peterhinch.assessmenttask2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import dev.peterhinch.assessmenttask2.R;

public class DetailActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    EditText editTextHeading;
    EditText editTextDescription;
    EditText editTextPhone;
    EditText editTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Retrieve the data passed in with the bundle.
        Bundle bundle = getIntent().getExtras();

        // Define the EditText views within the details activity.
        editTextHeading = findViewById(R.id.detail_editText_heading);
        editTextDescription = findViewById(R.id.detail_editText_description);
        editTextPhone = findViewById(R.id.detail_editText_phone);
        editTextDate = findViewById(R.id.detail_editText_date);

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

    // TODO - Process the 'Call' button event (advanced functionality).
    // Reference: https://ssaurel.medium.com/learn-to-make-calls-programmatically-on-android-d5c3069a7c28
    // Reference: https://developer.android.com/training/permissions/requesting
//    private void callClick() {
//        Button btnCall = findViewById(R.id.detail_button_call);
//        btnCall.setOnClickListener(view -> {
//            Log.d(TAG, "Call button pressed.");
//
//            String numberToCall = editTextPhone.getText().toString();
//
//            // Check that the field is not empty.
//            if (!TextUtils.isEmpty(numberToCall)) {
//                // Verify that the app has the correct permissions
//                if (checkPermission(Manifest.permission.CALL_PHONE, , )) {
//                    String dial = "tel:" + numberToCall;
//                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
//                } else {
//                    Log.e(TAG, "Android permission CALL_PHONE denied.");
//                }
//            } else {
//                Toast.makeText(DetailActivity.this, "No phone number entered.",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        if (checkPermission(Manifest.permission.CALL_PHONE, , )) {
//            dial.setEnabled(true);
//        }
//    }
}