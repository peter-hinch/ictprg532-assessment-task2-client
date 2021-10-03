package dev.peterhinch.assessmenttask2.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import dev.peterhinch.assessmenttask2.R;

// Method for switching between activities with an animation derived from the GeeksForGeeks tutorial:
// 'How to add slide animation between activities in android?'
// reference: https://www.geeksforgeeks.org/how-to-add-slide-animation-between-activities-in-android/

public class WelcomeActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private final String isDarkThemeKey = "is_dark_theme_key";
    private int activeThemeResId;

    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load and set theme.
        loadAndSetTheme();
        // Set layout.
        setContentView(R.layout.activity_welcome);
        // Set action bar background color.
        setActionBarStyle();

        // Buttons to for light and dark themes
        findViewById(R.id.welcome_button_themeDark)
                .setOnClickListener(v -> setAndSaveThemeSetting(true));
        findViewById(R.id.welcome_button_themeLight)
                .setOnClickListener(v -> setAndSaveThemeSetting(false));

        // Detect swipe gesture to progress to the next screen.
        mDetector = new GestureDetectorCompat(this, new SliderGestureListener());
    }

    // This method opens the ListActivity screen with the animations specified in the XML files
    // within the resources directory.
    public void openListActivity() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class SliderGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        // Detect a fling gesture and animate if the direction is as intended.
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                float velocityX, float velocityY) {
            Log.d(TAG, "onFling: " + velocityX + "/" + velocityY);
            // velocityX will be a value less than zero if the swipe is to the right.
            if (velocityX < 0) {
                openListActivity();
            }
            return true;
        }
    }

    // for app action bar, if dark theme, color to be set accordingly.
    // if light theme, system default setting is good enough
    private void setActionBarStyle() {
        if (activeThemeResId == R.style.DarkTheme) {
            ActionBar actionBar;
            actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(
                        this, R.color.colorActionBarBackgroundDarkTheme)));
            }
        }
    }

    // load theme setting from Shared Preferences
    // refer to this tutorial
    // https://developer.android.com/training/data-storage/shared-preferences#ReadSharedPreference
    private void loadAndSetTheme() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        boolean isDark = sharedPref.getBoolean(isDarkThemeKey, false);
        activeThemeResId = isDark ? R.style.DarkTheme : R.style.AppTheme;
        setTheme(activeThemeResId);
    }

    // write theme setting from Shared Preferences
    // refer to this tutorial
    // https://developer.android.com/training/data-storage/shared-preferences#WriteSharedPreference
    private void setAndSaveThemeSetting(boolean isDark) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Set the key value for dark theme to true in shared preferences.
        editor.putBoolean(isDarkThemeKey, isDark);
        editor.apply();

        // recreate this activity. so that new theme will be applied.
        recreate();
    }
}
