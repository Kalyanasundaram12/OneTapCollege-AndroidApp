package com.kalyan.onetapcollege;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewComplaintActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private ComplaintAdapter adapter;
    private TextView nameLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_complaint);

        // Retrieve username from intent extra
        String username = getIntent().getStringExtra("username");

        recyclerView = findViewById(R.id.recyclerViewComplaints);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ComplaintAdapter(username);
        recyclerView.setAdapter(adapter);
        nameLabel = findViewById(R.id.nameLabel);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Complaints").child(username);

        nameLabel.setText(username+"'s Complaints!");

        // Retrieve complaint IDs for the selected username from Firebase Realtime Database
        // Retrieve complaint data for the selected username from Firebase Realtime Database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Map<String, String>> complaintsData = new ArrayList<>();
                for (DataSnapshot complaintSnapshot : dataSnapshot.getChildren()) {
                    // Retrieve all fields for each complaint
                    Map<String, String> complaintData = new HashMap<>();
                    complaintData.put("complaintId", complaintSnapshot.getKey());
                    complaintData.put("block", complaintSnapshot.child("block").getValue(String.class));
                    complaintData.put("description", complaintSnapshot.child("description").getValue(String.class));
                    complaintData.put("name", complaintSnapshot.child("name").getValue(String.class));
                    complaintData.put("status", complaintSnapshot.child("status").getValue(String.class));
                    complaintData.put("title", complaintSnapshot.child("title").getValue(String.class));
                    complaintData.put("type", complaintSnapshot.child("type").getValue(String.class));
                    // Add complaint data to the list
                    complaintsData.add(complaintData);
                }
                // Update RecyclerView with complaint data
                adapter.setComplaintsData(complaintsData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled event
            }
        });


        // Adjust insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainComplaints), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
