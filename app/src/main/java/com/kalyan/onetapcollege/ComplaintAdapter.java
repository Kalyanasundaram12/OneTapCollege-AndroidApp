package com.kalyan.onetapcollege;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ViewHolder> {

    private List<Map<String, String>> complaintsData;
    private String[] statusOptions = {"Open", "In Progress", "Close"};
    private String username;

    public ComplaintAdapter(String username) {
        this.username = username;
    }

    public void setComplaintsData(List<Map<String, String>> complaintsData) {
        this.complaintsData = complaintsData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complaint, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView complaintIdTextView;
        private TextView blockTextView;
        private TextView descriptionTextView;
        private TextView nameTextView;
        private Spinner statusSpinner;
        private TextView titleTextView;
        private TextView typeTextView;
        private DatabaseReference databaseReference;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            complaintIdTextView = itemView.findViewById(R.id.complaintIdTextView);
            blockTextView = itemView.findViewById(R.id.blockTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            statusSpinner = itemView.findViewById(R.id.statusSpinner);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Complaints").child(username);


            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(),
                    android.R.layout.simple_spinner_item, statusOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            statusSpinner.setAdapter(adapter);

            // Set listener for Spinner item selection
            statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Update status in Firebase when item is selected
                    String complaintId = complaintIdTextView.getText().toString();
                    String status = statusOptions[position];
                    updateStatusInFirebase(complaintId, status);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Handle nothing selected event
                }
            });
        }

        public void bind(Map<String, String> complaintData) {
            complaintIdTextView.setText(complaintData.get("complaintId"));
            blockTextView.setText(complaintData.get("block"));
            descriptionTextView.setText(complaintData.get("description"));
            nameTextView.setText(complaintData.get("name"));
            titleTextView.setText(complaintData.get("title"));
            typeTextView.setText(complaintData.get("type"));

            // Set selected status
            String status = complaintData.get("status");
            int statusIndex = getStatusIndex(status);
            if (statusIndex != -1) {
                statusSpinner.setSelection(statusIndex);
            }
        }

        private int getStatusIndex(String status) {
            for (int i = 0; i < statusOptions.length; i++) {
                if (statusOptions[i].equals(status)) {
                    return i;
                }
            }
            return -1;
        }

        private void updateStatusInFirebase(String complaintId, String status) {
            // Update status in Firebase Realtime Database
            databaseReference.child(complaintId).child("status").setValue(status);
        }
    }
}
