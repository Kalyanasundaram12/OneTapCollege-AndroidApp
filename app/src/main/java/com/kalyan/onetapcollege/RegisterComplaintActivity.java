package com.kalyan.onetapcollege;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterComplaintActivity extends AppCompatActivity {

    private String userId, password, email, username;
    private EditText nameEditText, titleEditText, descriptionEditText;
    private Spinner blockSpinner, typeSpinner;
    private Button submitButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complaint);
//        EdgeToEdge.enable(this);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Complaints");

        // Get intent data
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        password = intent.getStringExtra("password");
        email = intent.getStringExtra("email");
        username = intent.getStringExtra("username");

        // Initialize views
        nameEditText = findViewById(R.id.name_edit_text);
        titleEditText = findViewById(R.id.title_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        blockSpinner = findViewById(R.id.block_spinner);
        ArrayAdapter<CharSequence> blockAdapter = ArrayAdapter.createFromResource(this,
                R.array.block_options, android.R.layout.simple_spinner_item);
        blockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blockSpinner.setAdapter(blockAdapter);

        typeSpinner = findViewById(R.id.type_spinner);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.type_options, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        submitButton = findViewById(R.id.submit_button);

        // Set submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComplaint();
            }
        });
    }

    private void submitComplaint() {
        // Get user input
        String name = nameEditText.getText().toString().trim();
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String block = blockSpinner.getSelectedItem().toString();
        String type = typeSpinner.getSelectedItem().toString();

        // Generate complaint ID
        String complaintId = databaseReference.push().getKey();

        // Create Complaint object
        Complaint complaint = new Complaint(name, title, block, type, description, "Open");

        // Store the complaint in Firebase Realtime Database
        databaseReference.child(username).child(complaintId).setValue(complaint);
         Toast.makeText(this, "Complaint submitted successfully", Toast.LENGTH_SHORT).show();
         finish(); // Finish this activity
    }
}
class Complaint {
    private String name;
    private String title;
    private String block;
    private String type;
    private String description;

    private String status;

    public Complaint() {
        // Default constructor required for Firebase
    }

    public Complaint(String name, String title, String block, String type, String description, String status) {
        this.name = name;
        this.title = title;
        this.block = block;
        this.type = type;
        this.description = description;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

