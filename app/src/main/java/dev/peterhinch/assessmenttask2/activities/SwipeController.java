package dev.peterhinch.assessmenttask2.activities;

import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static androidx.recyclerview.widget.ItemTouchHelper.RIGHT;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

// Class to act on swipe actions within the RecyclerView.
// Reference: https://www.learn2crack.com/2016/02/custom-swipe-recyclerview.html
// Reference: https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28#ed30
public class SwipeController extends ItemTouchHelper.Callback {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                         int direction) {
        int position = viewHolder.getAdapterPosition();

        if (direction == LEFT) {
            Log.d(TAG, "Swipe detected on item " + position + ".");
        }
    }
}
