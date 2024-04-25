package com.kalyan.onetapcollege;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Map;

public class ComplaintAdapterDisplay extends RecyclerView.Adapter<ComplaintAdapterDisplay.ViewHolder> {

    private List<Map<String, String>> complaintsData;

    public void setComplaintsData(List<Map<String, String>> complaintsData) {
        this.complaintsData = complaintsData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complaint_display, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> complaintData = complaintsData.get(position);
        holder.bind(complaintData);
    }

    @Override
    public int getItemCount() {
        return complaintsData == null ? 0 : complaintsData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView complaintIdTextView;
        private TextView blockTextView;
        private TextView descriptionTextView;
        private TextView nameTextView;
        private TextView statusTextView;
        private TextView titleTextView;
        private TextView typeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            complaintIdTextView = itemView.findViewById(R.id.complaintIdTextView);
            blockTextView = itemView.findViewById(R.id.blockTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
        }

        public void bind(Map<String, String> complaintData) {
            complaintIdTextView.setText("Complaint ID: " + complaintData.get("complaintId"));
            blockTextView.setText("Block: " + complaintData.get("block"));
            descriptionTextView.setText("Description: " + complaintData.get("description"));
            nameTextView.setText("Name: " + complaintData.get("name"));
            statusTextView.setText("Status: " + complaintData.get("status"));
            titleTextView.setText("Title: " + complaintData.get("title"));
            typeTextView.setText("Type: " + complaintData.get("type"));

            // Set background color based on status
            String status = complaintData.get("status");
            int color;
            switch (status) {
                case "Close":
                    color = Color.GREEN;
                    break;
                case "In Progress":
                    color = Color.YELLOW;
                    break;
                case "Open":
                default:
                    color = Color.RED;
                    break;
            }
            itemView.findViewById(R.id.cardView).setBackgroundColor(color);
        }
    }
}
