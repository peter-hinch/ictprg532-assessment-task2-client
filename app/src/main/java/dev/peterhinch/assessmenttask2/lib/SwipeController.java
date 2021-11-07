package dev.peterhinch.assessmenttask2.lib;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;
import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

// TODO - Detect swipe gesture to reveal the edit button.
// Class to act on swipe actions within the RecyclerView.
// Reference: https://www.learn2crack.com/2016/02/custom-swipe-recyclerview.html
// Reference: https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28#ed30
// Reference: https://medium.com/mindorks/swipe-to-reply-android-recycler-view-ui-c11365f8999f
// Reference: https://medium.com/android-news/android-recyclerview-swipeable-items-46a3c763498d

public class SwipeController extends ItemTouchHelper.Callback {
    private final String TAG = this.getClass().getSimpleName();

    private boolean swipeBack = false;
    private boolean revealVisible = false;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currentItemViewHolder = null;
    private SwipeControllerActions buttonActions;
    private static final float buttonWidth = 300;

    public SwipeController(SwipeControllerActions buttonActions) {
        this.buttonActions = buttonActions;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT);
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
    }

    // Override convertToAbsoluteDirection to prevent items from swiping off
    // the list entirely.
    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX,
                            float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ACTION_STATE_SWIPE) {
                setTouchListener(canvas, recyclerView, viewHolder, dX, dY, actionState,
                        isCurrentlyActive);
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState,
                isCurrentlyActive);
    }

    // Set swipeBack to true after item swiping is released, bringing the item
    // back instead of dismissing it from the view.
    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(final Canvas canvas, @NonNull final RecyclerView recyclerView,
                                  final RecyclerView.ViewHolder viewHolder,
                                  final float dX, final float dY, final int actionState,
                                  final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((v, event) -> {
            swipeBack =
                    event.getAction() == MotionEvent.ACTION_CANCEL ||
                    event.getAction() == MotionEvent.ACTION_UP;
            // Determine if the swipe was large enough to reveal the button.
            if (swipeBack && dX < -buttonWidth)
            {
                Log.d(TAG, "swipeBack was sufficient to reveal button, dX: " + dX);
                revealVisible = true;
                setTouchDownListener(canvas, recyclerView, viewHolder, dX, dY,
                        actionState, isCurrentlyActive);
                // Prevent clicking on RecyclerView items while active.
                setItemsClickable(recyclerView, false);
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchDownListener(final Canvas canvas, @NonNull final RecyclerView recyclerView,
                                      final RecyclerView.ViewHolder viewHolder,
                                      final float dX, final float dY, final int actionState,
                                      final boolean isCurrentlyActive) {
        Log.d(TAG, "setTouchDownListener");
        recyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(canvas, recyclerView, viewHolder, dX, dY,
                        actionState, isCurrentlyActive);
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchUpListener(final Canvas canvas, @NonNull final RecyclerView recyclerView,
                                    final RecyclerView.ViewHolder viewHolder,
                                    final float dX, final float dY,
                                    final int actionState, final boolean isCurrentlyActive) {
        Log.d(TAG, "setTouchUpListener");
        recyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                SwipeController.super.onChildDraw(canvas, recyclerView, viewHolder,
                        0F, dY, actionState, isCurrentlyActive);
                recyclerView.setOnTouchListener((v1, event1) -> false);
                setItemsClickable(recyclerView, true);
                swipeBack = false;
                Log.d(TAG, "" + revealVisible);
                if (revealVisible) {
                    buttonActions.onClicked(viewHolder.getAdapterPosition());
                }
                revealVisible = false;
                currentItemViewHolder = null;
            }
            return false;
        });
    }

    private void setItemsClickable(@NonNull RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }
}
