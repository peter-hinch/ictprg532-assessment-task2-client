package dev.peterhinch.assessmenttask2.activities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

// Create a drag shadow for dragging an ItemView as a small grey rectangle.
// Reference: https://developer.android.com/guide/topics/ui/drag-drop
public class DragShadowBuilder extends View.DragShadowBuilder {
    // Define the drag shadow image.
    private static Drawable shadow;

    // Constructor for DragShadowBuilder.
    public DragShadowBuilder(View v) {
        // Store the View parameter passed to myDragShadowBuilder.
        super(v);

        // Create a draggable image that fills the provided canvas.
        shadow = new ColorDrawable(Color.LTGRAY);
    }

    // Define a callback that sends the drag shadow dimensions and touch point.
    @Override
    public void onProvideShadowMetrics (Point size, Point touch) {
        // Define local variables.
        int width, height;

        // Set the width and height of the shadow to half that of the original view.
        width = getView().getWidth() / 2;
        height = getView().getHeight() / 2;

        // Set the dimensions of the drag shadow to be the same as the provided Canvas.
        shadow.setBounds(0, 0, width, height);

        // Set the size width and height.
        size.set(width, height);

        // Set the touch point position to be in the middle of the drag shadow.
        touch.set(width / 2, height / 2);
    }

    // Define the callback that draws the drag shadow in a Canvas constructed
    // from the dimensions in onProvideShadowMetrics() .
    @Override
    public void onDrawShadow(Canvas canvas) {
        // Draw the ColorDrawable in the provided Canvas.
        shadow.draw(canvas);
    }
}
