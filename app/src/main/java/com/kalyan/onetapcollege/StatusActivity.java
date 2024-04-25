package com.kalyan.onetapcollege;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatusActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private ComplaintAdapterDisplay adapter;
    private String username;

    private TextView nameLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
//        EdgeToEdge.enable(this);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        nameLabel = findViewById(R.id.nameLabel);
        recyclerView = findViewById(R.id.recyclerViewComplaints);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ComplaintAdapterDisplay();
        recyclerView.setAdapter(adapter);

        nameLabel.setText(username+"'s Complaints!");

        // Retrieve complaints data from Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Complaints").child(username);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Map<String, String>> complaints = new ArrayList<>();
                for (DataSnapshot complaintSnapshot : dataSnapshot.getChildren()) {
                    String complaintId = complaintSnapshot.getKey(); // Get the complaintId
                    Map<String, String> complaintData = (Map<String, String>) complaintSnapshot.getValue();
                    complaintData.put("complaintId", complaintId); // Add complaintId to complaintData
                    complaints.add(complaintData);
                }
                adapter.setComplaintsData(complaints);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle onCancelled event
            }
        });
    }
}
