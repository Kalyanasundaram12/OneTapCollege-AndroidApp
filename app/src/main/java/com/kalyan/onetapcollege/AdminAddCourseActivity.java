package com.kalyan.onetapcollege;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminAddCourseActivity extends AppCompatActivity {

    private EditText courseCodeEditText, courseNameEditText, seatsEditText, slot1EditText, slot2EditText, editTextCourseCredits;
    private Button addButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_course);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("courses");

        courseCodeEditText = findViewById(R.id.editTextCourseCode);
        courseNameEditText = findViewById(R.id.editTextCourseName);
        seatsEditText = findViewById(R.id.editTextSeats);
        slot1EditText = findViewById(R.id.editTextSlot1);
        slot2EditText = findViewById(R.id.editTextSlot2);
        editTextCourseCredits = findViewById(R.id.editTextCourseCredits);
        addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse();
            }
        });
    }

    private void addCourse() {
        String courseCode = courseCodeEditText.getText().toString().trim();
        String courseName = courseNameEditText.getText().toString().trim();
        String seats = seatsEditText.getText().toString().trim();
        String slot1 = slot1EditText.getText().toString().trim();
        String slot2 = slot2EditText.getText().toString().trim();
        int creditHours = Integer.parseInt(editTextCourseCredits.getText().toString().trim());

        if (courseCode.isEmpty() || courseName.isEmpty() || seats.isEmpty() || slot1.isEmpty() || slot2.isEmpty() || creditHours==0) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Course2 course = new Course2(courseName, seats, slot1, slot2, creditHours);

        // Save course to Firebase
        databaseReference.child(courseCode).setValue(course);

        Toast.makeText(this, "Course added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
class Course2 {
    private String courseName;
    private String seats;
    private String slot1;
    private String slot2;

    private int creditHours;

    public Course2() {
        // Default constructor required for Firebase
    }

    public Course2(String courseName, String seats, String slot1, String slot2, int creditHours) {
        this.courseName = courseName;
        this.seats = seats;
        this.slot1 = slot1;
        this.slot2 = slot2;
        this.creditHours = creditHours;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getSlot1() {
        return slot1;
    }

    public void setSlot1(String slot1) {
        this.slot1 = slot1;
    }

    public String getSlot2() {
        return slot2;
    }

    public void setSlot2(String slot2) {
        this.slot2 = slot2;
    }
}