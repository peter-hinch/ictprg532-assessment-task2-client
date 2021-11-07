package dev.peterhinch.assessmenttask2.lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

// The following SwipeRevealLayout is based on an example by Mark O'Sullivan.
/*
 * Copyright (c) 2018 Mark O'Sullivan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
// Reference: https://medium.com/android-news/android-recyclerview-swipeable-items-46a3c763498d
public class SwipeRevealLayout extends ViewGroup {

    private static final String SUPER_INSTANCE_STATE = "saved_instance_state_parcelable";

    private static final int DEFAULT_MIN_FLING_VELOCITY = 300; // dp per second.
    private static final int DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT = 1; // dp.

    private static final int DRAG_EDGE_RIGHT = 0x1 << 1;

    // While view resides underneath the main view.
    public static int MODE_NORMAL = 0;
    // While the secondary view is attached to the edge of the main view.
    public static int MODE_SAME_LEVEL = 1;

    // Main view
    private View mainView;
    // Secondary view
    private View secondaryView;

    // Rectangles representing bounds for view states.
    private final Rect rectMainClose = new Rect();
    private final Rect rectMainOpen = new Rect();
    private final Rect rect2ndClose = new Rect();
    private final Rect rect2ndOpen = new Rect();

    // Minimum distance (px) to the closest drag edge that the SwipeRevealLayout
    // will disallow the parent to intercept touch event.
    private int minDistRequestDisallowParent = 0;

    private boolean isOpenBeforeInit = false;
    private volatile boolean isScrolling = false;
    private volatile boolean lockDrag = false;

    private int minFlingVelocity = DEFAULT_MIN_FLING_VELOCITY;
    private int mode = MODE_NORMAL;

    private int dragEdge = DRAG_EDGE_RIGHT;

    private float dragDist = 0;
    private float prevX = -1;

    private ViewDragHelper dragHelper;
    private GestureDetectorCompat gestureDetector;

    public SwipeRevealLayout(Context context) {
        super(context);
        init(context, null);
    }

    public SwipeRevealLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public SwipeRevealLayout(Context context, AttributeSet attributeSet,
                             int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_INSTANCE_STATE, super.onSaveInstanceState());
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        state = bundle.getParcelable(SUPER_INSTANCE_STATE);
        super.onRestoreInstanceState(state);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (isDragLocked()) {
            return super.onInterceptTouchEvent(motionEvent);
        }

        dragHelper.processTouchEvent(motionEvent);
        gestureDetector.onTouchEvent(motionEvent);
        accumulateDragDist(motionEvent);

        boolean couldBecomeClick = couldBecomeClick(motionEvent);
        boolean settling =
                dragHelper.getViewDragState() == ViewDragHelper.STATE_SETTLING;
        boolean idleAfterScrolled =
                dragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE &&
                isScrolling;

        // This needs to be placed as the last statement.
        prevX = motionEvent.getX();

        // Return true => intercept, cannot trigger onClick event.
        return !couldBecomeClick && (settling || idleAfterScrolled);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        // Get views.
        if (getChildCount() >= 2) {
            secondaryView = getChildAt(0);
            mainView = getChildAt(1);
        }
        else if (getChildCount() == 1) {
            mainView = getChildAt(0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);

            int left, right, top, bottom;

            final int minLeft = getPaddingLeft();
            final int maxRight = Math.max(r - getPaddingRight() - l, 0);
            final int minTop = getPaddingTop();
            final int maxBottom = Math.max(b - getPaddingBottom() - t, 0);

            int measuredChildHeight = child.getMeasuredHeight();
            int measuredChildWidth = child.getMeasuredWidth();

            // Accommodate child size of 'match_parent'.
            final LayoutParams childParams = child.getLayoutParams();
            boolean isMatchParentHeight = false;
            boolean isMatchParentWidth = false;

            if (childParams != null) {
                isMatchParentHeight = (childParams.height == LayoutParams.MATCH_PARENT);
                isMatchParentWidth = (childParams.width == LayoutParams.MATCH_PARENT);
            }

            if (isMatchParentHeight) {
                measuredChildHeight = maxBottom - minTop;
                childParams.height = measuredChildHeight;
            }

            if (isMatchParentWidth) {
                measuredChildWidth = maxRight - minLeft;
                childParams.width = measuredChildWidth;
            }

            left = Math.max(r - measuredChildWidth - getPaddingRight() - l, minLeft);
            top = Math.min(getPaddingTop(), maxBottom);
            right = Math.max(r - getPaddingRight() - l, minLeft);
            bottom = Math.min(measuredChildHeight + getPaddingTop(), maxBottom);

            child.layout(left, top, right, bottom);
        }

        // Adjust the offset when the offset mode is SAME_LEVEL .
        if (mode == MODE_SAME_LEVEL) {
            secondaryView.offsetLeftAndRight(secondaryView.getWidth());
        }

        initRects();

        if (isOpenBeforeInit) {
            open(false);
        } else {
            close(false);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() < 2) {
            throw new RuntimeException("Layout must have two children");
        }

        final LayoutParams params = getLayoutParams();

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int desiredWidth = 0;
        int desiredHeight = 0;

        // First find the largest child.
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            desiredWidth = Math.max(child.getMeasuredWidth(), desiredWidth);
            desiredHeight = Math.max(child.getMeasuredHeight(), desiredHeight);
        }
        // Create new measure spec using the largest child width.
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(desiredWidth, widthMode);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, heightMode);

        final int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            final LayoutParams childParams = child.getLayoutParams();

            if (childParams != null) {
                if (childParams.height == LayoutParams.MATCH_PARENT) {
                    child.setMinimumHeight(measuredHeight);
                }

                if (childParams.width == LayoutParams.MATCH_PARENT) {
                    child.setMinimumWidth(measuredWidth);
                }
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            desiredWidth = Math.max(child.getMeasuredWidth(), desiredWidth);
            desiredHeight = Math.max(child.getMeasuredHeight(), desiredHeight);
        }

        // taking accounts of padding
        desiredWidth += getPaddingLeft() + getPaddingRight();
        desiredHeight += getPaddingTop() + getPaddingBottom();

        // adjust desired width
        if (widthMode == MeasureSpec.EXACTLY) {
            desiredWidth = measuredWidth;
        } else {
            if (params.width == LayoutParams.MATCH_PARENT) {
                desiredWidth = measuredWidth;
            }

            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = Math.min(desiredWidth, measuredWidth);
            }
        }

        // adjust desired height
        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = measuredHeight;
        } else {
            if (params.height == LayoutParams.MATCH_PARENT) {
                desiredHeight = measuredHeight;
            }

            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = Math.min(desiredHeight, measuredHeight);
            }
        }

        setMeasuredDimension(desiredWidth, desiredHeight);
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    // Open the panel to show the secondary view.
    public void open(boolean animation) {
        isOpenBeforeInit = true;

        if (animation) {
            dragHelper.smoothSlideViewTo(mainView, rectMainOpen.left, rectMainOpen.top);
        } else {
            dragHelper.abort();
            mainView.layout( rectMainOpen.left, rectMainOpen.top, rectMainOpen.right,
                    rectMainOpen.bottom);
            secondaryView.layout(rect2ndOpen.left, rect2ndOpen.top, rect2ndOpen.right,
                    rect2ndOpen.bottom);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

     // Close the panel to hide the secondary view.
    public void close(boolean animation) {
        isOpenBeforeInit = false;

        if (animation) {
            dragHelper.smoothSlideViewTo(mainView, rectMainClose.left, rectMainClose.top);
        } else {
            dragHelper.abort();
            mainView.layout(rectMainClose.left, rectMainClose.top, rectMainClose.right,
                    rectMainClose.bottom);
            secondaryView.layout(rect2ndClose.left, rect2ndClose.top, rect2ndClose.right,
                    rect2ndClose.bottom);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public boolean isDragLocked() {
        return lockDrag;
    }

    public void dragLock(boolean drag) {
        this.lockDrag = drag;
    }

    private int getMainOpenLeft() {
        if (dragEdge == DRAG_EDGE_RIGHT) {
            return rectMainClose.left - secondaryView.getWidth();
        }
        return 0;
    }

    private int getMainOpenTop() {
        if (dragEdge == DRAG_EDGE_RIGHT) {
            return rectMainClose.top;
        }
        return 0;
    }

    private int get2ndOpenLeft() {
        return rect2ndClose.left;
    }

    private int get2ndOpenTop() {
        return rect2ndClose.top;
    }

    private void initRects() {
        // Closed position for the main view.
        rectMainClose.set(mainView.getLeft(), mainView.getTop(), mainView.getRight(),
                mainView.getBottom());

        // Closed position for the secondary view.
        rect2ndClose.set(secondaryView.getLeft(), secondaryView.getTop(),
                secondaryView.getRight(), secondaryView.getBottom());

        // Open position for the main view.
        rectMainOpen.set(getMainOpenLeft(), getMainOpenTop(),
                getMainOpenLeft() + mainView.getWidth(),
                getMainOpenTop() + mainView.getHeight());

        // Open position for the secondary view.
        rect2ndOpen.set(get2ndOpenLeft(), get2ndOpenTop(),
                get2ndOpenLeft() + secondaryView.getWidth(),
                get2ndOpenTop() + secondaryView.getHeight());
    }

    private boolean couldBecomeClick(MotionEvent motionEvent) {
        return isInMainView(motionEvent) && !shouldInitiateADrag();
    }

    private boolean isInMainView(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        boolean withinVertical = mainView.getTop() <= y && y <= mainView.getBottom();
        boolean withinHorizontal = mainView.getLeft() <= x && x <= mainView.getRight();

        return withinVertical && withinHorizontal;
    }

    private boolean shouldInitiateADrag() {
        float minDistToInitiateDrag = dragHelper.getTouchSlop();
        return dragDist >= minDistToInitiateDrag;
    }

    private void accumulateDragDist(MotionEvent motionEvent) {
        final int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            dragDist = 0;
            return;
        }

        float dragged = Math.abs(motionEvent.getX() - prevX);
        dragDist += dragged;
    }

    private void init(Context context, AttributeSet attributeSet) {
        if (attributeSet != null && context != null) {
            dragEdge = DRAG_EDGE_RIGHT;
            mode = MODE_NORMAL;
            minFlingVelocity = DEFAULT_MIN_FLING_VELOCITY;
            minDistRequestDisallowParent = DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT;
        }
        dragHelper = ViewDragHelper.create(this, 1.0f, dragHelperCallback);
        dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);

        gestureDetector = new GestureDetectorCompat(context, gestureListener);
    }

    private final GestureDetector.OnGestureListener gestureListener =
            new GestureDetector.SimpleOnGestureListener() {
        boolean hasDisallowed = false;

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            isScrolling = false;
            hasDisallowed = false;
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float velocityX, float velocityY) {
            isScrolling = true;

            if (getParent() != null) {
                boolean shouldDisallow;

                if( !hasDisallowed) {
                    shouldDisallow = getDistToClosestEdge() >= minDistRequestDisallowParent;
                    if (shouldDisallow) {
                        hasDisallowed = true;
                    }
                } else {
                    shouldDisallow = true;
                }

                // Disallow parent to intercept touch event so that the
                // layout will work properly on RecyclerView or view that
                // handles scroll gesture.
                getParent().requestDisallowInterceptTouchEvent(shouldDisallow);
            }
            return false;
        }
    };

    private int getDistToClosestEdge() {
        if (dragEdge == DRAG_EDGE_RIGHT) {
                final int pivotLeft = rectMainClose.right - secondaryView.getWidth();

                return Math.min(
                        mainView.getRight() - pivotLeft,
                        rectMainClose.right - mainView.getRight()
                );
        }
        return 0;
    }

    private int getHalfwayPivotHorizontal() {
        return rectMainClose.right - secondaryView.getWidth() / 2;
    }

    private final ViewDragHelper.Callback dragHelperCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {

            if (lockDrag)
                return false;

            dragHelper.captureChildView(mainView, pointerId);
            return false;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            if (dragEdge == DRAG_EDGE_RIGHT) {
                return Math.max(
                        Math.min(left, rectMainClose.left),
                        rectMainClose.left - secondaryView.getWidth()
                );
            }
            return child.getLeft();
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xVel, float yVel) {
            final boolean velRightExceeded =  pxToDp((int) xVel) >= minFlingVelocity;
            final boolean velLeftExceeded =   pxToDp((int) xVel) <= -minFlingVelocity;

            final int pivotHorizontal = getHalfwayPivotHorizontal();

            if (velRightExceeded) {
                close(true);
            } else if (velLeftExceeded) {
                open(true);
            } else {
                if (mainView.getRight() < pivotHorizontal) {
                    open(true);
                } else {
                    close(true);
                }
            }
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);

            if (lockDrag) {
                return;
            }
            dragHelper.captureChildView(mainView, pointerId);
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (mode == MODE_SAME_LEVEL) {
                if (dragEdge == DRAG_EDGE_RIGHT) {
                    secondaryView.offsetLeftAndRight(dx);
                } else {
                    secondaryView.offsetTopAndBottom(dy);
                }
            }
            ViewCompat.postInvalidateOnAnimation(SwipeRevealLayout.this);
        }
    };

    private int pxToDp(int px) {
        Resources resources = getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
