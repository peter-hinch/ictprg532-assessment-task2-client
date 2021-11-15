package dev.peterhinch.assessmenttask2.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

import dev.peterhinch.assessmenttask2.R;
import dev.peterhinch.assessmenttask2.room.LocalRecordDb;
import dev.peterhinch.assessmenttask2.room.entities.Record;

public class DetailActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    Button btnCall;

    EditText editTextHeading;
    EditText editTextDescription;
    EditText editTextPhone;
    EditText editTextDate;

    // The record object to be displayed.
    Record recordToDisplay = null;

    // Date format for date display.
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("en_AU"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Retrieve the data passed in with the bundle.
        Bundle bundle = getIntent().getExtras();

        // Define the call button.
        btnCall = findViewById(R.id.detail_button_call);

        // Define the EditText views within the details activity.
        editTextHeading = findViewById(R.id.detail_editText_heading);
        editTextDescription = findViewById(R.id.detail_editText_description);
        editTextPhone = findViewById(R.id.detail_editText_phone);
        editTextDate = findViewById(R.id.detail_editText_date);

        // Populate the EditText views with information passed in with the bundle.
        try {
            int recordId = bundle.getInt("id");
            recordToDisplay = LocalRecordDb.recordReadOne(this, recordId);
            editTextHeading.setText(recordToDisplay.getHeading());
            editTextDescription.setText(recordToDisplay.getDescription());
            editTextPhone.setText(recordToDisplay.getPhone());
            editTextDate.setText(simpleDateFormat.format(recordToDisplay.getDate()));
        } catch (Exception ex) {
            Log.d(TAG, "There was an issue reading the record in from local " +
                    "database: " + ex);
        }

        // Set the click function for the activity buttons.
        backClick();
        callClick();
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

    // Set the click listener for the 'Call' button.
    // Reference: https://developer.android.com/training/permissions/requesting
    // Reference: https://ssaurel.medium.com/learn-to-make-calls-programmatically-on-android-d5c3069a7c28
    // Reference: https://www.journaldev.com/10409/android-runtime-permissions-example
    private void callClick() {
        btnCall.setOnClickListener(view -> {
            Log.d(TAG, "Call button pressed.");

            String numberToCall = editTextPhone.getText().toString();

            // Check that the phone number field is not empty.
            if (!TextUtils.isEmpty(numberToCall)) {
                // Verify that the app has the correct permissions
                if (checkPhonePermission()) {
                    String dial = "tel:" + numberToCall;
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                } else {
                    Log.e(TAG, "Android permission CALL_PHONE denied.");
                    // If the permission has not been granted, request permission
                    requestPhonePermission();
                }
            } else {
                Toast.makeText(DetailActivity.this, "No phone number entered.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Check to see whether the app has permission to make phone calls.
    private boolean checkPhonePermission() {
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.CALL_PHONE) ==
            PackageManager.PERMISSION_GRANTED;
    }

    // Request permission from the user to make phone calls.
    private void requestPhonePermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CALL_PHONE},
                MAKE_CALL_PERMISSION_REQUEST_CODE);
    }

    // Override onRequestPermissionsResult.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MAKE_CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                btnCall.setEnabled(true);
                Log.d(TAG, "Permission granted: CALL_PHONE.");
            } else {
                Log.d(TAG, "Permission denied: CALL_PHONE.");

                if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    showMessageOKCancel("You will need to allow this app permission " +
                                    "to make phone calls.",
                            (dialog, which) -> requestPhonePermission());
                }
            }
        }
    }

    // Build a simple message dialog to show a message.
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DetailActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
