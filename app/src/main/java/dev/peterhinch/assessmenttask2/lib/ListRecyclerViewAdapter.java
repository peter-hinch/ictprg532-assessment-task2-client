package dev.peterhinch.assessmenttask2.lib;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import dev.peterhinch.assessmenttask2.R;
import dev.peterhinch.assessmenttask2.activities.DetailActivity;
import dev.peterhinch.assessmenttask2.activities.EditActivity;
import dev.peterhinch.assessmenttask2.room.entities.Record;

// The recycler view adapter must extend RecyclerView.Adapter .
public class ListRecyclerViewAdapter
        extends RecyclerView.Adapter<ListRecyclerViewAdapter.ListItemViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    // Declare a dataset.
    private ArrayList<Record> recordList;

    // String pattern and date format for date display.
    private final String pattern = "dd/MM/yyyy";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    // Create a constructor for the adapter class and dataset property.
    public ListRecyclerViewAdapter(ArrayList<Record> recordList) {
        this.recordList = recordList;
    }

    public void refreshList() {
        notifyDataSetChanged();
    }

    public void recordUpdate(int recordPosition) {
        notifyItemChanged(recordPosition);
    }

    public void recordDelete(int recordPosition) {
        notifyItemRemoved(recordPosition);
    }

    // Implement onCreateViewHolder, onBindViewHolder and getItemCount.
    // Implement onCreateViewHolder.
    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item, parent, false);
        return new ListItemViewHolder(v);
    }

    // Implement onBindViewHolder.
    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        holder.id = recordList.get(position).getId();
        holder.position = holder.getAdapterPosition();
        holder.txtViewHeading.setText(recordList.get(position).getHeading());
        holder.txtViewDescription.setText(recordList.get(position).getDescription());
        holder.txtViewPhone.setText(recordList.get(position).getPhone());
        holder.txtViewDate.setText(
                simpleDateFormat.format(recordList.get(position).getDate()));

        // Create onClickListener and onLongClickListeners for mainView.
        holder.mainView.setOnClickListener(view -> {
            Context context = view.getContext();
            detailsClick(context, holder);
        });
        holder.mainView.setOnLongClickListener(view -> deleteDrag(view, holder));

        // Create an onClickListener for the edit button.
        holder.editFab.setOnClickListener(view -> {
            Context context = view.getContext();
            editClick(context, holder);
        });
    }

    // Implement getItemCount.
    @Override
    public int getItemCount() {
        return recordList == null ? 0 : recordList.size();
    }

    // Create an inner class that extends RecyclerView.ViewHolder .
    class ListItemViewHolder extends RecyclerView.ViewHolder {
        private final FrameLayout mainView;
        private int id;
        private int position;
        private final TextView txtViewHeading;
        private final TextView txtViewDescription;
        private final TextView txtViewPhone;
        private final TextView txtViewDate;
        private final FloatingActionButton editFab;

        // The inner class must have a constructor for the view holder.
        public ListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setTag(TAG);

            // Set the values in the item view.
            txtViewHeading = itemView.findViewById(R.id.itemView_textView_heading);
            txtViewDescription = itemView.findViewById(R.id.itemView_textView_description);
            txtViewPhone = itemView.findViewById(R.id.itemView_textView_phone);
            txtViewDate = itemView.findViewById(R.id.itemView_textView_date);
            editFab = itemView.findViewById(R.id.itemView_fabMini_edit);
            mainView = itemView.findViewById(R.id.itemView_frameLayout_main);
        }
    }

    private void detailsClick(Context context, ListItemViewHolder holder) {
        Log.d(TAG, "MAIN VIEW CLICKED AT POSITION: " + holder.position + ".");
        Intent intent = new Intent(context, DetailActivity.class);

        // Create a bundle to pass details into the detail activity.
        Bundle bundle = new Bundle();
        bundle.putString("heading", holder.txtViewHeading.getText().toString());
        bundle.putString("description", holder.txtViewDescription.getText().toString());
        bundle.putString("phone", holder.txtViewPhone.getText().toString());
        bundle.putString("date", holder.txtViewDate.getText().toString());
        intent.putExtras(bundle);

        context.startActivity(intent);
    }

    private void editClick(Context context, ListItemViewHolder holder) {
        Log.d(TAG, "EDIT CLICKED AT POSITION: " + holder.position + ".");
        Intent intent = new Intent(context, EditActivity.class);

        // Create a bundle to pass details into the detail activity.
        Bundle bundle = new Bundle();
        bundle.putString("heading", holder.txtViewHeading.getText().toString());
        bundle.putString("description", holder.txtViewDescription.getText().toString());
        bundle.putString("phone", holder.txtViewPhone.getText().toString());
        bundle.putString("date", holder.txtViewDate.getText().toString());
        intent.putExtras(bundle);

        context.startActivity(intent);
    }

    private boolean deleteDrag(View view, ListItemViewHolder holder) {
        Log.d(TAG, "MAIN VIEW LONG CLICKED AT POSITION: " + holder.position + ".");
        // Create a new ClipData object (this is the data to be passed
        // in the drag and drop operation).
        ClipData.Item listItemId = new ClipData.Item(Integer.toString(holder.id));
        ClipData.Item listItemPos = new ClipData.Item(Integer.toString(holder.position));
        ClipData dragData = new ClipData(
                (CharSequence) view.getTag(),
                new String[] {ClipDescription.MIMETYPE_TEXT_PLAIN},
                listItemId
        );
        dragData.addItem(listItemPos);

        // Instantiate the drag shadow builder.
        View.DragShadowBuilder myShadow = new DragShadowBuilder(view);

        // Start the drag - 'null' means no local data is used, '0' means
        // no flags are passed.
        view.startDrag(dragData, myShadow, null, 0);

        return true;
    }
}
