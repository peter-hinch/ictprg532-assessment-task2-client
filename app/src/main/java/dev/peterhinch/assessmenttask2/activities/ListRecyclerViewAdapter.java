package dev.peterhinch.assessmenttask2.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import dev.peterhinch.assessmenttask2.R;
import dev.peterhinch.assessmenttask2.room.entities.Record;

// The recycler view adapter must extend RecyclerView.Adapter .
public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ListItemViewHolder> {
    // Declare a dataset.
    private ArrayList<Record> recordList;

    // String pattern and date format for date display
    private String pattern = "dd/MM/yyyy";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    // Create a constructor for the adapter class and dataset property.
    public ListRecyclerViewAdapter(ArrayList<Record> recordList) {
        this.recordList = recordList;
    }

    // A method to reload the list contents.
    public void reloadList (ArrayList<Record> recordList) {
        this.recordList = recordList;
        this.notifyDataSetChanged();
    }

    // Implement onCreateViewHolder, onBindViewHolder and getItemCount.
    // Implement onCreateViewHolder
    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item, parent, false);
        return new ListItemViewHolder(v);
    }

    // Implement onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        holder.txtViewHeading.setText(recordList.get(position).getHeading());
        holder.txtViewDescription.setText(recordList.get(position).getDescription());
        holder.txtViewPhone.setText(recordList.get(position).getPhone());
        holder.txtViewDate.setText(
                simpleDateFormat.format(recordList.get(position).getDate()));
    }

    // Implement getItemCount
    @Override
    public int getItemCount() {
        return recordList == null ? 0 : recordList.size();
    }

    // Create an inner class that extends RecyclerView.ViewHolder .
    static class ListItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtViewHeading;
        private TextView txtViewDescription;
        private TextView txtViewPhone;
        private TextView txtViewDate;
        // The inner class must have a constructor for the view holder
        public ListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Set the values in the item view
            txtViewHeading = itemView.findViewById(R.id.itemView_textView_heading);
            txtViewDescription = itemView.findViewById(R.id.itemView_textView_description);
            txtViewPhone = itemView.findViewById(R.id.itemView_textView_phone);
            txtViewDate = itemView.findViewById(R.id.itemView_textView_date);
        }
    }
}
