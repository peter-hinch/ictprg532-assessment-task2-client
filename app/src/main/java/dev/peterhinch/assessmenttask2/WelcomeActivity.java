package dev.peterhinch.assessmenttask2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

// Method for switching between activities with an animation from the GeeksForGeeks tutorial:
// 'How to add slide animation between activities in android?'
// reference: https://www.geeksforgeeks.org/how-to-add-slide-animation-between-activities-in-android/

public class WelcomeActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private final DisplayMetrics displayMetrics = new DisplayMetrics();

    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mDetector = new GestureDetectorCompat(this, new SliderGestureListener());
    }

    // This method opens the ListActivity screen with the specified animation from the resources
    // directory.
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
}